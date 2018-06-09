package com.crossover.techtrial.service;

import com.crossover.techtrial.dto.DailyElectricityDTO;
import com.crossover.techtrial.model.DailyElectricity;

import java.util.List;

/**
 * Services for {@link DailyElectricity}.
 *
 * @author Marcos Barbero
 */
public interface DailyElectricityService {

    DailyElectricity save(DailyElectricity dailyElectricity);

    List<DailyElectricityDTO> getAllDailyElectricityByPanelId(String panelSerial);
}
