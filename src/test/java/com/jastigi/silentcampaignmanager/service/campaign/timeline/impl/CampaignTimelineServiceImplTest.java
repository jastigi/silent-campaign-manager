package com.jastigi.silentcampaignmanager.service.campaign.timeline.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.dto.CampaignTimelineEventDTO;
import com.jastigi.silentcampaignmanager.dto.CampaignTimelineEventType;
import com.jastigi.silentcampaignmanager.entity.CampaignExecution;
import com.jastigi.silentcampaignmanager.entity.SimulationRecord;
import com.jastigi.silentcampaignmanager.exception.CampaignNotFoundException;
import com.jastigi.silentcampaignmanager.repository.CampaignExecutionRepository;
import com.jastigi.silentcampaignmanager.repository.CampaignRepository;
import com.jastigi.silentcampaignmanager.repository.SimulationRecordRepository;
import com.jastigi.silentcampaignmanager.service.campaign.timeline.CampaignTimelineService;
import com.jastigi.silentcampaignmanager.service.campaign.timeline.assembler.TimelineAssembler;

@ExtendWith(MockitoExtension.class)
class CampaignTimelineServiceImplTest {

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private CampaignExecutionRepository campaignExecutionRepository;

    @Mock
    private SimulationRecordRepository simulationRecordRepository;

    @Mock
    private TimelineAssembler timelineAssembler;

    private CampaignTimelineService campaignTimelineService;

    @BeforeEach
    void setUp() {

        campaignTimelineService = new CampaignTimelineServiceImpl(
                campaignRepository,
                campaignExecutionRepository,
                simulationRecordRepository,
                timelineAssembler);
    }

    @Test
    void shouldReturnAssembledCampaignTimeline() {

        Long campaignId = 1L;

        CampaignExecution execution = CampaignExecution.builder()
                .id(10L)
                .build();

        SimulationRecord simulationRecord = SimulationRecord.builder()
                .id(20L)
                .build();

        List<CampaignExecution> executions = List.of(
                execution);

        List<SimulationRecord> simulationRecords = List.of(
                simulationRecord);

        CampaignTimelineEventDTO event = CampaignTimelineEventDTO.builder()
                .timestamp(
                        LocalDateTime.of(
                                2026,
                                8,
                                5,
                                10,
                                0))
                .type(
                        CampaignTimelineEventType.CAMPAIGN_EXECUTION_STARTED)
                .description(
                        "Campaign execution started")
                .build();

        List<CampaignTimelineEventDTO> assembledTimeline = List.of(
                event);

        when(
                campaignRepository.existsById(
                        campaignId))
                .thenReturn(
                        true);

        when(
                campaignExecutionRepository.findByCampaignId(
                        campaignId))
                .thenReturn(
                        executions);

        when(
                simulationRecordRepository
                        .findByPatrolCampaignId(
                                campaignId))
                .thenReturn(
                        simulationRecords);

        when(
                timelineAssembler.assemble(
                        executions,
                        simulationRecords))
                .thenReturn(
                        assembledTimeline);

        List<CampaignTimelineEventDTO> result = campaignTimelineService.getTimeline(
                campaignId);

        assertSame(
                assembledTimeline,
                result);

        assertEquals(
                1,
                result.size());

        assertSame(
                event,
                result.getFirst());

        verify(
                campaignRepository)
                .existsById(
                        campaignId);

        verify(
                campaignExecutionRepository)
                .findByCampaignId(
                        campaignId);

        verify(
                simulationRecordRepository)
                .findByPatrolCampaignId(
                        campaignId);

        verify(
                timelineAssembler)
                .assemble(
                        executions,
                        simulationRecords);
    }

    @Test
    void shouldReturnEmptyTimelineWhenCampaignHasNoHistory() {

        Long campaignId = 2L;

        when(
                campaignRepository.existsById(
                        campaignId))
                .thenReturn(
                        true);

        when(
                campaignExecutionRepository.findByCampaignId(
                        campaignId))
                .thenReturn(
                        List.of());

        when(
                simulationRecordRepository
                        .findByPatrolCampaignId(
                                campaignId))
                .thenReturn(
                        List.of());

        when(
                timelineAssembler.assemble(
                        List.of(),
                        List.of()))
                .thenReturn(
                        List.of());

        List<CampaignTimelineEventDTO> result = campaignTimelineService.getTimeline(
                campaignId);

        assertEquals(
                0,
                result.size());

        verify(
                timelineAssembler)
                .assemble(
                        List.of(),
                        List.of());
    }

    @Test
    void shouldThrowExceptionWhenCampaignDoesNotExist() {

        Long campaignId = 999L;

        when(
                campaignRepository.existsById(
                        campaignId))
                .thenReturn(
                        false);

        assertThrows(
                CampaignNotFoundException.class,
                () -> campaignTimelineService
                        .getTimeline(
                                campaignId));

        verify(
                campaignExecutionRepository,
                never())
                .findByCampaignId(
                        org.mockito.ArgumentMatchers.anyLong());

        verify(
                simulationRecordRepository,
                never())
                .findByPatrolCampaignId(
                        org.mockito.ArgumentMatchers.anyLong());

        verify(
                timelineAssembler,
                never())
                .assemble(
                        org.mockito.ArgumentMatchers.anyList(),
                        org.mockito.ArgumentMatchers.anyList());
    }

}
