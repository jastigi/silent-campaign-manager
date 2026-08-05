package com.jastigi.silentcampaignmanager.service.campaign.patrol.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.CampaignStatus;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.Submarine;
import com.jastigi.silentcampaignmanager.entity.SubmarineStatus;
import com.jastigi.silentcampaignmanager.exception.CampaignNotFoundException;
import com.jastigi.silentcampaignmanager.repository.CampaignRepository;
import com.jastigi.silentcampaignmanager.repository.PatrolRepository;
import com.jastigi.silentcampaignmanager.repository.SubmarineRepository;
import com.jastigi.silentcampaignmanager.service.campaign.lifecycle.CampaignLifecycleService;
import com.jastigi.silentcampaignmanager.service.campaign.patrol.CampaignPatrolGenerationService;
import com.jastigi.silentcampaignmanager.service.campaign.patrol.generator.PatrolGenerator;

@ExtendWith(MockitoExtension.class)
class CampaignPatrolGenerationServiceImplTest {

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private SubmarineRepository submarineRepository;

    @Mock
    private PatrolRepository patrolRepository;

    @Mock
    private PatrolGenerator patrolGenerator;

    @Mock
    private CampaignLifecycleService campaignLifecycleService;

    private CampaignPatrolGenerationService campaignPatrolGenerationService;

    @BeforeEach
    void setUp() {

        campaignPatrolGenerationService = new CampaignPatrolGenerationServiceImpl(
                campaignRepository,
                submarineRepository,
                patrolRepository,
                patrolGenerator,
                campaignLifecycleService);
    }

    @Test
    void shouldGenerateAndPersistPatrolsForAvailableSubmarines() {

        Long campaignId = 1L;

        Campaign campaign = campaign(
                campaignId);

        Submarine firstSubmarine = submarine(
                10L,
                "USS Ohio");

        Submarine secondSubmarine = submarine(
                20L,
                "USS Michigan");

        Patrol firstGeneratedPatrol = Patrol.builder()
                .campaign(
                        campaign)
                .submarine(
                        firstSubmarine)
                .build();

        Patrol secondGeneratedPatrol = Patrol.builder()
                .campaign(
                        campaign)
                .submarine(
                        secondSubmarine)
                .build();

        List<Submarine> activeSubmarines = List.of(
                firstSubmarine,
                secondSubmarine);

        List<Patrol> generatedPatrols = List.of(
                firstGeneratedPatrol,
                secondGeneratedPatrol);

        when(
                campaignRepository.findById(
                        campaignId))
                .thenReturn(
                        Optional.of(
                                campaign));

        when(
                submarineRepository.findByStatus(
                        SubmarineStatus.ACTIVE))
                .thenReturn(
                        activeSubmarines);

        when(
                patrolRepository.findByCampaignId(
                        campaignId))
                .thenReturn(
                        List.of());

        when(
                patrolGenerator.generatePatrols(
                        campaign,
                        activeSubmarines))
                .thenReturn(
                        generatedPatrols);

        int generatedCount = campaignPatrolGenerationService
                .generatePatrols(
                        campaignId);

        assertEquals(
                2,
                generatedCount);

        verify(
                campaignLifecycleService)
                .validatePatrolGenerationAllowed(
                        campaign);

        verify(
                patrolGenerator)
                .generatePatrols(
                        campaign,
                        activeSubmarines);

        verify(
                patrolRepository)
                .saveAll(
                        generatedPatrols);
    }

    @Test
    void shouldExcludeSubmarinesAlreadyAssignedToCampaign() {

        Long campaignId = 2L;

        Campaign campaign = campaign(
                campaignId);

        Submarine assignedSubmarine = submarine(
                10L,
                "USS Ohio");

        Submarine availableSubmarine = submarine(
                20L,
                "USS Michigan");

        Patrol existingPatrol = Patrol.builder()
                .campaign(
                        campaign)
                .submarine(
                        assignedSubmarine)
                .build();

        Patrol generatedPatrol = Patrol.builder()
                .campaign(
                        campaign)
                .submarine(
                        availableSubmarine)
                .build();

        when(
                campaignRepository.findById(
                        campaignId))
                .thenReturn(
                        Optional.of(
                                campaign));

        when(
                submarineRepository.findByStatus(
                        SubmarineStatus.ACTIVE))
                .thenReturn(
                        List.of(
                                assignedSubmarine,
                                availableSubmarine));

        when(
                patrolRepository.findByCampaignId(
                        campaignId))
                .thenReturn(
                        List.of(
                                existingPatrol));

        when(
                patrolGenerator.generatePatrols(
                        campaign,
                        List.of(
                                availableSubmarine)))
                .thenReturn(
                        List.of(
                                generatedPatrol));

        int generatedCount = campaignPatrolGenerationService
                .generatePatrols(
                        campaignId);

        assertEquals(
                1,
                generatedCount);

        verify(
                patrolGenerator)
                .generatePatrols(
                        campaign,
                        List.of(
                                availableSubmarine));

        verify(
                patrolRepository)
                .saveAll(
                        List.of(
                                generatedPatrol));
    }

    @Test
    void shouldReturnZeroWhenEveryActiveSubmarineIsAlreadyAssigned() {

        Long campaignId = 3L;

        Campaign campaign = campaign(
                campaignId);

        Submarine submarine = submarine(
                10L,
                "USS Ohio");

        Patrol existingPatrol = Patrol.builder()
                .campaign(
                        campaign)
                .submarine(
                        submarine)
                .build();

        when(
                campaignRepository.findById(
                        campaignId))
                .thenReturn(
                        Optional.of(
                                campaign));

        when(
                submarineRepository.findByStatus(
                        SubmarineStatus.ACTIVE))
                .thenReturn(
                        List.of(
                                submarine));

        when(
                patrolRepository.findByCampaignId(
                        campaignId))
                .thenReturn(
                        List.of(
                                existingPatrol));

        when(
                patrolGenerator.generatePatrols(
                        campaign,
                        List.of()))
                .thenReturn(
                        List.of());

        int generatedCount = campaignPatrolGenerationService
                .generatePatrols(
                        campaignId);

        assertEquals(
                0,
                generatedCount);

        verify(
                patrolRepository,
                never())
                .saveAll(
                        org.mockito.ArgumentMatchers.anyList());
    }

    @Test
    void shouldReturnZeroWhenThereAreNoActiveSubmarines() {

        Long campaignId = 4L;

        Campaign campaign = campaign(
                campaignId);

        when(
                campaignRepository.findById(
                        campaignId))
                .thenReturn(
                        Optional.of(
                                campaign));

        when(
                submarineRepository.findByStatus(
                        SubmarineStatus.ACTIVE))
                .thenReturn(
                        List.of());

        when(
                patrolRepository.findByCampaignId(
                        campaignId))
                .thenReturn(
                        List.of());

        when(
                patrolGenerator.generatePatrols(
                        campaign,
                        List.of()))
                .thenReturn(
                        List.of());

        int generatedCount = campaignPatrolGenerationService
                .generatePatrols(
                        campaignId);

        assertEquals(
                0,
                generatedCount);

        verify(
                patrolRepository,
                never())
                .saveAll(
                        org.mockito.ArgumentMatchers.anyList());
    }

    @Test
    void shouldThrowExceptionWhenCampaignDoesNotExist() {

        Long campaignId = 999L;

        when(
                campaignRepository.findById(
                        campaignId))
                .thenReturn(
                        Optional.empty());

        assertThrows(
                CampaignNotFoundException.class,
                () -> campaignPatrolGenerationService
                        .generatePatrols(
                                campaignId));

        verify(
                campaignLifecycleService,
                never())
                .validatePatrolGenerationAllowed(
                        org.mockito.ArgumentMatchers.any());

        verify(
                submarineRepository,
                never())
                .findByStatus(
                        org.mockito.ArgumentMatchers.any());

        verify(
                patrolRepository,
                never())
                .findByCampaignId(
                        org.mockito.ArgumentMatchers.anyLong());

        verify(
                patrolGenerator,
                never())
                .generatePatrols(
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.anyList());
    }

    private Campaign campaign(
            Long campaignId) {

        Campaign campaign = new Campaign();

        campaign.setId(
                campaignId);

        campaign.setName(
                "Campaign " + campaignId);

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
            Long submarineId,
            String name) {

        Submarine submarine = new Submarine();

        submarine.setId(
                submarineId);

        submarine.setName(
                name);

        submarine.setStatus(
                SubmarineStatus.ACTIVE);

        return submarine;
    }

}
