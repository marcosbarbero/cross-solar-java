package com.crossover.techtrial.service;

import com.crossover.techtrial.model.Panel;
import com.crossover.techtrial.repository.PanelRepository;
import com.crossover.techtrial.service.exceptions.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.crossover.techtrial.utils.TestUtils.panel;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link PanelService}.
 *
 * @author Marcos Barbero
 */
public class PanelServiceTest {

    private PanelService panelService;
    private PanelRepository panelRepository;

    @Before
    public void setUp() {
        panelRepository = mock(PanelRepository.class);
        this.panelService = new PanelServiceImpl(panelRepository);
    }

    @Test
    public void register() {
        this.panelService.register(panel());
    }

    @Test
    public void findBySerial() {
        Panel expected = panel();
        when(this.panelRepository.findBySerial(any())).thenReturn(expected);

        Panel result = this.panelService.getBySerial(expected.getSerial());

        assertEquals(expected, result);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findBySerial_notFound() {
        when(this.panelRepository.findBySerial(any())).thenReturn(null);

        this.panelService.getBySerial("random-panel-id");
    }

    @Test
    public void getAll() {
        List<Panel> expected = singletonList(panel());
        when(this.panelRepository.findAll()).thenReturn(expected);

        List<Panel> result = this.panelService.getAll();

        assertEquals(expected, result);
    }
}