package com.jastigi.silentcampaignmanager.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jastigi.silentcampaignmanager.dto.PatrolRequestDTO;
import com.jastigi.silentcampaignmanager.dto.PatrolResponseDTO;
import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.exception.CampaignNotFoundException;
import com.jastigi.silentcampaignmanager.mapper.PatrolMapper;
import com.jastigi.silentcampaignmanager.repository.CampaignRepository;
import com.jastigi.silentcampaignmanager.repository.PatrolRepository;
import com.jastigi.silentcampaignmanager.service.PatrolService;

@Service
public class PatrolServiceImpl implements PatrolService {

    private final PatrolRepository patrolRepository;
    private final CampaignRepository campaignRepository;

    public PatrolServiceImpl(
            PatrolRepository patrolRepository,
            CampaignRepository campaignRepository) {

        this.patrolRepository = patrolRepository;
        this.campaignRepository = campaignRepository;
    }

    @Override
    public PatrolResponseDTO createPatrol(
            Long campaignId,
            PatrolRequestDTO request) {

        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new CampaignNotFoundException(
                        campaignId));

        Patrol patrol = PatrolMapper.toEntity(request);

        patrol.setCampaign(campaign);

        Patrol savedPatrol = patrolRepository.save(patrol);

        return PatrolMapper.toDTO(savedPatrol);
    }

    @Override
    public List<PatrolResponseDTO> getPatrolsByCampaign(
            Long campaignId) {

        return patrolRepository
                .findByCampaignId(campaignId)
                .stream()
                .map(PatrolMapper::toDTO)
                .toList();
    }

}
