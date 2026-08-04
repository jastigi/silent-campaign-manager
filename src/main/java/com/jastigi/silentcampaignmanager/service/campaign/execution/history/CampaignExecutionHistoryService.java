package com.jastigi.silentcampaignmanager.service.campaign.execution.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jastigi.silentcampaignmanager.dto.CampaignExecutionResponseDTO;

public interface CampaignExecutionHistoryService {

    Page<CampaignExecutionResponseDTO> getHistoryByCampaign(
            Long campaignId,
            Pageable pageable);

}
