package com.jastigi.silentcampaignmanager.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jastigi.silentcampaignmanager.exception.CampaignNotFoundException;
import com.jastigi.silentcampaignmanager.exception.InvalidCampaignTransitionException;
import com.jastigi.silentcampaignmanager.security.JwtService;
import com.jastigi.silentcampaignmanager.service.campaign.patrol.CampaignPatrolGenerationService;

@WebMvcTest(controllers = CampaignPatrolGenerationController.class, excludeAutoConfiguration = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
class CampaignPatrolGenerationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CampaignPatrolGenerationService campaignPatrolGenerationService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void shouldGenerateCampaignPatrols()
            throws Exception {

        Long campaignId = 1L;

        when(
                campaignPatrolGenerationService
                        .generatePatrols(
                                campaignId))
                .thenReturn(
                        3);

        mockMvc.perform(
                post(
                        "/api/v1/campaigns/{campaignId}/generate-patrols",
                        campaignId))
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$.generatedPatrols")
                                .value(3));

        verify(
                campaignPatrolGenerationService)
                .generatePatrols(
                        campaignId);
    }

    @Test
    void shouldReturnZeroWhenNoPatrolsCanBeGenerated()
            throws Exception {

        Long campaignId = 2L;

        when(
                campaignPatrolGenerationService
                        .generatePatrols(
                                campaignId))
                .thenReturn(
                        0);

        mockMvc.perform(
                post(
                        "/api/v1/campaigns/{campaignId}/generate-patrols",
                        campaignId))
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$.generatedPatrols")
                                .value(0));

        verify(
                campaignPatrolGenerationService)
                .generatePatrols(
                        campaignId);
    }

    @Test
    void shouldReturnNotFoundWhenCampaignDoesNotExist()
            throws Exception {

        Long campaignId = 999L;

        when(
                campaignPatrolGenerationService
                        .generatePatrols(
                                campaignId))
                .thenThrow(
                        new CampaignNotFoundException(
                                campaignId));

        mockMvc.perform(
                post(
                        "/api/v1/campaigns/{campaignId}/generate-patrols",
                        campaignId))
                .andExpect(
                        status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404))
                .andExpect(
                        jsonPath("$.error")
                                .value(
                                        "Not Found"))
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Campaign not found with id: 999"))
                .andExpect(
                        jsonPath("$.path")
                                .value(
                                        "/api/v1/campaigns/999/generate-patrols"));

        verify(
                campaignPatrolGenerationService)
                .generatePatrols(
                        campaignId);
    }

    @Test
    void shouldReturnConflictWhenCampaignStatusDoesNotAllowGeneration()
            throws Exception {

        Long campaignId = 3L;

        when(
                campaignPatrolGenerationService
                        .generatePatrols(
                                campaignId))
                .thenThrow(
                        new InvalidCampaignTransitionException(
                                "Patrols cannot be generated because campaign status is FINISHED"));

        mockMvc.perform(
                post(
                        "/api/v1/campaigns/{campaignId}/generate-patrols",
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
                                        "Patrols cannot be generated because campaign status is FINISHED"))
                .andExpect(
                        jsonPath("$.path")
                                .value(
                                        "/api/v1/campaigns/3/generate-patrols"));

        verify(
                campaignPatrolGenerationService)
                .generatePatrols(
                        campaignId);
    }

}
