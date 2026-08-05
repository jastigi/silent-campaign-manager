package com.jastigi.silentcampaignmanager.service.campaign.patrol.generator.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.CampaignStatus;
import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.Submarine;
import com.jastigi.silentcampaignmanager.entity.SubmarineStatus;
import com.jastigi.silentcampaignmanager.service.campaign.patrol.generator.PatrolGenerator;

class PatrolGeneratorImplTest {

    private PatrolGenerator patrolGenerator;

    @BeforeEach
    void setUp() {

        patrolGenerator = new PatrolGeneratorImpl();
    }

    @Test
    void shouldGenerateOnePatrolForEachActiveSubmarine() {

        Campaign campaign = campaign();

        Submarine firstSubmarine = submarine(
                1L,
                "USS Ohio",
                SubmarineStatus.ACTIVE);

        Submarine secondSubmarine = submarine(
                2L,
                "USS Michigan",
                SubmarineStatus.ACTIVE);

        List<Patrol> patrols = patrolGenerator.generatePatrols(
                campaign,
                List.of(
                        firstSubmarine,
                        secondSubmarine));

        assertEquals(
                2,
                patrols.size());

        Patrol firstPatrol = patrols.get(0);

        assertEquals(
                "USS Ohio Patrol",
                firstPatrol.getPatrolName());

        assertEquals(
                LocalDate.of(
                        1985,
                        1,
                        1),
                firstPatrol.getPatrolDate());

        assertEquals(
                "North Atlantic",
                firstPatrol.getArea());

        assertEquals(
                MissionType.DETERRENCE_PATROL,
                firstPatrol.getMissionType());

        assertSame(
                campaign,
                firstPatrol.getCampaign());

        assertSame(
                firstSubmarine,
                firstPatrol.getSubmarine());

        Patrol secondPatrol = patrols.get(1);

        assertEquals(
                "USS Michigan Patrol",
                secondPatrol.getPatrolName());

        assertSame(
                secondSubmarine,
                secondPatrol.getSubmarine());
    }

    @Test
    void shouldGeneratePatrolsOnlyForActiveSubmarines() {

        Campaign campaign = campaign();

        Submarine active = submarine(
                1L,
                "USS Ohio",
                SubmarineStatus.ACTIVE);

        Submarine refit = submarine(
                2L,
                "USS Michigan",
                SubmarineStatus.REFIT);

        Submarine damaged = submarine(
                3L,
                "USS Florida",
                SubmarineStatus.DAMAGED);

        Submarine retired = submarine(
                4L,
                "USS Georgia",
                SubmarineStatus.RETIRED);

        List<Patrol> patrols = patrolGenerator.generatePatrols(
                campaign,
                List.of(
                        active,
                        refit,
                        damaged,
                        retired));

        assertEquals(
                1,
                patrols.size());

        assertSame(
                active,
                patrols.getFirst()
                        .getSubmarine());
    }

    @Test
    void shouldIgnoreNullSubmarineEntries() {

        Campaign campaign = campaign();

        Submarine active = submarine(
                1L,
                "USS Ohio",
                SubmarineStatus.ACTIVE);

        List<Patrol> patrols = patrolGenerator.generatePatrols(
                campaign,
                Arrays.asList(
                        null,
                        active,
                        null));

        assertEquals(
                1,
                patrols.size());

        assertSame(
                active,
                patrols.getFirst()
                        .getSubmarine());
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoSubmarines() {

        List<Patrol> patrols = patrolGenerator.generatePatrols(
                campaign(),
                List.of());

        assertTrue(
                patrols.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenNoSubmarineIsActive() {

        List<Patrol> patrols = patrolGenerator.generatePatrols(
                campaign(),
                List.of(
                        submarine(
                                1L,
                                "USS Michigan",
                                SubmarineStatus.REFIT),
                        submarine(
                                2L,
                                "USS Florida",
                                SubmarineStatus.DAMAGED),
                        submarine(
                                3L,
                                "USS Georgia",
                                SubmarineStatus.RETIRED)));

        assertTrue(
                patrols.isEmpty());
    }

    @Test
    void shouldUseGenericNameWhenSubmarineNameIsMissing() {

        Submarine submarine = submarine(
                1L,
                null,
                SubmarineStatus.ACTIVE);

        List<Patrol> patrols = patrolGenerator.generatePatrols(
                campaign(),
                List.of(
                        submarine));

        assertEquals(
                "Unnamed Submarine Patrol",
                patrols.getFirst()
                        .getPatrolName());
    }

    @Test
    void shouldRejectNullCampaign() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> patrolGenerator.generatePatrols(
                        null,
                        List.of()));

        assertEquals(
                "Campaign must not be null",
                exception.getMessage());
    }

    @Test
    void shouldRejectCampaignWithoutStartDate() {

        Campaign campaign = campaign();

        campaign.setStartDate(
                null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> patrolGenerator.generatePatrols(
                        campaign,
                        List.of()));

        assertEquals(
                "Campaign start date must not be null",
                exception.getMessage());
    }

    @Test
    void shouldRejectNullSubmarineList() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> patrolGenerator.generatePatrols(
                        campaign(),
                        null));

        assertEquals(
                "Submarine list must not be null",
                exception.getMessage());
    }

    private Campaign campaign() {

        Campaign campaign = new Campaign();

        campaign.setId(
                1L);

        campaign.setName(
                "North Atlantic Campaign");

        campaign.setDescription(
                "Dynamic patrol generation test campaign");

        campaign.setStartDate(
                LocalDate.of(
                        1985,
                        1,
                        1));

        campaign.setStatus(
                CampaignStatus.ACTIVE);

        return campaign;
    }

    private Submarine submarine(
            Long id,
            String name,
            SubmarineStatus status) {

        Submarine submarine = new Submarine();

        submarine.setId(
                id);

        submarine.setName(
                name);

        submarine.setStatus(
                status);

        return submarine;
    }

}
