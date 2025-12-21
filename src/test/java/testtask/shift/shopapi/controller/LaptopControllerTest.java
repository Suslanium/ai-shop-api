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
import testtask.shift.shopapi.model.laptop.Laptop;
import testtask.shift.shopapi.model.laptop.LaptopSize;
import testtask.shift.shopapi.service.LaptopService;

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

@WebMvcTest(LaptopController.class)
class LaptopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LaptopService laptopService;

    @Test
    void getLaptops_returnsList() throws Exception {
        List<Laptop> laptops = List.of(
                new Laptop(1L, "LP-1", "Lenovo", new BigDecimal("999.99"), 4L, LaptopSize.Inch13),
                new Laptop(2L, "LP-2", "Dell", new BigDecimal("1299.00"), 2L, LaptopSize.Inch15)
        );
        when(laptopService.getAllLaptops()).thenReturn(laptops);

        mockMvc.perform(get("/api/laptops"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].size").value("13 inches"))
                .andExpect(jsonPath("$[1].producer").value("Dell"));
    }

    @Test
    void getLaptop_returnsEntity() throws Exception {
        Laptop laptop = new Laptop(5L, "LP-5", "HP", new BigDecimal("899.00"), 1L, LaptopSize.Inch14);
        when(laptopService.getLaptop(5L)).thenReturn(laptop);

        mockMvc.perform(get("/api/laptops/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.seriesNumber").value("LP-5"))
                .andExpect(jsonPath("$.size").value("14 inches"));
    }

    @Test
    void getLaptop_returnsNotFound() throws Exception {
        when(laptopService.getLaptop(99L)).thenThrow(new ResourceNotFoundException("Laptop not found"));

        mockMvc.perform(get("/api/laptops/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createLaptop_delegatesToService() throws Exception {
        Laptop payload = new Laptop("LP-9", "Asus", new BigDecimal("1199.00"), 6L, LaptopSize.Inch17);
        Laptop saved = new Laptop(9L, payload.getSeriesNumber(), payload.getProducer(),
                payload.getPrice(), payload.getNumberOfProductsInStock(), payload.getSize());
        when(laptopService.save(any(Laptop.class))).thenReturn(saved);

        mockMvc.perform(post("/api/laptops/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(9))
                .andExpect(jsonPath("$.size").value("17 inches"));
    }

    @Test
    void editLaptop_setsIdFromPath() throws Exception {
        Laptop existing = new Laptop(7L, "LP-7", "Lenovo", new BigDecimal("999.99"), 2L, LaptopSize.Inch13);
        Laptop payload = new Laptop(999L, "LP-7A", "Lenovo", new BigDecimal("1099.99"), 2L, LaptopSize.Inch15);
        when(laptopService.getLaptop(7L)).thenReturn(existing);
        when(laptopService.save(any(Laptop.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/api/laptops/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.size").value("15 inches"));

        ArgumentCaptor<Laptop> captor = ArgumentCaptor.forClass(Laptop.class);
        verify(laptopService).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(7L);
    }
}
