package com.jastigi.silentcampaignmanager.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jastigi.silentcampaignmanager.dto.CampaignResponseDTO;
import com.jastigi.silentcampaignmanager.service.CampaignService;

@WebMvcTest(CampaignController.class)
class CampaignControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CampaignService campaignService;

    @Test
    void shouldReturnPagedCampaignsOnRootPath() throws Exception {
        Page<CampaignResponseDTO> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        when(campaignService.getAllCampaigns(anyInt(), anyInt(), anyString(), anyString())).thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/campaigns")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    void shouldReturnPagedCampaignsOnPagedPath() throws Exception {
        Page<CampaignResponseDTO> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        when(campaignService.getAllCampaigns(anyInt(), anyInt(), anyString(), anyString())).thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/campaigns/paged")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    void shouldHandleMethodArgumentTypeMismatchCleanly() throws Exception {
        mockMvc.perform(get("/api/v1/campaigns/not-a-number")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").value("/api/v1/campaigns/not-a-number"));
    }

    @Test
    void shouldPassSortingParametersToService() throws Exception {
        Page<CampaignResponseDTO> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        when(campaignService.getAllCampaigns(0, 10, "name", "desc")).thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/campaigns")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "name")
                .param("direction", "desc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(campaignService).getAllCampaigns(0, 10, "name", "desc");
    }
}
