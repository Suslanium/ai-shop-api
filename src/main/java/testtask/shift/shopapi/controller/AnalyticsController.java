package testtask.shift.shopapi.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import testtask.shift.shopapi.model.analytics.InventoryCountsResponse;
import testtask.shift.shopapi.model.analytics.UsageStatsResponse;
import testtask.shift.shopapi.service.AnalyticsService;
import testtask.shift.shopapi.service.UsageStatsService;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;
    private final UsageStatsService usageStatsService;

    public AnalyticsController(AnalyticsService analyticsService, UsageStatsService usageStatsService) {
        this.analyticsService = analyticsService;
        this.usageStatsService = usageStatsService;
    }

    @GetMapping(value = "/counts", produces = "application/json")
    public @NotNull InventoryCountsResponse getCounts() {
        return analyticsService.getInventoryCounts();
    }

    @GetMapping(value = "/usage", produces = "application/json")
    public @NotNull UsageStatsResponse getUsage() {
        return new UsageStatsResponse(usageStatsService.snapshot());
    }
}
