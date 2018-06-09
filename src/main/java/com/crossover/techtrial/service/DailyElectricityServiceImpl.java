package com.crossover.techtrial.service;

import com.crossover.techtrial.dto.DailyElectricityDTO;
import com.crossover.techtrial.model.DailyElectricity;
import com.crossover.techtrial.model.Panel;
import com.crossover.techtrial.repository.DailyElectricityRepository;
import com.crossover.techtrial.service.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

/**
 * Implementation of {@link DailyElectricityService}.
 *
 * @author Marcos Barbero
 */
@Service
public class DailyElectricityServiceImpl implements DailyElectricityService {

    private final DailyElectricityRepository dailyElectricityRepository;
    private final PanelService panelService;

    public DailyElectricityServiceImpl(final DailyElectricityRepository dailyElectricityRepository,
                                       final PanelService panelService) {
        this.dailyElectricityRepository = dailyElectricityRepository;
        this.panelService = panelService;
    }

    @Override
    public DailyElectricity save(DailyElectricity dailyElectricity) {
        return this.dailyElectricityRepository.save(dailyElectricity);
    }

    @Override
    public List<DailyElectricityDTO> getAllDailyElectricityByPanelId(String panelSerial) {
        Panel panel = this.panelService.getBySerial(panelSerial);
        List<DailyElectricity> entities = this.dailyElectricityRepository.findAllByPanelId(panel.getId());

        if (entities.isEmpty()) {
            throw new ResourceNotFoundException(format("No daily electricity report found for the panel '%s'", panelSerial));
        }

        return entities.stream()
                .map(entity -> new DailyElectricityDTO(
                                entity.getPanel().getId(),
                                entity.getReadingAt().toLocalDate(),
                                entity.getGeneratedElectricitySum(),
                                entity.getGeneratedElectricityAverage().longValue(),
                                entity.getGeneratedElectricityMin(),
                                entity.getGeneratedElectricityMax()))
                .collect(toList());
    }
}
