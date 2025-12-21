package testtask.shift.shopapi.service;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UsageStatsInterceptor implements HandlerInterceptor {
    private final UsageStatsService usageStatsService;

    public UsageStatsInterceptor(UsageStatsService usageStatsService) {
        this.usageStatsService = usageStatsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uri = request.getRequestURI();
        if (!uri.startsWith("/error")) {
            String key = request.getMethod() + " " + uri;
            usageStatsService.increment(key);
        }
        return true;
    }
}
