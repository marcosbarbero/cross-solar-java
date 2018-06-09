package com.crossover.techtrial.service;

import com.crossover.techtrial.model.HourlyElectricity;
import com.crossover.techtrial.model.Panel;
import com.crossover.techtrial.repository.HourlyElectricityRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static com.crossover.techtrial.utils.TestUtils.*;
import static java.time.LocalDate.now;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link HourlyElectricityService}.
 *
 * @author Marcos Barbero
 */
public class HourlyElectricityServiceTest {

    private HourlyElectricityService hourlyElectricityService;
    private HourlyElectricityRepository hourlyElectricityRepository;

    @Before
    public void setUp() {
        PanelService panelService = mock(PanelService.class);
        when(panelService.getBySerial(any())).thenReturn(panel());

        this.hourlyElectricityRepository = mock(HourlyElectricityRepository.class);
        this.hourlyElectricityService = new HourlyElectricityServiceImpl(hourlyElectricityRepository, panelService);

    }

    @Test
    public void test_save() {
        HourlyElectricity expected = hourlyElectricity();

        when(this.hourlyElectricityRepository.save(any())).thenReturn(expected);

        HourlyElectricity result = this.hourlyElectricityService.save(expected, "random-panel-id");

        assertEquals(expected, result);
    }

    @Test
    public void test_get_all_hourly_electricity_by_panel_id() {
        List<HourlyElectricity> hourlyElectricityList = singletonList(hourlyElectricity());
        Page<HourlyElectricity> expected = new PageImpl<>(hourlyElectricityList);

        when(this.hourlyElectricityRepository.findAllByPanelId(any(), any())).thenReturn(expected);

        Page<HourlyElectricity> result = this.hourlyElectricityService.getAllHourlyElectricityByPanelId("random-panel-id", pageable());

        assertEquals(expected, result);
    }

    @Test
    public void test_get_hourly_electricity_by_reading_at_between_and_panel() {
        List<HourlyElectricity> expected = singletonList(hourlyElectricity());

        when(this.hourlyElectricityRepository.findByReadingAtBetweenAndPanel(any(), any(), any())).thenReturn(expected);

        List<HourlyElectricity> result = this.hourlyElectricityService.getAllHourlyElectricityByReadingAtBetween(now().atStartOfDay(), now().atStartOfDay(), new Panel(1L));

        assertFalse(result.isEmpty());
        assertEquals(expected, result);
    }

}