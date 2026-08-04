package com.jastigi.silentcampaignmanager.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jastigi.silentcampaignmanager.dto.CampaignProgressResponseDTO;
import com.jastigi.silentcampaignmanager.dto.CampaignSimulationResponseDTO;
import com.jastigi.silentcampaignmanager.dto.SimulationResultDTO;
import com.jastigi.silentcampaignmanager.exception.CampaignNotFoundException;
import com.jastigi.silentcampaignmanager.exception.InvalidCampaignTransitionException;
import com.jastigi.silentcampaignmanager.mapper.CampaignSimulationMapper;
import com.jastigi.silentcampaignmanager.security.JwtService;
import com.jastigi.silentcampaignmanager.service.campaign.simulation.CampaignSimulationService;
import com.jastigi.silentcampaignmanager.service.campaign.simulation.result.CampaignSimulationResult;

@WebMvcTest(controllers = CampaignSimulationController.class, excludeAutoConfiguration = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
class CampaignSimulationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CampaignSimulationService campaignSimulationService;

    @MockitoBean
    private CampaignSimulationMapper campaignSimulationMapper;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void shouldSimulateCampaignSuccessfully()
            throws Exception {

        Long campaignId = 1L;

        CampaignSimulationResult result = org.mockito.Mockito.mock(
                CampaignSimulationResult.class);

        CampaignSimulationResponseDTO response = CampaignSimulationResponseDTO.builder()
                .campaignId(
                        campaignId)
                .campaignName(
                        "North Atlantic Campaign")
                .executedAt(
                        Instant.parse(
                                "2026-08-03T18:30:00Z"))
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
                                        .missionScore(90)
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
                        jsonPath("$.campaignName")
                                .value(
                                        "North Atlantic Campaign"))
                .andExpect(
                        jsonPath("$.executedAt")
                                .value(
                                        "2026-08-03T18:30:00Z"))
                .andExpect(
                        jsonPath("$.progress.totalPatrols")
                                .value(1))
                .andExpect(
                        jsonPath("$.progress.completedPatrols")
                                .value(1))
                .andExpect(
                        jsonPath("$.progress.pendingPatrols")
                                .value(0))
                .andExpect(
                        jsonPath("$.progress.completionPercentage")
                                .value(100.0))
                .andExpect(
                        jsonPath("$.progress.completed")
                                .value(true))
                .andExpect(
                        jsonPath("$.patrolResults")
                                .isArray())
                .andExpect(
                        jsonPath("$.patrolResults[0].missionOutcome")
                                .value(
                                        "SUCCESS"))
                .andExpect(
                        jsonPath("$.patrolResults[0].missionScore")
                                .value(90));

        verify(
                campaignSimulationService)
                .simulateCampaign(
                        campaignId);

        verify(
                campaignSimulationMapper)
                .toDTO(
                        result);
    }

    @Test
    void shouldReturnNotFoundWhenCampaignDoesNotExist()
            throws Exception {

        Long campaignId = 999L;

        when(
                campaignSimulationService
                        .simulateCampaign(
                                campaignId))
                .thenThrow(
                        new CampaignNotFoundException(
                                campaignId));

        mockMvc.perform(
                post(
                        "/api/v1/campaigns/{id}/simulate",
                        campaignId))
                .andExpect(
                        status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404))
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Campaign not found with id: 999"))
                .andExpect(
                        jsonPath("$.path")
                                .value(
                                        "/api/v1/campaigns/999/simulate"));
    }

    @Test
    void shouldReturnConflictWhenCampaignCannotBeSimulated()
            throws Exception {

        Long campaignId = 2L;

        when(
                campaignSimulationService
                        .simulateCampaign(
                                campaignId))
                .thenThrow(
                        new InvalidCampaignTransitionException(
                                "Campaign cannot be simulated because its status is FINISHED"));

        mockMvc.perform(
                post(
                        "/api/v1/campaigns/{id}/simulate",
                        campaignId))
                .andExpect(
                        status().isConflict())
                .andExpect(
                        jsonPath("$.status")
                                .value(409))
                .andExpect(
                        jsonPath("$.error")
                                .value(
                                        "Conflict"))
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Campaign cannot be simulated because its status is FINISHED"))
                .andExpect(
                        jsonPath("$.path")
                                .value(
                                        "/api/v1/campaigns/2/simulate"));
    }

}
