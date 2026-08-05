package com.jastigi.silentcampaignmanager.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jastigi.silentcampaignmanager.dto.CampaignTimelineEventDTO;
import com.jastigi.silentcampaignmanager.service.campaign.timeline.CampaignTimelineService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
public class CampaignTimelineController {

    private final CampaignTimelineService campaignTimelineService;

    @GetMapping("/{campaignId}/timeline")
    public ResponseEntity<List<CampaignTimelineEventDTO>> getCampaignTimeline(
            @PathVariable Long campaignId) {

        List<CampaignTimelineEventDTO> timeline = campaignTimelineService.getTimeline(
                campaignId);

        return ResponseEntity.ok(
                timeline);
    }

}
