package testtask.shift.shopapi.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.validation.annotation.Validated;
import testtask.shift.shopapi.model.monitor.Monitor;

@Validated
public interface MonitorService {
    @NotNull
    Iterable<Monitor> getAllMonitors();

    Monitor getMonitor(long id);

    Monitor save(Monitor monitor);
}
