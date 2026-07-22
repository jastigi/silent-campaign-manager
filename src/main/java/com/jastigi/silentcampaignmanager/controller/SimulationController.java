package com.jastigi.silentcampaignmanager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jastigi.silentcampaignmanager.dto.SimulationResultDTO;
import com.jastigi.silentcampaignmanager.mapper.SimulationMapper;
import com.jastigi.silentcampaignmanager.service.simulation.SimulationService;
import com.jastigi.silentcampaignmanager.service.simulation.result.SimulationResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SimulationController {

    private final SimulationService simulationService;

    private final SimulationMapper simulationMapper;

    @PostMapping("/patrols/{id}/simulate")
    public ResponseEntity<SimulationResultDTO> simulatePatrol(
            @PathVariable Long id) {

        SimulationResult result = simulationService.simulate(id);

        SimulationResultDTO response = simulationMapper.toDto(result);

        return ResponseEntity.ok(response);

    }

}
