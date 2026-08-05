package com.jastigi.silentcampaignmanager.service.campaign.timeline.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jastigi.silentcampaignmanager.dto.CampaignTimelineEventDTO;
import com.jastigi.silentcampaignmanager.entity.CampaignExecution;
import com.jastigi.silentcampaignmanager.entity.SimulationRecord;
import com.jastigi.silentcampaignmanager.exception.CampaignNotFoundException;
import com.jastigi.silentcampaignmanager.repository.CampaignExecutionRepository;
import com.jastigi.silentcampaignmanager.repository.CampaignRepository;
import com.jastigi.silentcampaignmanager.repository.SimulationRecordRepository;
import com.jastigi.silentcampaignmanager.service.campaign.timeline.CampaignTimelineService;
import com.jastigi.silentcampaignmanager.service.campaign.timeline.assembler.TimelineAssembler;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CampaignTimelineServiceImpl
        implements CampaignTimelineService {

    private final CampaignRepository campaignRepository;

    private final CampaignExecutionRepository campaignExecutionRepository;

    private final SimulationRecordRepository simulationRecordRepository;

    private final TimelineAssembler timelineAssembler;

    @Override
    public List<CampaignTimelineEventDTO> getTimeline(
            Long campaignId) {

        validateCampaignExists(
                campaignId);

        List<CampaignExecution> campaignExecutions = campaignExecutionRepository
                .findByCampaignId(
                        campaignId);

        List<SimulationRecord> simulationRecords = simulationRecordRepository
                .findByPatrolCampaignId(
                        campaignId);

        return timelineAssembler.assemble(
                campaignExecutions,
                simulationRecords);
    }

    private void validateCampaignExists(
            Long campaignId) {

        if (!campaignRepository.existsById(
                campaignId)) {

            throw new CampaignNotFoundException(
                    campaignId);
        }
    }

}
