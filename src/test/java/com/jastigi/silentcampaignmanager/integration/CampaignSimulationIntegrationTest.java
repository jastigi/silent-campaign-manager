package com.jastigi.silentcampaignmanager.integration;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jastigi.silentcampaignmanager.dto.CampaignProgressResponseDTO;
import com.jastigi.silentcampaignmanager.dto.CampaignSimulationResponseDTO;
import com.jastigi.silentcampaignmanager.dto.SimulationResultDTO;
import com.jastigi.silentcampaignmanager.mapper.CampaignSimulationMapper;
import com.jastigi.silentcampaignmanager.service.campaign.simulation.CampaignSimulationService;
import com.jastigi.silentcampaignmanager.service.campaign.simulation.result.CampaignSimulationResult;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class CampaignSimulationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CampaignSimulationService campaignSimulationService;

    @MockitoBean
    private CampaignSimulationMapper campaignSimulationMapper;

    @Test
    void shouldExposeCampaignSimulationEndpoint()
            throws Exception {

        Long campaignId = 1L;

        CampaignSimulationResult result = org.mockito.Mockito.mock(
                CampaignSimulationResult.class);

        CampaignSimulationResponseDTO response = CampaignSimulationResponseDTO.builder()
                .campaignId(
                        campaignId)
                .campaignName(
                        "Integration Campaign")
                .executedAt(
                        Instant.parse(
                                "2026-08-03T18:45:00Z"))
                .progress(
                        CampaignProgressResponseDTO.builder()
                                .totalPatrols(1)
                                .completedPatrols(1)
                                .pendingPatrols(0)
                                .completionPercentage(100.0)
                                .completed(true)
                                .build())
                .patrolResults(
                        List.of(
                                SimulationResultDTO.builder()
                                        .missionOutcome(
                                                "SUCCESS")
                                        .missionScore(85)
                                        .build()))
                .build();

        when(
                campaignSimulationService
                        .simulateCampaign(
                                campaignId))
                .thenReturn(
                        result);

        when(
                campaignSimulationMapper.toDTO(
                        result))
                .thenReturn(
                        response);

        mockMvc.perform(
                post(
                        "/api/v1/campaigns/{id}/simulate",
                        campaignId))
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$.campaignId")
                                .value(1))
                .andExpect(
                        jsonPath("$.progress.completed")
                                .value(true))
                .andExpect(
                        jsonPath("$.patrolResults[0].missionScore")
                                .value(85));
    }

}
