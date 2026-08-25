package com.jastigi.silentcampaignmanager.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jastigi.silentcampaignmanager.dto.CampaignDetailsResponseDTO;
import com.jastigi.silentcampaignmanager.dto.CampaignRequestDTO;
import com.jastigi.silentcampaignmanager.dto.CampaignResponseDTO;
import com.jastigi.silentcampaignmanager.dto.PatrolSummaryDTO;
import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.CampaignStatus;
import com.jastigi.silentcampaignmanager.exception.CampaignNotFoundException;
import com.jastigi.silentcampaignmanager.exception.CampaignOperationNotAllowedException;
import com.jastigi.silentcampaignmanager.mapper.CampaignMapper;
import com.jastigi.silentcampaignmanager.repository.CampaignExecutionRepository;
import com.jastigi.silentcampaignmanager.repository.CampaignRepository;
import com.jastigi.silentcampaignmanager.repository.PatrolRepository;
import com.jastigi.silentcampaignmanager.service.CampaignService;
import com.jastigi.silentcampaignmanager.service.campaign.statistics.CampaignStatistics;
import com.jastigi.silentcampaignmanager.service.campaign.statistics.CampaignStatisticsService;
import com.jastigi.silentcampaignmanager.specification.CampaignSpecifications;

@Service
public class CampaignServiceImpl implements CampaignService {

        private final CampaignRepository campaignRepository;
        private final CampaignStatisticsService campaignStatisticsService;
        private final PatrolRepository patrolRepository;
        private final CampaignExecutionRepository campaignExecutionRepository;

        public CampaignServiceImpl(
                        CampaignRepository campaignRepository,
                        CampaignStatisticsService campaignStatisticsService,
                        PatrolRepository patrolRepository,
                        CampaignExecutionRepository campaignExecutionRepository) {

                this.campaignRepository = campaignRepository;
                this.campaignStatisticsService = campaignStatisticsService;
                this.patrolRepository = patrolRepository;
                this.campaignExecutionRepository = campaignExecutionRepository;
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
        @Transactional(readOnly = true)
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
        @Transactional
        public CampaignResponseDTO updateCampaign(
                        Long id,
                        CampaignRequestDTO request) {

                Campaign campaign = campaignRepository.findById(id)
                                .orElseThrow(() -> new CampaignNotFoundException(id));

                if (campaign.getStatus() != CampaignStatus.ACTIVE) {

                        throw new CampaignOperationNotAllowedException(
                                        "Only active campaigns can be edited");
                }

                campaign.setName(
                                request.getName());

                campaign.setDescription(
                                request.getDescription());

                campaign.setStartDate(
                                request.getStartDate());

                Campaign updatedCampaign =
                                campaignRepository.save(campaign);

                return CampaignMapper.toDTO(
                                updatedCampaign);
        }

        @Override
        @Transactional
        public void deleteCampaign(
                        Long id) {

                Campaign campaign =
                                campaignRepository.findById(id)
                                                .orElseThrow(
                                                                () -> new CampaignNotFoundException(id));

                if (campaign.getStatus() != CampaignStatus.ACTIVE) {

                        throw new CampaignOperationNotAllowedException(
                                        "Only active campaigns can be deleted");
                }

                long patrolCount =
                                patrolRepository.countByCampaignId(id);

                if (patrolCount > 0) {

                        throw new CampaignOperationNotAllowedException(
                                        "Campaigns with patrols cannot be deleted");
                }

                boolean hasExecutions =
                                campaignExecutionRepository.existsByCampaignId(id);

                if (hasExecutions) {

                        throw new CampaignOperationNotAllowedException(
                                        "Campaigns with execution history cannot be deleted");
                }

                campaignRepository.delete(
                                campaign);
        }

        @Override
        public CampaignStatistics getStatistics(Long campaignId) {

                campaignRepository.findById(campaignId)
                                .orElseThrow(() -> new CampaignNotFoundException(campaignId));

                return campaignStatisticsService.calculate(campaignId);
        }

        @Override
        public Page<CampaignResponseDTO> getCampaigns(Pageable pageable) {

                return campaignRepository.findAll(pageable)
                                .map(CampaignMapper::toDTO);

        }

        @Override
        public Page<CampaignResponseDTO> searchCampaigns(
                        CampaignStatus status,
                        Pageable pageable) {

                Specification<Campaign> specification = CampaignSpecifications.hasStatus(status);

                return campaignRepository
                                .findAll(specification, pageable)
                                .map(CampaignMapper::toDTO);

        }

        @Override
        public Page<CampaignResponseDTO> searchCampaigns(
                        CampaignStatus status,
                        String name,
                        Pageable pageable) {

                Specification<Campaign> specification = Specification
                                .where(CampaignSpecifications.hasStatus(status))
                                .and(CampaignSpecifications.nameContains(name));

                return campaignRepository
                                .findAll(specification, pageable)
                                .map(CampaignMapper::toDTO);

        }

}
