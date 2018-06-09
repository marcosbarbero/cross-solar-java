package com.crossover.techtrial.controller;

import com.crossover.techtrial.model.HourlyElectricity;
import com.crossover.techtrial.model.Panel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.crossover.techtrial.utils.TestUtils.hourlyElectricity;
import static com.crossover.techtrial.utils.TestUtils.panel;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * PanelControllerTest class will test all APIs in PanelController.java.
 *
 * @author Crossover
 * @author Marcos Barbero
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "crosssolar.scheduler.enabled=false")
public class PanelControllerTest {

    private static final String SERIAL_ID = "1234567890123456";
    private static final String INVALID_SERIAL_ID = "987129387";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testPanelShouldBeRegistered() throws Exception {
        Panel panel = panel();
        this.mockMvc.perform(post("/api/register")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(panel)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand", is(panel.getBrand())))
                .andExpect(jsonPath("$.latitude", notNullValue()))
                .andExpect(jsonPath("$.longitude", notNullValue()))
                .andExpect(jsonPath("$.serial", is(panel.getSerial())))
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    public void testSaveHourlyElectricity() throws Exception {
        HourlyElectricity hourlyElectricity = hourlyElectricity();
        this.mockMvc.perform(post("/api/panels/{panel-serial}/hourly", SERIAL_ID)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(hourlyElectricity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.generatedElectricity", notNullValue()))
                .andExpect(jsonPath("$.readingAt", notNullValue()));
    }

    @Test
    public void testGetHourlyElectricity() throws Exception {
        this.mockMvc.perform(get("/api/panels/{panel-serial}/hourly", SERIAL_ID)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].generatedElectricity", notNullValue()))
                .andExpect(jsonPath("$.content[0].readingAt", notNullValue()));
    }

    @Test
    public void testGetHourlyElectricityNotFound() throws Exception {
        this.mockMvc.perform(get("/api/panels/{panel-serial}/hourly", INVALID_SERIAL_ID)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllDailyElectricityFromYesterday() throws Exception {
        this.mockMvc.perform(get("/api/panels/{panel-serial}/daily", SERIAL_ID)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date", notNullValue()))
                .andExpect(jsonPath("$[0].average", notNullValue()))
                .andExpect(jsonPath("$[0].min", notNullValue()))
                .andExpect(jsonPath("$[0].max", notNullValue()))
                .andExpect(jsonPath("$[0].sum", notNullValue()));
    }

    @Test
    public void testGetAllDailyElectricityFromYesterdayNotFound() throws Exception {
        this.mockMvc.perform(get("/api/panels/{panel-serial}/daily", INVALID_SERIAL_ID)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
