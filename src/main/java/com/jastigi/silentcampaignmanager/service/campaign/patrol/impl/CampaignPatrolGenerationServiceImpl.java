package com.jastigi.silentcampaignmanager.service.campaign.patrol.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jastigi.silentcampaignmanager.entity.Campaign;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CampaignPatrolGenerationServiceImpl
        implements CampaignPatrolGenerationService {

    private final CampaignRepository campaignRepository;

    private final SubmarineRepository submarineRepository;

    private final PatrolRepository patrolRepository;

    private final PatrolGenerator patrolGenerator;

    private final CampaignLifecycleService campaignLifecycleService;

    @Override
    @Transactional
    public int generatePatrols(
            Long campaignId) {

        Campaign campaign = campaignRepository.findById(
                campaignId)
                .orElseThrow(
                        () -> new CampaignNotFoundException(
                                campaignId));

        campaignLifecycleService
                .validatePatrolGenerationAllowed(
                        campaign);

        List<Submarine> activeSubmarines = submarineRepository.findByStatus(
                SubmarineStatus.ACTIVE);

        List<Patrol> existingPatrols = patrolRepository.findByCampaignId(
                campaignId);

        Set<Long> assignedSubmarineIds = extractAssignedSubmarineIds(
                existingPatrols);

        List<Submarine> availableSubmarines = activeSubmarines.stream()
                .filter(
                        submarine -> isNotAlreadyAssigned(
                                submarine,
                                assignedSubmarineIds))
                .toList();

        List<Patrol> generatedPatrols = patrolGenerator.generatePatrols(
                campaign,
                availableSubmarines);

        if (generatedPatrols.isEmpty()) {
            return 0;
        }

        patrolRepository.saveAll(
                generatedPatrols);

        return generatedPatrols.size();
    }

    private Set<Long> extractAssignedSubmarineIds(
            List<Patrol> existingPatrols) {

        Set<Long> assignedSubmarineIds = new HashSet<>();

        for (Patrol patrol : existingPatrols) {

            if (patrol == null
                    || patrol.getSubmarine() == null
                    || patrol.getSubmarine()
                            .getId() == null) {

                continue;
            }

            assignedSubmarineIds.add(
                    patrol.getSubmarine()
                            .getId());
        }

        return assignedSubmarineIds;
    }

    private boolean isNotAlreadyAssigned(
            Submarine submarine,
            Set<Long> assignedSubmarineIds) {

        if (submarine == null) {
            return false;
        }

        Long submarineId = submarine.getId();

        return submarineId == null
                || !assignedSubmarineIds.contains(
                        submarineId);
    }

}
