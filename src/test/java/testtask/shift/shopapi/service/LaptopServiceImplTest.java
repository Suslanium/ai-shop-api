package testtask.shift.shopapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import testtask.shift.shopapi.model.laptop.Laptop;
import testtask.shift.shopapi.model.laptop.LaptopSize;
import testtask.shift.shopapi.repository.LaptopRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LaptopServiceImplTest {

    @Mock
    private LaptopRepository laptopRepository;

    @InjectMocks
    private LaptopServiceImpl laptopService;

    @Test
    void getAllLaptops_returnsRepositoryResults() {
        List<Laptop> laptops = List.of(buildLaptop(1L));
        when(laptopRepository.findAll()).thenReturn(laptops);

        Iterable<Laptop> result = laptopService.getAllLaptops();

        assertThat(result).isSameAs(laptops);
        verify(laptopRepository).findAll();
    }

    @Test
    void getLaptop_returnsEntityWhenFound() {
        Laptop laptop = buildLaptop(2L);
        when(laptopRepository.findById(2L)).thenReturn(Optional.of(laptop));

        Laptop result = laptopService.getLaptop(2L);

        assertThat(result).isSameAs(laptop);
        verify(laptopRepository).findById(2L);
    }

    @Test
    void getLaptop_throwsWhenMissing() {
        when(laptopRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> laptopService.getLaptop(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Laptop not found");
    }

    @Test
    void save_delegatesToRepository() {
        Laptop laptop = buildLaptop(3L);
        when(laptopRepository.save(laptop)).thenReturn(laptop);

        Laptop result = laptopService.save(laptop);

        assertThat(result).isSameAs(laptop);
        verify(laptopRepository).save(laptop);
    }

    private Laptop buildLaptop(Long id) {
        return new Laptop(id, "LP-" + id, "Lenovo", new BigDecimal("899.00"), 5L, LaptopSize.Inch14);
    }
}
