package com.jastigi.silentcampaignmanager.service.campaign.statistics.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jastigi.silentcampaignmanager.entity.SimulationOutcome;
import com.jastigi.silentcampaignmanager.entity.SimulationRecord;
import com.jastigi.silentcampaignmanager.repository.SimulationRecordRepository;
import com.jastigi.silentcampaignmanager.service.campaign.progress.CampaignProgressService;
import com.jastigi.silentcampaignmanager.service.campaign.progress.result.CampaignProgress;
import com.jastigi.silentcampaignmanager.service.campaign.statistics.CampaignStatistics;
import com.jastigi.silentcampaignmanager.service.campaign.statistics.CampaignStatisticsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CampaignStatisticsServiceImpl
                implements CampaignStatisticsService {

        private final SimulationRecordRepository simulationRecordRepository;

        private final CampaignProgressService campaignProgressService;

        @Override
        @Transactional(readOnly = true)
        public CampaignStatistics calculate(
                        Long campaignId) {

                CampaignProgress progress = campaignProgressService.getProgress(
                                campaignId);

                List<SimulationRecord> simulationRecords = simulationRecordRepository
                                .findByPatrolCampaignId(
                                                campaignId);

                long totalSimulations = simulationRecords.size();

                long successfulSimulations = countByOutcome(
                                simulationRecords,
                                SimulationOutcome.SUCCESS);

                long partialSuccessfulSimulations = countByOutcome(
                                simulationRecords,
                                SimulationOutcome.PARTIAL_SUCCESS);

                long failedSimulations = countByOutcome(
                                simulationRecords,
                                SimulationOutcome.FAILURE);

                return CampaignStatistics.builder()
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
                                .totalSimulations(
                                                totalSimulations)
                                .successfulSimulations(
                                                successfulSimulations)
                                .partialSuccessfulSimulations(
                                                partialSuccessfulSimulations)
                                .failedSimulations(
                                                failedSimulations)
                                .successRate(
                                                calculateSuccessRate(
                                                                successfulSimulations,
                                                                totalSimulations))
                                .averageMissionScore(
                                                calculateAverageMissionScore(
                                                                simulationRecords))
                                .totalContactsDetected(
                                                sumContactsDetected(
                                                                simulationRecords))
                                .totalContactsLost(
                                                sumContactsLost(
                                                                simulationRecords))
                                .totalIntelligenceGathered(
                                                sumIntelligenceGathered(
                                                                simulationRecords))
                                .totalIncidents(
                                                sumIncidents(
                                                                simulationRecords))
                                .build();
        }

        private long countByOutcome(
                        List<SimulationRecord> simulationRecords,
                        SimulationOutcome outcome) {

                return simulationRecords.stream()
                                .filter(record -> record.getMissionOutcome() == outcome)
                                .count();
        }

        private double calculateSuccessRate(
                        long successfulSimulations,
                        long totalSimulations) {

                if (totalSimulations == 0) {
                        return 0.0;
                }

                double successRate = successfulSimulations
                                * 100.0
                                / totalSimulations;

                return roundToTwoDecimals(
                                successRate);
        }

        private double calculateAverageMissionScore(
                        List<SimulationRecord> simulationRecords) {

                double average = simulationRecords.stream()
                                .mapToInt(
                                                SimulationRecord::getMissionScore)
                                .average()
                                .orElse(0.0);

                return roundToTwoDecimals(
                                average);
        }

        private long sumContactsDetected(
                        List<SimulationRecord> simulationRecords) {

                return simulationRecords.stream()
                                .mapToLong(
                                                SimulationRecord::getContactsDetected)
                                .sum();
        }

        private long sumContactsLost(
                        List<SimulationRecord> simulationRecords) {

                return simulationRecords.stream()
                                .mapToLong(
                                                SimulationRecord::getContactsLost)
                                .sum();
        }

        private long sumIntelligenceGathered(
                        List<SimulationRecord> simulationRecords) {

                return simulationRecords.stream()
                                .mapToLong(
                                                SimulationRecord::getIntelligenceGathered)
                                .sum();
        }

        private long sumIncidents(
                        List<SimulationRecord> simulationRecords) {

                return simulationRecords.stream()
                                .mapToLong(
                                                SimulationRecord::getIncidents)
                                .sum();
        }

        private double roundToTwoDecimals(
                        double value) {

                return Math.round(
                                value * 100.0)
                                / 100.0;
        }

}
