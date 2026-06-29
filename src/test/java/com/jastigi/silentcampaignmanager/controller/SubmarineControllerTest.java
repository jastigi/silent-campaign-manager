package com.jastigi.silentcampaignmanager.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jastigi.silentcampaignmanager.dto.SubmarineRequestDTO;
import com.jastigi.silentcampaignmanager.dto.SubmarineResponseDTO;
import com.jastigi.silentcampaignmanager.entity.SubmarineClass;
import com.jastigi.silentcampaignmanager.entity.SubmarineRole;
import com.jastigi.silentcampaignmanager.entity.SubmarineStatus;
import com.jastigi.silentcampaignmanager.entity.SubmarineType;
import com.jastigi.silentcampaignmanager.security.JwtService;
import com.jastigi.silentcampaignmanager.service.SubmarineService;

@WebMvcTest(controllers = SubmarineController.class, excludeAutoConfiguration = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
class SubmarineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SubmarineService submarineService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void shouldCreateSubmarineSuccessfully() throws Exception {

        SubmarineResponseDTO response = new SubmarineResponseDTO();
        response.setId(1L);
        response.setName("USS Dallas");
        response.setType(SubmarineType.SSN);
        response.setSubmarineClass(SubmarineClass.LOS_ANGELES);
        response.setSubmarineRole(SubmarineRole.SSN);
        response.setNation("USA");
        response.setStatus(SubmarineStatus.ACTIVE);

        when(submarineService.createSubmarine(
                any(SubmarineRequestDTO.class)))
                .thenReturn(response);

        String requestJson = """
                {
                    "name": "USS Dallas",
                    "type": "SSN",
                    "submarineClass": "LOS_ANGELES",
                    "submarineRole": "SSN",
                    "nation": "USA",
                    "status": "ACTIVE"
                }
                """;

        mockMvc.perform(post("/api/v1/submarines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("USS Dallas"))
                .andExpect(jsonPath("$.type").value("SSN"))
                .andExpect(jsonPath("$.submarineClass")
                        .value("LOS_ANGELES"))
                .andExpect(jsonPath("$.nation").value("USA"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldReturnAllSubmarines() throws Exception {

        SubmarineResponseDTO submarine = new SubmarineResponseDTO();
        submarine.setId(1L);
        submarine.setName("USS Dallas");
        submarine.setType(SubmarineType.SSN);
        submarine.setSubmarineClass(SubmarineClass.LOS_ANGELES);
        submarine.setSubmarineRole(SubmarineRole.SSN);
        submarine.setNation("USA");
        submarine.setStatus(SubmarineStatus.ACTIVE);

        when(submarineService.getAllSubmarines())
                .thenReturn(List.of(submarine));

        mockMvc.perform(get("/api/v1/submarines")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name")
                        .value("USS Dallas"))
                .andExpect(jsonPath("$[0].type").value("SSN"))
                .andExpect(jsonPath("$[0].submarineClass")
                        .value("LOS_ANGELES"))
                .andExpect(jsonPath("$[0].nation").value("USA"))
                .andExpect(jsonPath("$[0].status")
                        .value("ACTIVE"));
    }

    @Test
    void shouldReturnEmptyListWhenNoSubmarines()
            throws Exception {

        when(submarineService.getAllSubmarines())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/submarines")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldReturnSubmarineById() throws Exception {

        SubmarineResponseDTO response = new SubmarineResponseDTO();
        response.setId(1L);
        response.setName("USS Dallas");
        response.setType(SubmarineType.SSN);
        response.setSubmarineClass(SubmarineClass.LOS_ANGELES);
        response.setSubmarineRole(SubmarineRole.SSN);
        response.setNation("USA");
        response.setStatus(SubmarineStatus.ACTIVE);

        when(submarineService.getSubmarineById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/submarines/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("USS Dallas"));
    }

    @Test
    void shouldHandleTypeMismatchOnGetById() throws Exception {

        mockMvc.perform(get("/api/v1/submarines/not-a-number")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/submarines/not-a-number"));
    }

    @Test
    void shouldUpdateSubmarineSuccessfully() throws Exception {

        SubmarineResponseDTO response = new SubmarineResponseDTO();
        response.setId(1L);
        response.setName("USS Dallas Updated");
        response.setType(SubmarineType.SSN);
        response.setSubmarineClass(SubmarineClass.LOS_ANGELES);
        response.setSubmarineRole(SubmarineRole.SSN);
        response.setNation("USA");
        response.setStatus(SubmarineStatus.REFIT);

        when(submarineService.updateSubmarine(
                eq(1L), any(SubmarineRequestDTO.class)))
                .thenReturn(response);

        String requestJson = """
                {
                    "name": "USS Dallas Updated",
                    "type": "SSN",
                    "submarineClass": "LOS_ANGELES",
                    "submarineRole": "SSN",
                    "nation": "USA",
                    "status": "REFIT"
                }
                """;

        mockMvc.perform(put("/api/v1/submarines/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name")
                        .value("USS Dallas Updated"))
                .andExpect(jsonPath("$.status").value("REFIT"));
    }

    @Test
    void shouldDeleteSubmarineSuccessfully() throws Exception {

        doNothing().when(submarineService)
                .deleteSubmarine(1L);

        mockMvc.perform(delete("/api/v1/submarines/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
