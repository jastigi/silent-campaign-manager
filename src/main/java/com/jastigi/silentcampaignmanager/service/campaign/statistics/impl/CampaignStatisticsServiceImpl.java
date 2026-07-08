package com.jastigi.silentcampaignmanager.service.campaign.statistics.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;
import com.jastigi.silentcampaignmanager.repository.PatrolRepository;
import com.jastigi.silentcampaignmanager.service.campaign.statistics.CampaignStatistics;
import com.jastigi.silentcampaignmanager.service.campaign.statistics.CampaignStatisticsService;

@Service
public class CampaignStatisticsServiceImpl
                implements CampaignStatisticsService {

        private final PatrolRepository patrolRepository;

        public CampaignStatisticsServiceImpl(
                        PatrolRepository patrolRepository) {

                this.patrolRepository = patrolRepository;
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
                                .totalContacts(0)
                                .averageRisk(0)
                                .build();
        }

        private long countPatrols(
                        List<Patrol> patrols,
                        PatrolResult result) {

                return patrols.stream()
                                .filter(p -> p.getResult() == result)
                                .count();
        }

}
