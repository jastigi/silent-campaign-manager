package com.jastigi.silentcampaignmanager.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jastigi.silentcampaignmanager.dto.ContactResponseDTO;
import com.jastigi.silentcampaignmanager.dto.PatrolReportDTO;
import com.jastigi.silentcampaignmanager.dto.PatrolRequestDTO;
import com.jastigi.silentcampaignmanager.dto.PatrolResponseDTO;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;
import com.jastigi.silentcampaignmanager.service.mission.model.MissionEvaluationResult;

public interface PatrolService {

        PatrolResponseDTO createPatrol(
                        Long campaignId,
                        PatrolRequestDTO request);

        List<PatrolResponseDTO> getPatrolsByCampaign(
                        Long campaignId);

        PatrolResponseDTO getPatrolById(
                        Long id);

        PatrolResponseDTO updatePatrol(
                        Long id,
                        PatrolRequestDTO request);

        void deletePatrol(Long id);

        PatrolReportDTO generatePatrolReport(
                        Long patrolId);

        List<ContactResponseDTO> getContacts(Long patrolId);

        PatrolResponseDTO closePatrol(Long patrolId);

        MissionEvaluationResult getMissionEvaluation(Long patrolId);

        Page<PatrolResponseDTO> getPatrols(
                        Long campaignId,
                        Pageable pageable);

        Page<PatrolResponseDTO> searchPatrols(
                        Long campaignId,
                        PatrolResult result,
                        Pageable pageable);

}
