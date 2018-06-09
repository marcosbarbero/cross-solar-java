package com.crossover.techtrial.batch;

import com.crossover.techtrial.model.DailyElectricity;
import com.crossover.techtrial.model.HourlyElectricity;
import com.crossover.techtrial.model.Panel;
import com.crossover.techtrial.service.DailyElectricityService;
import com.crossover.techtrial.service.HourlyElectricityService;
import com.crossover.techtrial.service.PanelService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.crossover.techtrial.utils.TestUtils.dailyElectricity;
import static com.crossover.techtrial.utils.TestUtils.hourlyElectricity;
import static com.crossover.techtrial.utils.TestUtils.panel;
import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DailyElectricityAggregatorJobTest {

    private DailyElectricityAggregatorJob dailyElectricityAggregatorJob;
    private HourlyElectricityService hourlyElectricityService;
    private DailyElectricityService dailyElectricityService;
    private PanelService panelService;

    @Before
    public void setUp() {
        this.hourlyElectricityService = mock(HourlyElectricityService.class);
        this.dailyElectricityService = mock(DailyElectricityService.class);
        this.panelService = mock(PanelService.class);
        this.dailyElectricityAggregatorJob = new DailyElectricityAggregatorJob(hourlyElectricityService, dailyElectricityService, panelService);
    }

    @Test
    public void testConsolidateDailyElectricityGeneration() {
        List<Panel> expectedPanels = singletonList(panel());
        when(this.panelService.getAll()).thenReturn(expectedPanels);

        List<HourlyElectricity> expectedHourly = singletonList(hourlyElectricity());
        when(this.hourlyElectricityService.getAllHourlyElectricityByReadingAtBetween(any(), any(), any())).thenReturn(expectedHourly);

        DailyElectricity expectedDaily = dailyElectricity();
        when(this.dailyElectricityService.save(any())).thenReturn(expectedDaily);

        this.dailyElectricityAggregatorJob.consolidateDailyElectricityGeneration();
    }

}