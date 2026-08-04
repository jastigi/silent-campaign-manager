package com.jastigi.silentcampaignmanager.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jastigi.silentcampaignmanager.dto.CampaignProgressResponseDTO;
import com.jastigi.silentcampaignmanager.dto.CampaignSimulationResponseDTO;
import com.jastigi.silentcampaignmanager.dto.SimulationResultDTO;
import com.jastigi.silentcampaignmanager.service.campaign.progress.result.CampaignProgress;
import com.jastigi.silentcampaignmanager.service.campaign.simulation.result.CampaignSimulationResult;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CampaignSimulationMapper {

    private final SimulationMapper simulationMapper;

    public CampaignSimulationResponseDTO toDTO(
            CampaignSimulationResult result) {

        if (result == null) {
            throw new IllegalArgumentException(
                    "Campaign simulation result must not be null");
        }

        List<SimulationResultDTO> patrolResults = result.getPatrolResults()
                .stream()
                .map(
                        simulationMapper::toDto)
                .toList();

        return CampaignSimulationResponseDTO.builder()
                .campaignId(
                        result.getCampaign()
                                .getId())
                .campaignName(
                        result.getCampaign()
                                .getName())
                .executedAt(
                        result.getExecutedAt())
                .progress(
                        mapProgress(
                                result.getProgress()))
                .patrolResults(
                        patrolResults)
                .build();
    }

    private CampaignProgressResponseDTO mapProgress(
            CampaignProgress progress) {

        return CampaignProgressResponseDTO.builder()
                .totalPatrols(
                        progress.getTotalPatrols())
                .completedPatrols(
                        progress.getCompletedPatrols())
                .pendingPatrols(
                        progress.getPendingPatrols())
                .completionPercentage(
                        progress.getCompletionPercentage())
                .completed(
                        progress.isCompleted())
                .build();
    }

}