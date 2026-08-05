package com.jastigi.silentcampaignmanager.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.CampaignExecution;
import com.jastigi.silentcampaignmanager.entity.CampaignExecutionStatus;
import com.jastigi.silentcampaignmanager.entity.CampaignStatus;
import com.jastigi.silentcampaignmanager.repository.CampaignExecutionRepository;
import com.jastigi.silentcampaignmanager.repository.CampaignRepository;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class CampaignTimelineIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CampaignExecutionRepository campaignExecutionRepository;

    @Test
    void shouldReturnCampaignTimeline()
            throws Exception {

        Campaign campaign = new Campaign();

        campaign.setName("Timeline Campaign");
        campaign.setDescription("Integration test campaign");
        campaign.setStartDate(LocalDate.of(1985, 1, 1));
        campaign.setStatus(CampaignStatus.ACTIVE);

        campaign = campaignRepository.saveAndFlush(campaign);

        CampaignExecution execution = CampaignExecution.builder()
                .campaign(campaign)
                .status(CampaignExecutionStatus.COMPLETED)
                .totalPatrols(1)
                .completedPatrols(1)
                .startedAt(LocalDateTime.of(2026, 8, 5, 10, 0))
                .completedAt(LocalDateTime.of(2026, 8, 5, 10, 5))
                .build();

        campaignExecutionRepository.saveAndFlush(execution);

        mockMvc.perform(
                get("/api/v1/campaigns/{id}/timeline",
                        campaign.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].type")
                        .value("CAMPAIGN_EXECUTION_STARTED"))
                .andExpect(jsonPath("$[1].type")
                        .value("CAMPAIGN_EXECUTION_COMPLETED"));
    }

    @Test
    void shouldReturnNotFoundForUnknownCampaign()
            throws Exception {

        mockMvc.perform(
                get("/api/v1/campaigns/999999/timeline"))
                .andExpect(status().isNotFound());
    }

}
