package testtask.shift.shopapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import testtask.shift.shopapi.model.hdd.HardDrive;
import testtask.shift.shopapi.repository.HardDriveRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HardDriveServiceImplTest {

    @Mock
    private HardDriveRepository hardDriveRepository;

    @InjectMocks
    private HardDriveServiceImpl hardDriveService;

    @Test
    void getAllHardDrives_returnsRepositoryResults() {
        List<HardDrive> hardDrives = List.of(buildHardDrive(1L));
        when(hardDriveRepository.findAll()).thenReturn(hardDrives);

        Iterable<HardDrive> result = hardDriveService.getAllHardDrives();

        assertThat(result).isSameAs(hardDrives);
        verify(hardDriveRepository).findAll();
    }

    @Test
    void getHardDrive_returnsEntityWhenFound() {
        HardDrive hardDrive = buildHardDrive(2L);
        when(hardDriveRepository.findById(2L)).thenReturn(Optional.of(hardDrive));

        HardDrive result = hardDriveService.getHardDrive(2L);

        assertThat(result).isSameAs(hardDrive);
        verify(hardDriveRepository).findById(2L);
    }

    @Test
    void getHardDrive_throwsWhenMissing() {
        when(hardDriveRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hardDriveService.getHardDrive(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("HardDrive not found");
    }

    @Test
    void save_delegatesToRepository() {
        HardDrive hardDrive = buildHardDrive(3L);
        when(hardDriveRepository.save(hardDrive)).thenReturn(hardDrive);

        HardDrive result = hardDriveService.save(hardDrive);

        assertThat(result).isSameAs(hardDrive);
        verify(hardDriveRepository).save(hardDrive);
    }

    private HardDrive buildHardDrive(Long id) {
        return new HardDrive(id, "HD-" + id, "Seagate", new BigDecimal("129.99"), 10L, 512.0);
    }
}
