package testtask.shift.shopapi.model.analytics;

import java.util.Collections;
import java.util.Map;

public class UsageStatsResponse {
    private final Map<String, Long> endpoints;

    public UsageStatsResponse(Map<String, Long> endpoints) {
        this.endpoints = Collections.unmodifiableMap(endpoints);
    }

    public Map<String, Long> getEndpoints() {
        return endpoints;
    }
}
