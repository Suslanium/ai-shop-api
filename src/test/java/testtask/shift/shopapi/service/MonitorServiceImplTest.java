package testtask.shift.shopapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import testtask.shift.shopapi.model.monitor.Monitor;
import testtask.shift.shopapi.repository.MonitorRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MonitorServiceImplTest {

    @Mock
    private MonitorRepository monitorRepository;

    @InjectMocks
    private MonitorServiceImpl monitorService;

    @Test
    void getAllMonitors_returnsRepositoryResults() {
        List<Monitor> monitors = List.of(buildMonitor(1L));
        when(monitorRepository.findAll()).thenReturn(monitors);

        Iterable<Monitor> result = monitorService.getAllMonitors();

        assertThat(result).isSameAs(monitors);
        verify(monitorRepository).findAll();
    }

    @Test
    void getMonitor_returnsEntityWhenFound() {
        Monitor monitor = buildMonitor(2L);
        when(monitorRepository.findById(2L)).thenReturn(Optional.of(monitor));

        Monitor result = monitorService.getMonitor(2L);

        assertThat(result).isSameAs(monitor);
        verify(monitorRepository).findById(2L);
    }

    @Test
    void getMonitor_throwsWhenMissing() {
        when(monitorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> monitorService.getMonitor(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Monitor not found");
    }

    @Test
    void save_delegatesToRepository() {
        Monitor monitor = buildMonitor(3L);
        when(monitorRepository.save(monitor)).thenReturn(monitor);

        Monitor result = monitorService.save(monitor);

        assertThat(result).isSameAs(monitor);
        verify(monitorRepository).save(monitor);
    }

    private Monitor buildMonitor(Long id) {
        return new Monitor(id, "MN-" + id, "Dell", new BigDecimal("249.99"), 12L, 27.0);
    }
}
