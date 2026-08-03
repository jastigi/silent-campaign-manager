package com.jastigi.silentcampaignmanager.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jastigi.silentcampaignmanager.dto.CampaignResponseDTO;
import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.CampaignStatus;
import com.jastigi.silentcampaignmanager.exception.InvalidCampaignTransitionException;
import com.jastigi.silentcampaignmanager.security.JwtService;
import com.jastigi.silentcampaignmanager.service.CampaignService;
import com.jastigi.silentcampaignmanager.service.campaign.lifecycle.CampaignLifecycleService;
import com.jastigi.silentcampaignmanager.service.campaign.statistics.CampaignStatistics;

@WebMvcTest(controllers = CampaignController.class, excludeAutoConfiguration = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
class CampaignControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private CampaignService campaignService;

        @MockitoBean
        private JwtService jwtService;

        @MockitoBean
        private CampaignLifecycleService campaignLifecycleService;

        @Test
        void shouldReturnPagedCampaignsOnRootPath() throws Exception {
                Page<CampaignResponseDTO> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
                when(campaignService.getAllCampaigns(anyInt(), anyInt(), anyString(), anyString()))
                                .thenReturn(emptyPage);

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
                when(campaignService.getCampaigns(any(Pageable.class))).thenReturn(emptyPage);

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

        @Test
        void shouldReturnCampaignStatistics() throws Exception {

                Long campaignId = 1L;

                CampaignStatistics statistics = CampaignStatistics.builder()
                                .totalPatrols(5)
                                .completedPatrols(3)
                                .pendingPatrols(2)
                                .completionPercentage(60.0)
                                .completed(false)
                                .totalSimulations(4)
                                .successfulSimulations(2)
                                .partialSuccessfulSimulations(1)
                                .failedSimulations(1)
                                .successRate(50.0)
                                .averageMissionScore(65.0)
                                .totalContactsDetected(6)
                                .totalContactsLost(1)
                                .totalIntelligenceGathered(4)
                                .totalIncidents(3)
                                .build();

                when(
                                campaignService.getStatistics(
                                                campaignId))
                                .thenReturn(
                                                statistics);

                mockMvc.perform(
                                get(
                                                "/api/v1/campaigns/{id}/statistics",
                                                campaignId)
                                                .contentType(
                                                                MediaType.APPLICATION_JSON))
                                .andExpect(
                                                status().isOk())
                                .andExpect(
                                                jsonPath("$.totalPatrols")
                                                                .value(5))
                                .andExpect(
                                                jsonPath("$.completedPatrols")
                                                                .value(3))
                                .andExpect(
                                                jsonPath("$.pendingPatrols")
                                                                .value(2))
                                .andExpect(
                                                jsonPath("$.completionPercentage")
                                                                .value(60.0))
                                .andExpect(
                                                jsonPath("$.completed")
                                                                .value(false))
                                .andExpect(
                                                jsonPath("$.totalSimulations")
                                                                .value(4))
                                .andExpect(
                                                jsonPath("$.successfulSimulations")
                                                                .value(2))
                                .andExpect(
                                                jsonPath("$.partialSuccessfulSimulations")
                                                                .value(1))
                                .andExpect(
                                                jsonPath("$.failedSimulations")
                                                                .value(1))
                                .andExpect(
                                                jsonPath("$.successRate")
                                                                .value(50.0))
                                .andExpect(
                                                jsonPath("$.averageMissionScore")
                                                                .value(65.0))
                                .andExpect(
                                                jsonPath("$.totalContactsDetected")
                                                                .value(6))
                                .andExpect(
                                                jsonPath("$.totalContactsLost")
                                                                .value(1))
                                .andExpect(
                                                jsonPath("$.totalIntelligenceGathered")
                                                                .value(4))
                                .andExpect(
                                                jsonPath("$.totalIncidents")
                                                                .value(3));

                verify(
                                campaignService)
                                .getStatistics(
                                                campaignId);
        }

        @Test
        void shouldFinishCampaign() throws Exception {

                Long campaignId = 1L;

                Campaign campaign = campaign(
                                campaignId,
                                CampaignStatus.FINISHED);

                when(
                                campaignLifecycleService.finishCampaign(
                                                campaignId))
                                .thenReturn(
                                                campaign);

                mockMvc.perform(
                                patch(
                                                "/api/v1/campaigns/{id}/finish",
                                                campaignId))
                                .andExpect(
                                                status().isOk())
                                .andExpect(
                                                jsonPath("$.id")
                                                                .value(1))
                                .andExpect(
                                                jsonPath("$.name")
                                                                .value(
                                                                                "Campaign 1"))
                                .andExpect(
                                                jsonPath("$.status")
                                                                .value(
                                                                                "FINISHED"));

                verify(
                                campaignLifecycleService)
                                .finishCampaign(
                                                campaignId);
        }

        @Test
        void shouldAbandonCampaign() throws Exception {

                Long campaignId = 2L;

                Campaign campaign = campaign(
                                campaignId,
                                CampaignStatus.ABANDONED);

                when(
                                campaignLifecycleService.abandonCampaign(
                                                campaignId))
                                .thenReturn(
                                                campaign);

                mockMvc.perform(
                                patch(
                                                "/api/v1/campaigns/{id}/abandon",
                                                campaignId))
                                .andExpect(
                                                status().isOk())
                                .andExpect(
                                                jsonPath("$.id")
                                                                .value(2))
                                .andExpect(
                                                jsonPath("$.status")
                                                                .value(
                                                                                "ABANDONED"));

                verify(
                                campaignLifecycleService)
                                .abandonCampaign(
                                                campaignId);
        }

        @Test
        void shouldReturnConflictForInvalidLifecycleTransition()
                        throws Exception {

                Long campaignId = 3L;

                when(
                                campaignLifecycleService.finishCampaign(
                                                campaignId))
                                .thenThrow(
                                                new InvalidCampaignTransitionException(
                                                                CampaignStatus.FINISHED,
                                                                CampaignStatus.FINISHED));

                mockMvc.perform(
                                patch(
                                                "/api/v1/campaigns/{id}/finish",
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
                                                                                "Invalid campaign transition from FINISHED to FINISHED"))
                                .andExpect(
                                                jsonPath("$.path")
                                                                .value(
                                                                                "/api/v1/campaigns/3/finish"));
        }

        private Campaign campaign(
                        Long campaignId,
                        CampaignStatus status) {

                Campaign campaign = new Campaign();

                campaign.setId(
                                campaignId);

                campaign.setName(
                                "Campaign " + campaignId);

                campaign.setDescription(
                                "Lifecycle test campaign");

                campaign.setStartDate(
                                LocalDate.of(
                                                1985,
                                                1,
                                                1));

                campaign.setStatus(
                                status);

                return campaign;
        }

}
