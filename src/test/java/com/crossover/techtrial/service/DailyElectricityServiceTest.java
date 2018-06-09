package com.crossover.techtrial.service;

import com.crossover.techtrial.dto.DailyElectricityDTO;
import com.crossover.techtrial.model.DailyElectricity;
import com.crossover.techtrial.repository.DailyElectricityRepository;
import com.crossover.techtrial.service.exceptions.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static com.crossover.techtrial.utils.TestUtils.dailyElectricity;
import static com.crossover.techtrial.utils.TestUtils.panel;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link DailyElectricityService}.
 *
 * @author Marcos Barbero
 */
public class DailyElectricityServiceTest {

    private DailyElectricityService dailyElectricityService;
    private DailyElectricityRepository dailyElectricityRepository;

    @Before
    public void setUp() {
        PanelService panelService = mock(PanelService.class);
        when(panelService.getBySerial(any())).thenReturn(panel());

        this.dailyElectricityRepository = mock(DailyElectricityRepository.class);
        this.dailyElectricityService = new DailyElectricityServiceImpl(this.dailyElectricityRepository, panelService);
    }

    @Test
    public void save() {
        DailyElectricity expected = dailyElectricity();
        when(this.dailyElectricityRepository.save(any())).thenReturn(expected);

        DailyElectricity result = this.dailyElectricityService.save(expected);

        assertEquals(expected, result);
    }

    @Test
    public void getAllDailyElectricityByPanelId() {
        DailyElectricity dailyElectricity = dailyElectricity();
        List<DailyElectricity> expected = Collections.singletonList(dailyElectricity);
        when(this.dailyElectricityRepository.findAllByPanelId(any())).thenReturn(expected);

        List<DailyElectricityDTO> result = this.dailyElectricityService.getAllDailyElectricityByPanelId("random-serial");

        for (DailyElectricityDTO dto : result) {
            assertEquals(dailyElectricity.getGeneratedElectricityAverage().longValue(), dto.getAverage().longValue());
            assertEquals(dailyElectricity.getGeneratedElectricityMax(), dto.getMax());
            assertEquals(dailyElectricity.getGeneratedElectricityMin(), dto.getMin());
            assertEquals(dailyElectricity.getGeneratedElectricitySum(), dto.getSum());
            assertEquals(dailyElectricity.getReadingAt().toLocalDate(), dto.getDate());
            assertEquals(dailyElectricity.getPanel().getId(), dto.getPanelId());
        }
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getAllDailyElectricityByPanelId_notFound() {
        when(this.dailyElectricityRepository.findAllByPanelId(any())).thenReturn(emptyList());
        this.dailyElectricityService.getAllDailyElectricityByPanelId("random-serial");
    }

}