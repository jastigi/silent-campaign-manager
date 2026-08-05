package com.jastigi.silentcampaignmanager.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.CampaignStatus;
import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.Submarine;
import com.jastigi.silentcampaignmanager.entity.SubmarineClass;
import com.jastigi.silentcampaignmanager.entity.SubmarineStatus;
import com.jastigi.silentcampaignmanager.entity.SubmarineType;
import com.jastigi.silentcampaignmanager.repository.CampaignRepository;
import com.jastigi.silentcampaignmanager.repository.PatrolRepository;
import com.jastigi.silentcampaignmanager.repository.SubmarineRepository;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class CampaignPatrolGenerationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private SubmarineRepository submarineRepository;

    @Autowired
    private PatrolRepository patrolRepository;

    @Test
    void shouldGeneratePatrolsOnlyForActiveSubmarines()
            throws Exception {

        Campaign campaign = persistCampaign(
                CampaignStatus.ACTIVE);

        Submarine activeOhio = persistSubmarine(
                "USS Ohio",
                SubmarineStatus.ACTIVE);

        Submarine activeMichigan = persistSubmarine(
                "USS Michigan",
                SubmarineStatus.ACTIVE);

        persistSubmarine(
                "USS Florida",
                SubmarineStatus.DAMAGED);

        persistSubmarine(
                "USS Georgia",
                SubmarineStatus.REFIT);

        mockMvc.perform(
                post(
                        "/api/v1/campaigns/{campaignId}/generate-patrols",
                        campaign.getId()))
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$.generatedPatrols")
                                .value(2));

        List<Patrol> patrols = patrolRepository.findByCampaignId(
                campaign.getId());

        assertEquals(
                2,
                patrols.size());

        Patrol ohioPatrol = findPatrolForSubmarine(
                patrols,
                activeOhio.getId());

        assertEquals(
                "USS Ohio Patrol",
                ohioPatrol.getPatrolName());

        assertEquals(
                campaign.getStartDate(),
                ohioPatrol.getPatrolDate());

        assertEquals(
                "North Atlantic",
                ohioPatrol.getArea());

        assertEquals(
                MissionType.DETERRENCE_PATROL,
                ohioPatrol.getMissionType());

        assertEquals(
                campaign.getId(),
                ohioPatrol.getCampaign()
                        .getId());

        assertEquals(
                activeOhio.getId(),
                ohioPatrol.getSubmarine()
                        .getId());

        Patrol michiganPatrol = findPatrolForSubmarine(
                patrols,
                activeMichigan.getId());

        assertEquals(
                "USS Michigan Patrol",
                michiganPatrol.getPatrolName());
    }

    @Test
    void shouldNotCreateDuplicatePatrolsWhenEndpointIsCalledAgain()
            throws Exception {

        Campaign campaign = persistCampaign(
                CampaignStatus.ACTIVE);

        persistSubmarine(
                "USS Ohio",
                SubmarineStatus.ACTIVE);

        mockMvc.perform(
                post(
                        "/api/v1/campaigns/{campaignId}/generate-patrols",
                        campaign.getId()))
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$.generatedPatrols")
                                .value(1));

        mockMvc.perform(
                post(
                        "/api/v1/campaigns/{campaignId}/generate-patrols",
                        campaign.getId()))
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$.generatedPatrols")
                                .value(0));

        assertEquals(
                1,
                patrolRepository.countByCampaignId(
                        campaign.getId()));
    }

    @Test
    void shouldReturnConflictForFinishedCampaign()
            throws Exception {

        Campaign campaign = persistCampaign(
                CampaignStatus.FINISHED);

        persistSubmarine(
                "USS Ohio",
                SubmarineStatus.ACTIVE);

        mockMvc.perform(
                post(
                        "/api/v1/campaigns/{campaignId}/generate-patrols",
                        campaign.getId()))
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
                                        "Patrols cannot be generated because campaign status is FINISHED"));

        assertEquals(
                0,
                patrolRepository.countByCampaignId(
                        campaign.getId()));
    }

    @Test
    void shouldReturnNotFoundForMissingCampaign()
            throws Exception {

        mockMvc.perform(
                post(
                        "/api/v1/campaigns/999999/generate-patrols"))
                .andExpect(
                        status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404))
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Campaign not found with id: 999999"));
    }

    private Campaign persistCampaign(
            CampaignStatus status) {

        Campaign campaign = new Campaign();

        campaign.setName(
                "Dynamic Patrol Campaign");

        campaign.setDescription(
                "Campaign used by patrol generation integration tests.");

        campaign.setStartDate(
                LocalDate.of(
                        1985,
                        1,
                        1));

        campaign.setStatus(
                status);

        return campaignRepository.saveAndFlush(
                campaign);
    }

    private Submarine persistSubmarine(
            String name,
            SubmarineStatus status) {

        Submarine submarine = new Submarine();

        submarine.setName(
                name);

        submarine.setSubmarineType(
                SubmarineType.SSBN);

        submarine.setSubmarineClass(
                SubmarineClass.OHIO);

        submarine.setNation(
                "USA");

        submarine.setStatus(
                status);

        return submarineRepository.saveAndFlush(
                submarine);
    }

    private Patrol findPatrolForSubmarine(
            List<Patrol> patrols,
            Long submarineId) {

        return patrols.stream()
                .filter(
                        patrol -> patrol.getSubmarine() != null
                                && submarineId.equals(
                                        patrol.getSubmarine()
                                                .getId()))
                .findFirst()
                .orElseThrow();
    }

}
