package com.jastigi.silentcampaignmanager.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jastigi.silentcampaignmanager.dto.SimulationResultDTO;
import com.jastigi.silentcampaignmanager.exception.PatrolNotFoundException;
import com.jastigi.silentcampaignmanager.mapper.SimulationMapper;
import com.jastigi.silentcampaignmanager.service.simulation.SimulationService;
import com.jastigi.silentcampaignmanager.service.simulation.resolver.MissionOutcome;
import com.jastigi.silentcampaignmanager.service.simulation.result.ResolvedSimulationResult;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

@WebMvcTest(controllers = SimulationController.class, excludeAutoConfiguration = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
class SimulationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SimulationService simulationService;

    @MockitoBean
    private SimulationMapper simulationMapper;

    @Test
    void shouldSimulatePatrolSuccessfully()
            throws Exception {

        Long patrolId = 1L;

        SimulationResult simulationResult = SimulationResult.builder()
                .build();

        ResolvedSimulationResult resolvedResult = ResolvedSimulationResult.builder()
                .simulationResult(simulationResult)
                .missionOutcome(
                        MissionOutcome.SUCCESS)
                .missionScore(100)
                .reportSummary(
                        "HUNT_SSN mission completed successfully.")
                .missionDebrief(
                        "Mission successful.")
                .build();

        SimulationResultDTO response = SimulationResultDTO.builder()
                .summary(
                        "HUNT_SSN mission completed successfully.")
                .missionDebrief(
                        "Mission successful.")
                .missionOutcome(
                        "SUCCESS")
                .missionScore(100)
                .finalState(
                        "COMPLETED")
                .completionDate(
                        LocalDate.of(
                                1985,
                                4,
                                12))
                .contactsDetected(1)
                .contactsLost(0)
                .incidents(0)
                .timeline(
                        List.of(
                                "1985-04-12 | PATROL_COMPLETED | Patrol completed."))
                .build();

        when(simulationService.simulate(patrolId))
                .thenReturn(resolvedResult);

        when(simulationMapper.toDto(resolvedResult))
                .thenReturn(response);

        mockMvc.perform(
                post("/api/v1/patrols/{id}/simulate",
                        patrolId))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.summary")
                                .value(
                                        "HUNT_SSN mission completed successfully."))
                .andExpect(
                        jsonPath("$.missionDebrief")
                                .value(
                                        "Mission successful."))
                .andExpect(
                        jsonPath("$.missionOutcome")
                                .value("SUCCESS"))
                .andExpect(
                        jsonPath("$.missionScore")
                                .value(100))
                .andExpect(
                        jsonPath("$.finalState")
                                .value("COMPLETED"))
                .andExpect(
                        jsonPath("$.completionDate")
                                .value("1985-04-12"))
                .andExpect(
                        jsonPath("$.contactsDetected")
                                .value(1))
                .andExpect(
                        jsonPath("$.contactsLost")
                                .value(0))
                .andExpect(
                        jsonPath("$.incidents")
                                .value(0))
                .andExpect(
                        jsonPath("$.timeline")
                                .isArray())
                .andExpect(
                        jsonPath("$.timeline[0]")
                                .value(
                                        "1985-04-12 | PATROL_COMPLETED | Patrol completed."));

        verify(simulationService)
                .simulate(patrolId);

        verify(simulationMapper)
                .toDto(resolvedResult);
    }

    @Test
    void shouldReturnNotFoundWhenPatrolDoesNotExist()
            throws Exception {

        Long patrolId = 999L;

        when(simulationService.simulate(patrolId))
                .thenThrow(
                        new PatrolNotFoundException(
                                patrolId));

        mockMvc.perform(
                post("/api/v1/patrols/{id}/simulate",
                        patrolId))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404))
                .andExpect(
                        jsonPath("$.error")
                                .value("Not Found"))
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Patrol not found with id: 999"))
                .andExpect(
                        jsonPath("$.path")
                                .value(
                                        "/api/v1/patrols/999/simulate"));
    }

}
