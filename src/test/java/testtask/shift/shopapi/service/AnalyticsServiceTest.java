package testtask.shift.shopapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import testtask.shift.shopapi.model.analytics.InventoryCountsResponse;
import testtask.shift.shopapi.repository.HardDriveRepository;
import testtask.shift.shopapi.repository.LaptopRepository;
import testtask.shift.shopapi.repository.MonitorRepository;
import testtask.shift.shopapi.repository.PersonalComputerRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private HardDriveRepository hardDriveRepository;

    @Mock
    private LaptopRepository laptopRepository;

    @Mock
    private MonitorRepository monitorRepository;

    @Mock
    private PersonalComputerRepository personalComputerRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    @Test
    void getInventoryCounts_sumsRepositoryCounts() {
        when(hardDriveRepository.count()).thenReturn(4L);
        when(laptopRepository.count()).thenReturn(2L);
        when(monitorRepository.count()).thenReturn(3L);
        when(personalComputerRepository.count()).thenReturn(1L);

        InventoryCountsResponse response = analyticsService.getInventoryCounts();

        assertThat(response.getTotal()).isEqualTo(10L);
        assertThat(response.getHardDrives()).isEqualTo(4L);
        assertThat(response.getLaptops()).isEqualTo(2L);
        assertThat(response.getMonitors()).isEqualTo(3L);
        assertThat(response.getPersonalComputers()).isEqualTo(1L);
    }
}
