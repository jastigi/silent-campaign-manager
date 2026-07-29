package com.jastigi.silentcampaignmanager.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
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

import com.jastigi.silentcampaignmanager.dto.SimulationHistoryResponseDTO;
import com.jastigi.silentcampaignmanager.exception.PatrolNotFoundException;
import com.jastigi.silentcampaignmanager.security.JwtService;
import com.jastigi.silentcampaignmanager.service.simulation.history.SimulationHistoryService;

@WebMvcTest(controllers = SimulationHistoryController.class, excludeAutoConfiguration = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
class SimulationHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SimulationHistoryService simulationHistoryService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void shouldReturnPagedSimulationHistory()
            throws Exception {

        SimulationHistoryResponseDTO dto = createHistoryDTO();

        Page<SimulationHistoryResponseDTO> page = new PageImpl<>(
                List.of(dto));

        when(
                simulationHistoryService.getHistory(
                        org.mockito.ArgumentMatchers.any(
                                Pageable.class)))
                .thenReturn(
                        page);

        mockMvc.perform(
                get("/api/v1/simulations/history"))
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$.content")
                                .isArray())
                .andExpect(
                        jsonPath("$.content[0].id")
                                .value(10))
                .andExpect(
                        jsonPath("$.content[0].patrolId")
                                .value(1))
                .andExpect(
                        jsonPath("$.content[0].patrolName")
                                .value(
                                        "North Atlantic Patrol"))
                .andExpect(
                        jsonPath("$.content[0].missionOutcome")
                                .value(
                                        "SUCCESS"))
                .andExpect(
                        jsonPath("$.content[0].missionScore")
                                .value(100))
                .andExpect(
                        jsonPath("$.content[0].finalState")
                                .value(
                                        "COMPLETED"))
                .andExpect(
                        jsonPath("$.content[0].contactsDetected")
                                .value(2))
                .andExpect(
                        jsonPath("$.content[0].contactsLost")
                                .value(0))
                .andExpect(
                        jsonPath("$.content[0].intelligenceGathered")
                                .value(1))
                .andExpect(
                        jsonPath("$.content[0].incidents")
                                .value(0))
                .andExpect(
                        jsonPath("$.content[0].completionDate")
                                .value(
                                        "1985-04-12"))
                .andExpect(
                        jsonPath("$.content[0].recordedAt")
                                .value(
                                        "2026-07-29T18:23:00"))
                .andExpect(
                        jsonPath("$.content[0].reportSummary")
                                .value(
                                        "Mission completed successfully."))
                .andExpect(
                        jsonPath("$.content[0].missionDebrief")
                                .value(
                                        "Useful intelligence was gathered."));

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(
                Pageable.class);

        verify(
                simulationHistoryService)
                .getHistory(
                        pageableCaptor.capture());

        Pageable pageable = pageableCaptor.getValue();

        org.junit.jupiter.api.Assertions.assertEquals(
                0,
                pageable.getPageNumber());

        org.junit.jupiter.api.Assertions.assertEquals(
                10,
                pageable.getPageSize());
    }

    @Test
    void shouldReturnPagedSimulationHistoryByPatrol()
            throws Exception {

        Long patrolId = 1L;

        Page<SimulationHistoryResponseDTO> page = new PageImpl<>(
                List.of(
                        createHistoryDTO()));

        when(
                simulationHistoryService
                        .getHistoryByPatrol(
                                eq(patrolId),
                                org.mockito.ArgumentMatchers.any(
                                        Pageable.class)))
                .thenReturn(
                        page);

        mockMvc.perform(
                get(
                        "/api/v1/patrols/{patrolId}/simulations",
                        patrolId)
                        .param(
                                "page",
                                "0")
                        .param(
                                "size",
                                "5"))
                .andExpect(
                        status().isOk())
                .andExpect(
                        jsonPath("$.content[0].patrolId")
                                .value(1))
                .andExpect(
                        jsonPath("$.content[0].missionOutcome")
                                .value(
                                        "SUCCESS"));

        verify(
                simulationHistoryService)
                .getHistoryByPatrol(
                        eq(patrolId),
                        org.mockito.ArgumentMatchers.any(
                                Pageable.class));
    }

    @Test
    void shouldReturnNotFoundWhenPatrolDoesNotExist()
            throws Exception {

        Long patrolId = 999L;

        when(
                simulationHistoryService
                        .getHistoryByPatrol(
                                eq(patrolId),
                                org.mockito.ArgumentMatchers.any(
                                        Pageable.class)))
                .thenThrow(
                        new PatrolNotFoundException(
                                patrolId));

        mockMvc.perform(
                get(
                        "/api/v1/patrols/{patrolId}/simulations",
                        patrolId))
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
                                        "Patrol not found with id: 999"))
                .andExpect(
                        jsonPath("$.path")
                                .value(
                                        "/api/v1/patrols/999/simulations"));
    }

    private SimulationHistoryResponseDTO createHistoryDTO() {

        return SimulationHistoryResponseDTO.builder()
                .id(10L)
                .patrolId(1L)
                .patrolName(
                        "North Atlantic Patrol")
                .missionOutcome(
                        "SUCCESS")
                .missionScore(100)
                .finalState(
                        "COMPLETED")
                .contactsDetected(2)
                .contactsLost(0)
                .intelligenceGathered(1)
                .incidents(0)
                .completionDate(
                        LocalDate.of(
                                1985,
                                4,
                                12))
                .recordedAt(
                        LocalDateTime.of(
                                2026,
                                7,
                                29,
                                18,
                                23))
                .reportSummary(
                        "Mission completed successfully.")
                .missionDebrief(
                        "Useful intelligence was gathered.")
                .build();
    }

}
