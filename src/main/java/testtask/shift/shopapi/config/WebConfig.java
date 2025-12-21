package testtask.shift.shopapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import testtask.shift.shopapi.service.UsageStatsInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final UsageStatsInterceptor usageStatsInterceptor;

    public WebConfig(UsageStatsInterceptor usageStatsInterceptor) {
        this.usageStatsInterceptor = usageStatsInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(usageStatsInterceptor);
    }
}
