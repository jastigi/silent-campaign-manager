package com.jastigi.silentcampaignmanager.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jastigi.silentcampaignmanager.dto.PatrolRequestDTO;
import com.jastigi.silentcampaignmanager.dto.PatrolResponseDTO;
import com.jastigi.silentcampaignmanager.service.PatrolService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/patrols")
public class PatrolManagementController {

    private final PatrolService patrolService;

    public PatrolManagementController(
            PatrolService patrolService) {

        this.patrolService = patrolService;
    }

    @GetMapping("/{id}")
    public PatrolResponseDTO getPatrolById(
            @PathVariable Long id) {

        return patrolService.getPatrolById(id);
    }

    @PutMapping("/{id}")
    public PatrolResponseDTO updatePatrol(
            @PathVariable Long id,
            @Valid @RequestBody PatrolRequestDTO request) {

        return patrolService.updatePatrol(
                id,
                request);
    }

    @DeleteMapping("/{id}")
    public void deletePatrol(
            @PathVariable Long id) {

        patrolService.deletePatrol(id);
    }

}
