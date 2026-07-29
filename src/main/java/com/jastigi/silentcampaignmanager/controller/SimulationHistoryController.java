package com.jastigi.silentcampaignmanager.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jastigi.silentcampaignmanager.dto.SimulationHistoryResponseDTO;
import com.jastigi.silentcampaignmanager.service.simulation.history.SimulationHistoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SimulationHistoryController {

    private final SimulationHistoryService simulationHistoryService;

    @GetMapping("/simulations/history")
    public ResponseEntity<Page<SimulationHistoryResponseDTO>> getSimulationHistory(
            @ParameterObject @PageableDefault(size = 10, sort = "recordedAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<SimulationHistoryResponseDTO> history = simulationHistoryService.getHistory(
                pageable);

        return ResponseEntity.ok(
                history);
    }

    @GetMapping("/patrols/{patrolId}/simulations")
    public ResponseEntity<Page<SimulationHistoryResponseDTO>> getSimulationHistoryByPatrol(
            @PathVariable Long patrolId,
            @ParameterObject @PageableDefault(size = 10, sort = "recordedAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<SimulationHistoryResponseDTO> history = simulationHistoryService
                .getHistoryByPatrol(
                        patrolId,
                        pageable);

        return ResponseEntity.ok(
                history);
    }

}