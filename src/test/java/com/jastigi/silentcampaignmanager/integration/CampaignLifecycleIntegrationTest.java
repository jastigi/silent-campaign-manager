package com.jastigi.silentcampaignmanager.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.CampaignStatus;
import com.jastigi.silentcampaignmanager.repository.CampaignRepository;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class CampaignLifecycleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CampaignRepository campaignRepository;

    private Campaign activeCampaign;

    @BeforeEach
    void setUp() {

        activeCampaign = new Campaign();

        activeCampaign.setName(
                "Lifecycle Integration Campaign");

        activeCampaign.setDescription(
                "Campaign used by lifecycle integration tests.");

        activeCampaign.setStartDate(
                LocalDate.of(
                        1985,
                        1,
                        1));

        activeCampaign.setStatus(
                CampaignStatus.ACTIVE);

        activeCampaign = campaignRepository.saveAndFlush(
                activeCampaign);
    }

    @Test
    void shouldAbandonActiveCampaign()
            throws Exception {

        mockMvc.perform(
                patch(
                        "/api/v1/campaigns/{id}/abandon",
                        activeCampaign.getId()))
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$.id")
                                .value(
                                        activeCampaign.getId()))
                .andExpect(
                        jsonPath("$.status")
                                .value(
                                        "ABANDONED"));

        Campaign persistedCampaign = campaignRepository.findById(
                activeCampaign.getId())
                .orElseThrow();

        org.junit.jupiter.api.Assertions.assertEquals(
                CampaignStatus.ABANDONED,
                persistedCampaign.getStatus());
    }

    @Test
    void shouldReturnConflictWhenAbandoningTerminalCampaign()
            throws Exception {

        activeCampaign.setStatus(
                CampaignStatus.FINISHED);

        campaignRepository.saveAndFlush(
                activeCampaign);

        mockMvc.perform(
                patch(
                        "/api/v1/campaigns/{id}/abandon",
                        activeCampaign.getId()))
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
                                        "Invalid campaign transition from FINISHED to ABANDONED"));
    }

    @Test
    void shouldReturnConflictWhenFinishingCampaignWithoutPatrols()
            throws Exception {

        mockMvc.perform(
                patch(
                        "/api/v1/campaigns/{id}/finish",
                        activeCampaign.getId()))
                .andExpect(
                        status().isConflict())
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Campaign cannot be finished because it has no patrols"));
    }

    @Test
    void shouldReturnNotFoundForMissingCampaign()
            throws Exception {

        mockMvc.perform(
                patch(
                        "/api/v1/campaigns/999999/abandon"))
                .andExpect(
                        status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404));
    }

}
