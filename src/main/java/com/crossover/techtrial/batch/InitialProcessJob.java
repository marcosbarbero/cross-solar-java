package com.crossover.techtrial.batch;

import com.crossover.techtrial.dto.DailyElectricityDTO;
import com.crossover.techtrial.model.DailyElectricity;
import com.crossover.techtrial.model.InitialProcess;
import com.crossover.techtrial.model.InitialProcessStatus;
import com.crossover.techtrial.model.Panel;
import com.crossover.techtrial.repository.DailyElectricityRepository;
import com.crossover.techtrial.repository.InitialProcessRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.crossover.techtrial.batch.DailyElectricityAggregatorJob.ZONE_ID;

/**
 * Class responsible to verify and process the first initial data.
 *
 * @author Marcos Barbero
 */
@Component
public class InitialProcessJob implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(InitialProcessJob.class);

    private static final String SQL_AGGREGATE_DAILY = "select e.panel_id as panel_id, e.reading_at as e_date," +
            " sum(e.generated_electricity) as e_sum, min(e.generated_electricity) as e_min," +
            " max(e.generated_electricity) as e_max, avg(e.generated_electricity) as e_avg" +
            " from hourly_electricity as e group by e.reading_at, e.panel_id";

    private final InitialProcessRepository initialProcessRepository;
    private final DailyElectricityRepository dailyElectricityRepository;
    private final JdbcTemplate jdbcTemplate;

    public InitialProcessJob(final InitialProcessRepository initialProcessRepository,
                             final DailyElectricityRepository dailyElectricityRepository,
                             final JdbcTemplate jdbcTemplate) {
        this.initialProcessRepository = initialProcessRepository;
        this.dailyElectricityRepository = dailyElectricityRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        Optional<InitialProcess> initialProcess = this.initialProcessRepository.findTopByOrderById();
        if (initialProcess.isPresent()) {
            InitialProcessStatus status = initialProcess.get().getStatus();

            if (status != InitialProcessStatus.FAILED) {
                return;
            }
        }

        InitialProcess savedStatus = updateStatus(null, InitialProcessStatus.STARTED);
        try {

            List<DailyElectricityDTO> result = this.jdbcTemplate.query(SQL_AGGREGATE_DAILY, new DailyAggregationRowMapper());

            if (result.isEmpty()) {
                return;
            }

            List<DailyElectricity> dailyElectricities = new ArrayList<>();
            for (DailyElectricityDTO dto : result) {
                DailyElectricity daily = new DailyElectricity();
                daily.setGeneratedElectricityAverage(dto.getAverage().doubleValue());
                daily.setGeneratedElectricityMax(dto.getMax());
                daily.setGeneratedElectricityMin(dto.getMin());
                daily.setGeneratedElectricitySum(dto.getSum());
                daily.setReadingAt(dto.getDate().atStartOfDay());
                daily.setPanel(new Panel(dto.getPanelId()));

                dailyElectricities.add(daily);
            }

            this.dailyElectricityRepository.saveAll(dailyElectricities);
            updateStatus(savedStatus.getId(), InitialProcessStatus.FINISHED);
        } catch (Exception ex) {
            logger.error("The initial daily process failed", ex);
            updateStatus(savedStatus.getId(), InitialProcessStatus.FAILED);
        }

    }

    private InitialProcess updateStatus(Long id, InitialProcessStatus status) {
        return this.initialProcessRepository.save(new InitialProcess(id, status));
    }

    /**
     * Row mapper for daily aggregation.
     *
     * @author Marcos Barbero
     */
    class DailyAggregationRowMapper implements RowMapper<DailyElectricityDTO> {

        @Override
        public DailyElectricityDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new DailyElectricityDTO(
                    rs.getLong("panel_id"),
                    toLocalDateTime(rs.getDate("e_date")),
                    rs.getLong("e_sum"),
                    rs.getLong("e_avg"),
                    rs.getLong("e_min"),
                    rs.getLong("e_max"));
        }

        private LocalDate toLocalDateTime(Date date) {
            return date.toLocalDate().atStartOfDay(ZoneId.of(ZONE_ID)).toLocalDate();
        }

    }
}
