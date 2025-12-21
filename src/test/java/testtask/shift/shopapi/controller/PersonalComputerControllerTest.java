package testtask.shift.shopapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import testtask.shift.shopapi.model.pc.FormFactor;
import testtask.shift.shopapi.model.pc.PersonalComputer;
import testtask.shift.shopapi.service.PersonalComputerService;
import testtask.shift.shopapi.service.UsageStatsService;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonalComputerController.class)
class PersonalComputerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonalComputerService personalComputerService;

    @MockBean
    private UsageStatsService usageStatsService;

    @Test
    void getPersonalComputers_returnsList() throws Exception {
        List<PersonalComputer> computers = List.of(
                new PersonalComputer(1L, "PC-1", "HP", new BigDecimal("999.99"), 3L, FormFactor.Desktop),
                new PersonalComputer(2L, "PC-2", "Acer", new BigDecimal("899.99"), 2L, FormFactor.Monoblock)
        );
        when(personalComputerService.getAllPersonalComputers()).thenReturn(computers);

        mockMvc.perform(get("/api/pcs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].formFactor").value("Desktop"))
                .andExpect(jsonPath("$[1].producer").value("Acer"));
    }

    @Test
    void getPersonalComputer_returnsEntity() throws Exception {
        PersonalComputer computer = new PersonalComputer(5L, "PC-5", "Dell", new BigDecimal("1199.00"), 1L, FormFactor.Nettop);
        when(personalComputerService.getPersonalComputer(5L)).thenReturn(computer);

        mockMvc.perform(get("/api/pcs/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.seriesNumber").value("PC-5"))
                .andExpect(jsonPath("$.formFactor").value("Nettop"));
    }

    @Test
    void getPersonalComputer_returnsNotFound() throws Exception {
        when(personalComputerService.getPersonalComputer(99L)).thenThrow(new ResourceNotFoundException("PC not found"));

        mockMvc.perform(get("/api/pcs/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createPersonalComputer_delegatesToService() throws Exception {
        PersonalComputer payload = new PersonalComputer("PC-9", "Lenovo", new BigDecimal("1299.00"), 4L, FormFactor.Desktop);
        PersonalComputer saved = new PersonalComputer(9L, payload.getSeriesNumber(), payload.getProducer(),
                payload.getPrice(), payload.getNumberOfProductsInStock(), payload.getFormFactor());
        when(personalComputerService.save(any(PersonalComputer.class))).thenReturn(saved);

        mockMvc.perform(post("/api/pcs/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(9))
                .andExpect(jsonPath("$.formFactor").value("Desktop"));
    }

    @Test
    void editPersonalComputer_setsIdFromPath() throws Exception {
        PersonalComputer existing = new PersonalComputer(7L, "PC-7", "HP", new BigDecimal("999.99"), 2L, FormFactor.Nettop);
        PersonalComputer payload = new PersonalComputer(999L, "PC-7A", "HP", new BigDecimal("1099.99"), 2L, FormFactor.Monoblock);
        when(personalComputerService.getPersonalComputer(7L)).thenReturn(existing);
        when(personalComputerService.save(any(PersonalComputer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/api/pcs/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.formFactor").value("Monoblock"));

        ArgumentCaptor<PersonalComputer> captor = ArgumentCaptor.forClass(PersonalComputer.class);
        verify(personalComputerService).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(7L);
    }
}
