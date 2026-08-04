package com.jastigi.silentcampaignmanager.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jastigi.silentcampaignmanager.dto.CampaignExecutionResponseDTO;
import com.jastigi.silentcampaignmanager.service.campaign.execution.history.CampaignExecutionHistoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
public class CampaignExecutionHistoryController {

    private final CampaignExecutionHistoryService campaignExecutionHistoryService;

    @GetMapping("/{campaignId}/executions")
    public ResponseEntity<Page<CampaignExecutionResponseDTO>> getCampaignExecutionHistory(
            @PathVariable Long campaignId,
            @ParameterObject @PageableDefault(size = 10, sort = "startedAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<CampaignExecutionResponseDTO> history = campaignExecutionHistoryService
                .getHistoryByCampaign(
                        campaignId,
                        pageable);

        return ResponseEntity.ok(
                history);
    }

}
