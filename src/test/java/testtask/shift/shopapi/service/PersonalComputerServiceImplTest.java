package testtask.shift.shopapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import testtask.shift.shopapi.model.pc.FormFactor;
import testtask.shift.shopapi.model.pc.PersonalComputer;
import testtask.shift.shopapi.repository.PersonalComputerRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonalComputerServiceImplTest {

    @Mock
    private PersonalComputerRepository personalComputerRepository;

    @InjectMocks
    private PersonalComputerServiceImpl personalComputerService;

    @Test
    void getAllPersonalComputers_returnsRepositoryResults() {
        List<PersonalComputer> computers = List.of(buildComputer(1L));
        when(personalComputerRepository.findAll()).thenReturn(computers);

        Iterable<PersonalComputer> result = personalComputerService.getAllPersonalComputers();

        assertThat(result).isSameAs(computers);
        verify(personalComputerRepository).findAll();
    }

    @Test
    void getPersonalComputer_returnsEntityWhenFound() {
        PersonalComputer computer = buildComputer(2L);
        when(personalComputerRepository.findById(2L)).thenReturn(Optional.of(computer));

        PersonalComputer result = personalComputerService.getPersonalComputer(2L);

        assertThat(result).isSameAs(computer);
        verify(personalComputerRepository).findById(2L);
    }

    @Test
    void getPersonalComputer_throwsWhenMissing() {
        when(personalComputerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> personalComputerService.getPersonalComputer(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("PC not found");
    }

    @Test
    void save_delegatesToRepository() {
        PersonalComputer computer = buildComputer(3L);
        when(personalComputerRepository.save(computer)).thenReturn(computer);

        PersonalComputer result = personalComputerService.save(computer);

        assertThat(result).isSameAs(computer);
        verify(personalComputerRepository).save(computer);
    }

    private PersonalComputer buildComputer(Long id) {
        return new PersonalComputer(id, "PC-" + id, "HP", new BigDecimal("1099.00"), 3L, FormFactor.Desktop);
    }
}
