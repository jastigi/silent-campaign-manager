package com.jastigi.silentcampaignmanager.service.campaign.execution.history.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.jastigi.silentcampaignmanager.dto.CampaignExecutionResponseDTO;
import com.jastigi.silentcampaignmanager.entity.CampaignExecution;
import com.jastigi.silentcampaignmanager.exception.CampaignNotFoundException;
import com.jastigi.silentcampaignmanager.mapper.CampaignExecutionMapper;
import com.jastigi.silentcampaignmanager.repository.CampaignExecutionRepository;
import com.jastigi.silentcampaignmanager.repository.CampaignRepository;
import com.jastigi.silentcampaignmanager.service.campaign.execution.history.CampaignExecutionHistoryService;

@ExtendWith(MockitoExtension.class)
class CampaignExecutionHistoryServiceImplTest {

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private CampaignExecutionRepository campaignExecutionRepository;

    @Mock
    private CampaignExecutionMapper campaignExecutionMapper;

    private CampaignExecutionHistoryService campaignExecutionHistoryService;

    @BeforeEach
    void setUp() {

        campaignExecutionHistoryService = new CampaignExecutionHistoryServiceImpl(
                campaignRepository,
                campaignExecutionRepository,
                campaignExecutionMapper);
    }

    @Test
    void shouldReturnPagedCampaignExecutionHistory() {

        Long campaignId = 1L;

        Pageable pageable = PageRequest.of(
                0,
                10);

        CampaignExecution execution = CampaignExecution.builder()
                .id(10L)
                .build();

        CampaignExecutionResponseDTO dto = CampaignExecutionResponseDTO.builder()
                .id(10L)
                .campaignId(
                        campaignId)
                .status(
                        "COMPLETED")
                .build();

        Page<CampaignExecution> page = new PageImpl<>(
                List.of(
                        execution),
                pageable,
                1);

        when(
                campaignRepository.existsById(
                        campaignId))
                .thenReturn(
                        true);

        when(
                campaignExecutionRepository.findByCampaignId(
                        campaignId,
                        pageable))
                .thenReturn(
                        page);

        when(
                campaignExecutionMapper.toDTO(
                        execution))
                .thenReturn(
                        dto);

        Page<CampaignExecutionResponseDTO> result = campaignExecutionHistoryService
                .getHistoryByCampaign(
                        campaignId,
                        pageable);

        assertEquals(
                1,
                result.getTotalElements());

        assertEquals(
                dto,
                result.getContent()
                        .getFirst());

        verify(
                campaignRepository)
                .existsById(
                        campaignId);

        verify(
                campaignExecutionRepository)
                .findByCampaignId(
                        campaignId,
                        pageable);

        verify(
                campaignExecutionMapper)
                .toDTO(
                        execution);
    }

    @Test
    void shouldReturnEmptyPageWhenCampaignHasNoExecutions() {

        Long campaignId = 2L;

        Pageable pageable = PageRequest.of(
                0,
                10);

        when(
                campaignRepository.existsById(
                        campaignId))
                .thenReturn(
                        true);

        when(
                campaignExecutionRepository.findByCampaignId(
                        campaignId,
                        pageable))
                .thenReturn(
                        Page.empty(
                                pageable));

        Page<CampaignExecutionResponseDTO> result = campaignExecutionHistoryService
                .getHistoryByCampaign(
                        campaignId,
                        pageable);

        assertEquals(
                0,
                result.getTotalElements());

        assertEquals(
                0,
                result.getContent()
                        .size());
    }

    @Test
    void shouldThrowExceptionWhenCampaignDoesNotExist() {

        Long campaignId = 999L;

        Pageable pageable = PageRequest.of(
                0,
                10);

        when(
                campaignRepository.existsById(
                        campaignId))
                .thenReturn(
                        false);

        assertThrows(
                CampaignNotFoundException.class,
                () -> campaignExecutionHistoryService
                        .getHistoryByCampaign(
                                campaignId,
                                pageable));

        verify(
                campaignExecutionRepository,
                never())
                .findByCampaignId(
                        org.mockito.ArgumentMatchers.anyLong(),
                        org.mockito.ArgumentMatchers.any(
                                Pageable.class));

        verify(
                campaignExecutionMapper,
                never())
                .toDTO(
                        org.mockito.ArgumentMatchers.any());
    }

}
