package com.jastigi.silentcampaignmanager.service.campaign.execution.history.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jastigi.silentcampaignmanager.dto.CampaignExecutionResponseDTO;
import com.jastigi.silentcampaignmanager.exception.CampaignNotFoundException;
import com.jastigi.silentcampaignmanager.mapper.CampaignExecutionMapper;
import com.jastigi.silentcampaignmanager.repository.CampaignExecutionRepository;
import com.jastigi.silentcampaignmanager.repository.CampaignRepository;
import com.jastigi.silentcampaignmanager.service.campaign.execution.history.CampaignExecutionHistoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CampaignExecutionHistoryServiceImpl
        implements CampaignExecutionHistoryService {

    private final CampaignRepository campaignRepository;

    private final CampaignExecutionRepository campaignExecutionRepository;

    private final CampaignExecutionMapper campaignExecutionMapper;

    @Override
    public Page<CampaignExecutionResponseDTO> getHistoryByCampaign(
            Long campaignId,
            Pageable pageable) {

        if (!campaignRepository.existsById(
                campaignId)) {

            throw new CampaignNotFoundException(
                    campaignId);
        }

        return campaignExecutionRepository
                .findByCampaignId(
                        campaignId,
                        pageable)
                .map(
                        campaignExecutionMapper::toDTO);
    }

}
