package com.jastigi.silentcampaignmanager.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jastigi.silentcampaignmanager.dto.CampaignTimelineEventDTO;
import com.jastigi.silentcampaignmanager.dto.CampaignTimelineEventType;
import com.jastigi.silentcampaignmanager.exception.CampaignNotFoundException;
import com.jastigi.silentcampaignmanager.security.JwtService;
import com.jastigi.silentcampaignmanager.service.campaign.timeline.CampaignTimelineService;

@WebMvcTest(controllers = CampaignTimelineController.class, excludeAutoConfiguration = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
class CampaignTimelineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CampaignTimelineService campaignTimelineService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void shouldReturnCampaignTimeline()
            throws Exception {

        Long campaignId = 1L;

        List<CampaignTimelineEventDTO> timeline = List.of(
                CampaignTimelineEventDTO.builder()
                        .timestamp(
                                LocalDateTime.of(
                                        2026,
                                        8,
                                        5,
                                        10,
                                        0))
                        .type(
                                CampaignTimelineEventType.CAMPAIGN_EXECUTION_STARTED)
                        .description(
                                "Campaign execution started")
                        .build(),
                CampaignTimelineEventDTO.builder()
                        .timestamp(
                                LocalDateTime.of(
                                        2026,
                                        8,
                                        5,
                                        10,
                                        5))
                        .type(
                                CampaignTimelineEventType.PATROL_COMPLETED)
                        .description(
                                "Patrol completed: North Atlantic Patrol")
                        .build(),
                CampaignTimelineEventDTO.builder()
                        .timestamp(
                                LocalDateTime.of(
                                        2026,
                                        8,
                                        5,
                                        10,
                                        10))
                        .type(
                                CampaignTimelineEventType.CAMPAIGN_EXECUTION_COMPLETED)
                        .description(
                                "Campaign execution completed")
                        .build());

        when(
                campaignTimelineService.getTimeline(
                        campaignId))
                .thenReturn(
                        timeline);

        mockMvc.perform(
                get(
                        "/api/v1/campaigns/{campaignId}/timeline",
                        campaignId))
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$")
                                .isArray())
                .andExpect(
                        jsonPath("$.length()")
                                .value(3))
                .andExpect(
                        jsonPath("$[0].timestamp")
                                .value(
                                        "2026-08-05T10:00:00"))
                .andExpect(
                        jsonPath("$[0].type")
                                .value(
                                        "CAMPAIGN_EXECUTION_STARTED"))
                .andExpect(
                        jsonPath("$[0].description")
                                .value(
                                        "Campaign execution started"))
                .andExpect(
                        jsonPath("$[1].timestamp")
                                .value(
                                        "2026-08-05T10:05:00"))
                .andExpect(
                        jsonPath("$[1].type")
                                .value(
                                        "PATROL_COMPLETED"))
                .andExpect(
                        jsonPath("$[1].description")
                                .value(
                                        "Patrol completed: North Atlantic Patrol"))
                .andExpect(
                        jsonPath("$[2].type")
                                .value(
                                        "CAMPAIGN_EXECUTION_COMPLETED"))
                .andExpect(
                        jsonPath("$[2].description")
                                .value(
                                        "Campaign execution completed"));

        verify(
                campaignTimelineService)
                .getTimeline(
                        campaignId);
    }

    @Test
    void shouldReturnEmptyTimeline()
            throws Exception {

        Long campaignId = 2L;

        when(
                campaignTimelineService.getTimeline(
                        campaignId))
                .thenReturn(
                        List.of());

        mockMvc.perform(
                get(
                        "/api/v1/campaigns/{campaignId}/timeline",
                        campaignId))
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$")
                                .isArray())
                .andExpect(
                        jsonPath("$.length()")
                                .value(0));

        verify(
                campaignTimelineService)
                .getTimeline(
                        campaignId);
    }

    @Test
    void shouldReturnNotFoundWhenCampaignDoesNotExist()
            throws Exception {

        Long campaignId = 999L;

        when(
                campaignTimelineService.getTimeline(
                        campaignId))
                .thenThrow(
                        new CampaignNotFoundException(
                                campaignId));

        mockMvc.perform(
                get(
                        "/api/v1/campaigns/{campaignId}/timeline",
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
                                        "/api/v1/campaigns/999/timeline"));

        verify(
                campaignTimelineService)
                .getTimeline(
                        campaignId);
    }

}
