package com.jastigi.silentcampaignmanager.service.campaign.lifecycle.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.CampaignStatus;
import com.jastigi.silentcampaignmanager.exception.CampaignNotFoundException;
import com.jastigi.silentcampaignmanager.exception.InvalidCampaignTransitionException;
import com.jastigi.silentcampaignmanager.repository.CampaignRepository;
import com.jastigi.silentcampaignmanager.service.campaign.lifecycle.CampaignLifecycleService;
import com.jastigi.silentcampaignmanager.service.campaign.progress.CampaignProgressService;
import com.jastigi.silentcampaignmanager.service.campaign.progress.result.CampaignProgress;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CampaignLifecycleServiceImpl
                implements CampaignLifecycleService {

        private final CampaignRepository campaignRepository;

        private final CampaignProgressService campaignProgressService;

        @Override
        @Transactional
        public Campaign finishCampaign(
                        Long campaignId) {

                Campaign campaign = findCampaign(
                                campaignId);

                validateActiveCampaign(
                                campaign,
                                CampaignStatus.FINISHED);

                CampaignProgress progress = campaignProgressService.getProgress(
                                campaignId);

                validateCampaignCanFinish(
                                progress);

                campaign.setStatus(
                                CampaignStatus.FINISHED);

                return campaignRepository.save(
                                campaign);
        }

        @Override
        @Transactional
        public Campaign abandonCampaign(
                        Long campaignId) {

                Campaign campaign = findCampaign(
                                campaignId);

                validateActiveCampaign(
                                campaign,
                                CampaignStatus.ABANDONED);

                campaign.setStatus(
                                CampaignStatus.ABANDONED);

                return campaignRepository.save(
                                campaign);
        }

        @Override
        public void validateExecutionAllowed(
                        Campaign campaign) {

                if (campaign == null) {

                        throw new IllegalArgumentException(
                                        "Campaign must not be null");
                }

                if (campaign.getStatus() != CampaignStatus.ACTIVE) {

                        throw new InvalidCampaignTransitionException(
                                        "Campaign cannot be simulated because its status is "
                                                        + campaign.getStatus());
                }
        }

        @Override
        public void validatePatrolGenerationAllowed(
                        Campaign campaign) {

                if (campaign == null) {

                        throw new IllegalArgumentException(
                                        "Campaign must not be null");
                }

                if (campaign.getStatus() != CampaignStatus.ACTIVE) {

                        throw new InvalidCampaignTransitionException(
                                        "Patrols cannot be generated because campaign status is "
                                                        + campaign.getStatus());
                }
        }

        private Campaign findCampaign(
                        Long campaignId) {

                return campaignRepository.findById(
                                campaignId)
                                .orElseThrow(
                                                () -> new CampaignNotFoundException(
                                                                campaignId));
        }

        private void validateActiveCampaign(
                        Campaign campaign,
                        CampaignStatus targetStatus) {

                if (campaign.getStatus() != CampaignStatus.ACTIVE) {

                        throw new InvalidCampaignTransitionException(
                                        campaign.getStatus(),
                                        targetStatus);
                }
        }

        private void validateCampaignCanFinish(
                        CampaignProgress progress) {

                if (progress.getTotalPatrols() == 0) {

                        throw new InvalidCampaignTransitionException(
                                        "Campaign cannot be finished because it has no patrols");
                }

                if (!progress.isCompleted()) {

                        throw new InvalidCampaignTransitionException(
                                        "Campaign cannot be finished because its patrol progression is incomplete");
                }
        }

}
