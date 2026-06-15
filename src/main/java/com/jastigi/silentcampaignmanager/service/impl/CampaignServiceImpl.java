package com.jastigi.silentcampaignmanager.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jastigi.silentcampaignmanager.dto.CampaignRequestDTO;
import com.jastigi.silentcampaignmanager.dto.CampaignResponseDTO;
import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.CampaignStatus;
import com.jastigi.silentcampaignmanager.exception.CampaignNotFoundException;
import com.jastigi.silentcampaignmanager.mapper.CampaignMapper;
import com.jastigi.silentcampaignmanager.repository.CampaignRepository;
import com.jastigi.silentcampaignmanager.service.CampaignService;

@Service
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository campaignRepository;

    public CampaignServiceImpl(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @Override
    public CampaignResponseDTO createCampaign(CampaignRequestDTO request) {

        Campaign campaign = CampaignMapper.toEntity(request);

        Campaign savedCampaign = campaignRepository.save(campaign);

        return CampaignMapper.toDTO(savedCampaign);
    }

    @Override
    public Page<CampaignResponseDTO> getAllCampaigns(
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        return campaignRepository.findAll(pageable)
                .map(CampaignMapper::toDTO);
    }

    @Override
    public CampaignResponseDTO getCampaignById(Long id) {

        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new CampaignNotFoundException(id));

        return CampaignMapper.toDTO(campaign);
    }

    @Override
    public List<CampaignResponseDTO> getCampaignsByStatus(CampaignStatus status) {

        return campaignRepository.findByStatus(status)
                .stream()
                .map(CampaignMapper::toDTO)
                .toList();
    }

}
