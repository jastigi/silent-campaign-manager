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
class CampaignExecutionHistoryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CampaignExecutionRepository campaignExecutionRepository;

    @Test
    void shouldReturnPersistedCampaignExecutionHistory()
            throws Exception {

        Campaign campaign = persistCampaign();

        persistExecution(
                campaign,
                CampaignExecutionStatus.FAILED,
                LocalDateTime.of(
                        2026,
                        8,
                        4,
                        10,
                        0),
                1,
                "Second patrol failed");

        persistExecution(
                campaign,
                CampaignExecutionStatus.COMPLETED,
                LocalDateTime.of(
                        2026,
                        8,
                        4,
                        12,
                        0),
                3,
                null);

        mockMvc.perform(
                get(
                        "/api/v1/campaigns/{campaignId}/executions",
                        campaign.getId()))
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$.totalElements")
                                .value(2))
                .andExpect(
                        jsonPath("$.content[0].status")
                                .value(
                                        "COMPLETED"))
                .andExpect(
                        jsonPath("$.content[0].completedPatrols")
                                .value(3))
                .andExpect(
                        jsonPath("$.content[1].status")
                                .value(
                                        "FAILED"))
                .andExpect(
                        jsonPath("$.content[1].completedPatrols")
                                .value(1))
                .andExpect(
                        jsonPath("$.content[1].failureMessage")
                                .value(
                                        "Second patrol failed"));
    }

    @Test
    void shouldReturnEmptyHistoryForCampaignWithoutExecutions()
            throws Exception {

        Campaign campaign = persistCampaign();

        mockMvc.perform(
                get(
                        "/api/v1/campaigns/{campaignId}/executions",
                        campaign.getId()))
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$.totalElements")
                                .value(0))
                .andExpect(
                        jsonPath("$.content")
                                .isEmpty());
    }

    @Test
    void shouldReturnNotFoundForMissingCampaign()
            throws Exception {

        mockMvc.perform(
                get(
                        "/api/v1/campaigns/999999/executions"))
                .andExpect(
                        status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404));
    }

    private Campaign persistCampaign() {

        Campaign campaign = new Campaign();

        campaign.setName(
                "Execution History Campaign");

        campaign.setDescription(
                "Campaign used by execution history integration tests.");

        campaign.setStartDate(
                LocalDate.of(
                        1985,
                        1,
                        1));

        campaign.setStatus(
                CampaignStatus.ACTIVE);

        return campaignRepository.saveAndFlush(
                campaign);
    }

    private CampaignExecution persistExecution(
            Campaign campaign,
            CampaignExecutionStatus status,
            LocalDateTime startedAt,
            int completedPatrols,
            String failureMessage) {

        CampaignExecution execution = CampaignExecution.builder()
                .campaign(
                        campaign)
                .status(
                        status)
                .totalPatrols(3)
                .completedPatrols(
                        completedPatrols)
                .startedAt(
                        startedAt)
                .completedAt(
                        startedAt.plusMinutes(
                                5))
                .failureMessage(
                        failureMessage)
                .build();

        return campaignExecutionRepository
                .saveAndFlush(
                        execution);
    }

}
