package testtask.shift.shopapi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import testtask.shift.shopapi.model.analytics.InventoryCountsResponse;
import testtask.shift.shopapi.service.AnalyticsService;
import testtask.shift.shopapi.service.UsageStatsService;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnalyticsController.class)
class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnalyticsService analyticsService;

    @MockBean
    private UsageStatsService usageStatsService;

    @Test
    void getCounts_returnsInventoryCounts() throws Exception {
        InventoryCountsResponse response = new InventoryCountsResponse(10L, 4L, 2L, 3L, 1L);
        when(analyticsService.getInventoryCounts()).thenReturn(response);

        mockMvc.perform(get("/api/analytics/counts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(10))
                .andExpect(jsonPath("$.hardDrives").value(4))
                .andExpect(jsonPath("$.laptops").value(2))
                .andExpect(jsonPath("$.monitors").value(3))
                .andExpect(jsonPath("$.personalComputers").value(1));
    }

    @Test
    void getUsage_returnsEndpointStats() throws Exception {
        when(usageStatsService.snapshot()).thenReturn(Map.of(
                "GET /api/hdds", 2L,
                "POST /api/laptops/add", 1L
        ));

        mockMvc.perform(get("/api/analytics/usage"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.endpoints['GET /api/hdds']").value(2))
                .andExpect(jsonPath("$.endpoints['POST /api/laptops/add']").value(1));
    }
}
