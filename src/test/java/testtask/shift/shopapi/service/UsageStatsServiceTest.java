package testtask.shift.shopapi.service;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class UsageStatsServiceTest {

    @Test
    void snapshot_returnsCurrentCounters() {
        UsageStatsService service = new UsageStatsService();

        service.increment("GET /api/hdds");
        service.increment("GET /api/hdds");
        service.increment("POST /api/hdds/add");

        Map<String, Long> snapshot = service.snapshot();

        assertThat(snapshot).containsEntry("GET /api/hdds", 2L);
        assertThat(snapshot).containsEntry("POST /api/hdds/add", 1L);
    }
}
