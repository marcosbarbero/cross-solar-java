package com.crossover.techtrial.batch;

import com.crossover.techtrial.model.DailyElectricity;
import com.crossover.techtrial.model.HourlyElectricity;
import com.crossover.techtrial.model.Panel;
import com.crossover.techtrial.service.DailyElectricityService;
import com.crossover.techtrial.service.HourlyElectricityService;
import com.crossover.techtrial.service.PanelService;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.LongSummaryStatistics;

/**
 * Job to consolidate on daily bases all the electricity generated.s
 *
 * @author Marcos Barbero
 */
@Component
@ConditionalOnProperty(value = "crosssolar.scheduler.enabled", havingValue = "true", matchIfMissing = true)
public class DailyElectricityAggregatorJob {

    // TODO: externalize this configuration
    static final String ZONE_ID = "CST6CDT";
    private static final String CRON = "0 0 0 * * *";

    private final HourlyElectricityService hourlyElectricityService;
    private final DailyElectricityService dailyElectricityService;
    private final PanelService panelService;

    public DailyElectricityAggregatorJob(final HourlyElectricityService hourlyElectricityService,
                                         final DailyElectricityService dailyElectricityService,
                                         final PanelService panelService) {
        this.hourlyElectricityService = hourlyElectricityService;
        this.dailyElectricityService = dailyElectricityService;
        this.panelService = panelService;
    }

    @Scheduled(cron = CRON, zone = ZONE_ID)
    @SchedulerLock(name = "consolidateDailyElectricityGeneration")
    public void consolidateDailyElectricityGeneration() {
        process();
    }

    private void process() {
        List<Panel> panels = this.panelService.getAll();

        for (Panel panel : panels) {
            List<HourlyElectricity> hourlyElectricities = this.hourlyElectricityService
                    .getAllHourlyElectricityByReadingAtBetween(yesterdayAtStartOfDay(), yesterdayAtEndOfDay(), panel);

            if (hourlyElectricities.isEmpty()) {
                continue;
            }

            LongSummaryStatistics summaryStatistics = hourlyElectricities.stream()
                    .mapToLong(HourlyElectricity::getGeneratedElectricity)
                    .summaryStatistics();

            DailyElectricity dailyElectricity = new DailyElectricity(panel,
                    summaryStatistics.getSum(),
                    summaryStatistics.getMin(),
                    summaryStatistics.getMax(),
                    summaryStatistics.getAverage(),
                    yesterdayAtStartOfDay());
            this.dailyElectricityService.save(dailyElectricity);
        }
    }

    private LocalDateTime yesterdayAtStartOfDay() {
        return LocalDate.now(ZoneId.of(ZONE_ID)).minusDays(1).atStartOfDay();
    }

    private LocalDateTime yesterdayAtEndOfDay() {
        LocalDate yesterday = LocalDate.now(ZoneId.of(ZONE_ID)).minusDays(1);
        return LocalDateTime.of(yesterday, LocalTime.MAX);
    }

}
