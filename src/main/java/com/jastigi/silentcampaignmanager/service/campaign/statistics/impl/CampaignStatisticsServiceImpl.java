package com.jastigi.silentcampaignmanager.service.campaign.statistics.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;
import com.jastigi.silentcampaignmanager.repository.ContactRepository;
import com.jastigi.silentcampaignmanager.repository.PatrolRepository;
import com.jastigi.silentcampaignmanager.service.campaign.statistics.CampaignStatistics;
import com.jastigi.silentcampaignmanager.service.campaign.statistics.CampaignStatisticsService;

@Service
public class CampaignStatisticsServiceImpl
                implements CampaignStatisticsService {

        private final PatrolRepository patrolRepository;
        private final ContactRepository contactRepository;

        public CampaignStatisticsServiceImpl(
                        PatrolRepository patrolRepository,
                        ContactRepository contactRepository) {

                this.patrolRepository = patrolRepository;
                this.contactRepository = contactRepository;
        }

        @Override
        public CampaignStatistics calculate(Long campaignId) {

                List<Patrol> patrols = patrolRepository.findByCampaignId(campaignId);

                return CampaignStatistics.builder()
                                .totalPatrols(patrols.size())
                                .successfulPatrols(countPatrols(
                                                patrols,
                                                PatrolResult.SUCCESS))
                                .partialSuccessfulPatrols(countPatrols(
                                                patrols,
                                                PatrolResult.PARTIAL_SUCCESS))
                                .failedPatrols(countPatrols(
                                                patrols,
                                                PatrolResult.FAILURE))
                                .totalContacts(contactRepository.countByPatrolCampaignId(campaignId))
                                .averageRisk(0)
                                .averageMissionScore(calculateAverageMissionScore(patrols))
                                .build();
        }

        private long countPatrols(
                        List<Patrol> patrols,
                        PatrolResult result) {

                return patrols.stream()
                                .filter(p -> p.getResult() == result)
                                .count();
        }

        private double calculateAverageMissionScore(
                        List<Patrol> patrols) {

                if (patrols.isEmpty()) {
                        return 0;
                }

                return patrols.stream()
                                .mapToInt(this::scorePatrol)
                                .average()
                                .orElse(0);

        }

        private int scorePatrol(Patrol patrol) {

                return switch (patrol.getResult()) {

                        case SUCCESS -> 100;

                        case PARTIAL_SUCCESS -> 70;

                        case FAILURE -> 30;

                        default -> 0;
                };

        }

}
