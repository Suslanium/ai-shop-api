package testtask.shift.shopapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import testtask.shift.shopapi.model.monitor.Monitor;
import testtask.shift.shopapi.service.MonitorService;
import testtask.shift.shopapi.service.UsageStatsService;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MonitorController.class)
class MonitorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MonitorService monitorService;

    @MockBean
    private UsageStatsService usageStatsService;

    @Test
    void getMonitors_returnsList() throws Exception {
        List<Monitor> monitors = List.of(
                new Monitor(1L, "MN-1", "Samsung", new BigDecimal("299.99"), 6L, 27.0),
                new Monitor(2L, "MN-2", "LG", new BigDecimal("329.99"), 2L, 32.0)
        );
        when(monitorService.getAllMonitors()).thenReturn(monitors);

        mockMvc.perform(get("/api/monitors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].diagonal").value(27.0))
                .andExpect(jsonPath("$[1].producer").value("LG"));
    }

    @Test
    void getMonitor_returnsEntity() throws Exception {
        Monitor monitor = new Monitor(5L, "MN-5", "Asus", new BigDecimal("199.99"), 1L, 24.0);
        when(monitorService.getMonitor(5L)).thenReturn(monitor);

        mockMvc.perform(get("/api/monitors/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.seriesNumber").value("MN-5"))
                .andExpect(jsonPath("$.diagonal").value(24.0));
    }

    @Test
    void getMonitor_returnsNotFound() throws Exception {
        when(monitorService.getMonitor(99L)).thenThrow(new ResourceNotFoundException("Monitor not found"));

        mockMvc.perform(get("/api/monitors/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createMonitor_delegatesToService() throws Exception {
        Monitor payload = new Monitor("MN-9", "BenQ", new BigDecimal("249.99"), 8L, 27.0);
        Monitor saved = new Monitor(9L, payload.getSeriesNumber(), payload.getProducer(),
                payload.getPrice(), payload.getNumberOfProductsInStock(), payload.getDiagonal());
        when(monitorService.save(any(Monitor.class))).thenReturn(saved);

        mockMvc.perform(post("/api/monitors/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(9))
                .andExpect(jsonPath("$.producer").value("BenQ"));
    }

    @Test
    void editMonitor_setsIdFromPath() throws Exception {
        Monitor existing = new Monitor(7L, "MN-7", "Samsung", new BigDecimal("299.99"), 3L, 27.0);
        Monitor payload = new Monitor(999L, "MN-7A", "Samsung", new BigDecimal("329.99"), 3L, 32.0);
        when(monitorService.getMonitor(7L)).thenReturn(existing);
        when(monitorService.save(any(Monitor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/api/monitors/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.diagonal").value(32.0));

        ArgumentCaptor<Monitor> captor = ArgumentCaptor.forClass(Monitor.class);
        verify(monitorService).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(7L);
    }
}
