package com.jastigi.silentcampaignmanager.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.jastigi.silentcampaignmanager.dto.CampaignDetailsResponseDTO;
import com.jastigi.silentcampaignmanager.dto.CampaignRequestDTO;
import com.jastigi.silentcampaignmanager.dto.CampaignResponseDTO;
import com.jastigi.silentcampaignmanager.dto.PatrolSummaryDTO;
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
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

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

    @Override
    public CampaignDetailsResponseDTO getCampaignDetails(Long id) {

        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new CampaignNotFoundException(id));

        CampaignDetailsResponseDTO dto = new CampaignDetailsResponseDTO();

        dto.setId(campaign.getId());
        dto.setName(campaign.getName());
        dto.setDescription(campaign.getDescription());
        dto.setStartDate(campaign.getStartDate());
        dto.setStatus(campaign.getStatus());

        List<PatrolSummaryDTO> patrols = campaign.getPatrols()
                .stream()
                .map(patrol -> {

                    PatrolSummaryDTO patrolDto = new PatrolSummaryDTO();

                    patrolDto.setId(
                            patrol.getId());

                    patrolDto.setPatrolName(
                            patrol.getPatrolName());

                    patrolDto.setArea(
                            patrol.getArea());

                    patrolDto.setResult(
                            patrol.getResult());

                    return patrolDto;

                })
                .toList();

        dto.setPatrols(patrols);

        return dto;
    }

    @Override
    public CampaignResponseDTO updateCampaign(
            Long id,
            CampaignRequestDTO request) {

        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new CampaignNotFoundException(id));

        campaign.setName(request.getName());
        campaign.setDescription(request.getDescription());
        campaign.setStartDate(request.getStartDate());
        campaign.setStatus(request.getStatus());

        Campaign updatedCampaign = campaignRepository.save(campaign);

        return CampaignMapper.toDTO(updatedCampaign);
    }

    @Override
    public void deleteCampaign(Long id) {

        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new CampaignNotFoundException(id));

        campaignRepository.delete(campaign);
    }

}
