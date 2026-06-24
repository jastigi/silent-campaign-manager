package com.jastigi.silentcampaignmanager.service;

import java.util.List;

import com.jastigi.silentcampaignmanager.dto.PatrolRequestDTO;
import com.jastigi.silentcampaignmanager.dto.PatrolResponseDTO;

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

}
