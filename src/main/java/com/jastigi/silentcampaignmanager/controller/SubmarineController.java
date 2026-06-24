package com.jastigi.silentcampaignmanager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.jastigi.silentcampaignmanager.dto.SubmarineRequestDTO;
import com.jastigi.silentcampaignmanager.dto.SubmarineResponseDTO;
import com.jastigi.silentcampaignmanager.service.SubmarineService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/submarines")
public class SubmarineController {

    private final SubmarineService submarineService;

    public SubmarineController(
            SubmarineService submarineService) {

        this.submarineService = submarineService;
    }

    @PostMapping
    public SubmarineResponseDTO createSubmarine(
            @Valid @RequestBody SubmarineRequestDTO request) {

        return submarineService.createSubmarine(
                request);
    }

    @GetMapping
    public List<SubmarineResponseDTO> getAllSubmarines() {

        return submarineService
                .getAllSubmarines();
    }

    @GetMapping("/{id}")
    public SubmarineResponseDTO getSubmarineById(
            @PathVariable Long id) {

        return submarineService
                .getSubmarineById(id);
    }

    @PutMapping("/{id}")
    public SubmarineResponseDTO updateSubmarine(
            @PathVariable Long id,
            @Valid @RequestBody SubmarineRequestDTO request) {

        return submarineService
                .updateSubmarine(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteSubmarine(
            @PathVariable Long id) {

        submarineService.deleteSubmarine(id);
    }

}
