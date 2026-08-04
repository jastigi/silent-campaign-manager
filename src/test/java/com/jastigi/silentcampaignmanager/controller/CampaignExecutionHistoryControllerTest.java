package com.jastigi.silentcampaignmanager.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jastigi.silentcampaignmanager.dto.CampaignExecutionResponseDTO;
import com.jastigi.silentcampaignmanager.exception.CampaignNotFoundException;
import com.jastigi.silentcampaignmanager.security.JwtService;
import com.jastigi.silentcampaignmanager.service.campaign.execution.history.CampaignExecutionHistoryService;

@WebMvcTest(controllers = CampaignExecutionHistoryController.class, excludeAutoConfiguration = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
class CampaignExecutionHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CampaignExecutionHistoryService campaignExecutionHistoryService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void shouldReturnPagedCampaignExecutionHistory()
            throws Exception {

        Long campaignId = 1L;

        CampaignExecutionResponseDTO dto = createExecutionDTO();

        Page<CampaignExecutionResponseDTO> page = new PageImpl<>(
                List.of(
                        dto));

        when(
                campaignExecutionHistoryService
                        .getHistoryByCampaign(
                                eq(campaignId),
                                org.mockito.ArgumentMatchers.any(
                                        Pageable.class)))
                .thenReturn(
                        page);

        mockMvc.perform(
                get(
                        "/api/v1/campaigns/{campaignId}/executions",
                        campaignId))
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$.content")
                                .isArray())
                .andExpect(
                        jsonPath("$.content[0].id")
                                .value(10))
                .andExpect(
                        jsonPath("$.content[0].campaignId")
                                .value(1))
                .andExpect(
                        jsonPath("$.content[0].campaignName")
                                .value(
                                        "North Atlantic Campaign"))
                .andExpect(
                        jsonPath("$.content[0].status")
                                .value(
                                        "COMPLETED"))
                .andExpect(
                        jsonPath("$.content[0].totalPatrols")
                                .value(3))
                .andExpect(
                        jsonPath("$.content[0].completedPatrols")
                                .value(3))
                .andExpect(
                        jsonPath("$.content[0].startedAt")
                                .value(
                                        "2026-08-04T10:00:00"))
                .andExpect(
                        jsonPath("$.content[0].completedAt")
                                .value(
                                        "2026-08-04T10:05:00"))
                .andExpect(
                        jsonPath("$.content[0].failureMessage")
                                .doesNotExist());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(
                Pageable.class);

        verify(
                campaignExecutionHistoryService)
                .getHistoryByCampaign(
                        eq(campaignId),
                        pageableCaptor.capture());

        Pageable pageable = pageableCaptor.getValue();

        assertEquals(
                0,
                pageable.getPageNumber());

        assertEquals(
                10,
                pageable.getPageSize());

        assertEquals(
                org.springframework.data.domain.Sort.Direction.DESC,
                pageable.getSort()
                        .getOrderFor(
                                "startedAt")
                        .getDirection());
    }

    @Test
    void shouldRespectRequestedPagination()
            throws Exception {

        Long campaignId = 2L;

        when(
                campaignExecutionHistoryService
                        .getHistoryByCampaign(
                                eq(campaignId),
                                org.mockito.ArgumentMatchers.any(
                                        Pageable.class)))
                .thenReturn(
                        Page.empty());

        mockMvc.perform(
                get(
                        "/api/v1/campaigns/{campaignId}/executions",
                        campaignId)
                        .param(
                                "page",
                                "1")
                        .param(
                                "size",
                                "5"))
                .andExpect(
                        status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(
                Pageable.class);

        verify(
                campaignExecutionHistoryService)
                .getHistoryByCampaign(
                        eq(campaignId),
                        pageableCaptor.capture());

        assertEquals(
                1,
                pageableCaptor
                        .getValue()
                        .getPageNumber());

        assertEquals(
                5,
                pageableCaptor
                        .getValue()
                        .getPageSize());
    }

    @Test
    void shouldReturnNotFoundWhenCampaignDoesNotExist()
            throws Exception {

        Long campaignId = 999L;

        when(
                campaignExecutionHistoryService
                        .getHistoryByCampaign(
                                eq(campaignId),
                                org.mockito.ArgumentMatchers.any(
                                        Pageable.class)))
                .thenThrow(
                        new CampaignNotFoundException(
                                campaignId));

        mockMvc.perform(
                get(
                        "/api/v1/campaigns/{campaignId}/executions",
                        campaignId))
                .andExpect(
                        status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404))
                .andExpect(
                        jsonPath("$.error")
                                .value(
                                        "Not Found"))
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Campaign not found with id: 999"))
                .andExpect(
                        jsonPath("$.path")
                                .value(
                                        "/api/v1/campaigns/999/executions"));
    }

    private CampaignExecutionResponseDTO createExecutionDTO() {

        return CampaignExecutionResponseDTO.builder()
                .id(10L)
                .campaignId(1L)
                .campaignName(
                        "North Atlantic Campaign")
                .status(
                        "COMPLETED")
                .totalPatrols(3)
                .completedPatrols(3)
                .startedAt(
                        LocalDateTime.of(
                                2026,
                                8,
                                4,
                                10,
                                0))
                .completedAt(
                        LocalDateTime.of(
                                2026,
                                8,
                                4,
                                10,
                                5))
                .build();
    }

}
