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
import testtask.shift.shopapi.model.hdd.HardDrive;
import testtask.shift.shopapi.service.HardDriveService;

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

@WebMvcTest(HardDriveController.class)
class HardDriveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HardDriveService hardDriveService;

    @Test
    void getHardDrives_returnsList() throws Exception {
        List<HardDrive> hardDrives = List.of(
                new HardDrive(1L, "HD-1", "Seagate", new BigDecimal("129.99"), 10L, 512.0),
                new HardDrive(2L, "HD-2", "WD", new BigDecimal("159.99"), 4L, 1024.0)
        );
        when(hardDriveService.getAllHardDrives()).thenReturn(hardDrives);

        mockMvc.perform(get("/api/hdds"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].seriesNumber").value("HD-1"))
                .andExpect(jsonPath("$[0].capacity").value(512.0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].producer").value("WD"));
    }

    @Test
    void getHardDrive_returnsEntity() throws Exception {
        HardDrive hardDrive = new HardDrive(5L, "HD-5", "Seagate", new BigDecimal("199.99"), 2L, 2048.0);
        when(hardDriveService.getHardDrive(5L)).thenReturn(hardDrive);

        mockMvc.perform(get("/api/hdds/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.seriesNumber").value("HD-5"))
                .andExpect(jsonPath("$.capacity").value(2048.0));
    }

    @Test
    void getHardDrive_returnsNotFound() throws Exception {
        when(hardDriveService.getHardDrive(99L)).thenThrow(new ResourceNotFoundException("HardDrive not found"));

        mockMvc.perform(get("/api/hdds/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createHardDrive_delegatesToService() throws Exception {
        HardDrive payload = new HardDrive("HD-9", "Toshiba", new BigDecimal("89.99"), 8L, 256.0);
        HardDrive saved = new HardDrive(9L, payload.getSeriesNumber(), payload.getProducer(),
                payload.getPrice(), payload.getNumberOfProductsInStock(), payload.getCapacity());
        when(hardDriveService.save(any(HardDrive.class))).thenReturn(saved);

        mockMvc.perform(post("/api/hdds/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(9))
                .andExpect(jsonPath("$.producer").value("Toshiba"));
    }

    @Test
    void editHardDrive_setsIdFromPath() throws Exception {
        HardDrive existing = new HardDrive(7L, "HD-7", "Seagate", new BigDecimal("129.99"), 3L, 512.0);
        HardDrive payload = new HardDrive(999L, "HD-7A", "Seagate", new BigDecimal("139.99"), 3L, 768.0);
        when(hardDriveService.getHardDrive(7L)).thenReturn(existing);
        when(hardDriveService.save(any(HardDrive.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/api/hdds/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.capacity").value(768.0));

        ArgumentCaptor<HardDrive> captor = ArgumentCaptor.forClass(HardDrive.class);
        verify(hardDriveService).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(7L);
    }
}
