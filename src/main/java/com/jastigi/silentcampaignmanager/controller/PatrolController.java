package com.jastigi.silentcampaignmanager.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jastigi.silentcampaignmanager.dto.ContactResponseDTO;
import com.jastigi.silentcampaignmanager.dto.MissionEvaluationResponseDTO;
import com.jastigi.silentcampaignmanager.dto.PatrolReportDTO;
import com.jastigi.silentcampaignmanager.dto.PatrolRequestDTO;
import com.jastigi.silentcampaignmanager.dto.PatrolResponseDTO;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;
import com.jastigi.silentcampaignmanager.service.PatrolService;
import com.jastigi.silentcampaignmanager.mapper.MissionEvaluationMapper;
import com.jastigi.silentcampaignmanager.service.missions.model.MissionEvaluationResult;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/campaigns/{campaignId}/patrols")
public class PatrolController {

        private final PatrolService patrolService;

        public PatrolController(
                        PatrolService patrolService) {

                this.patrolService = patrolService;
        }

        @PostMapping
        public PatrolResponseDTO createPatrol(
                        @PathVariable Long campaignId,
                        @Valid @RequestBody PatrolRequestDTO request) {

                return patrolService.createPatrol(
                                campaignId,
                                request);
        }

        @GetMapping
        public List<PatrolResponseDTO> getPatrolsByCampaign(
                        @PathVariable Long campaignId) {

                return patrolService.getPatrolsByCampaign(
                                campaignId);
        }

        @GetMapping("/{id}/report")
        public ResponseEntity<PatrolReportDTO> getPatrolReport(
                        @PathVariable Long id) {

                return ResponseEntity.ok(
                                patrolService.generatePatrolReport(id));
        }

        @GetMapping("/{id}/contacts")
        public ResponseEntity<List<ContactResponseDTO>> getContacts(
                        @PathVariable Long id) {

                return ResponseEntity.ok(
                                patrolService.getContacts(id));

        }

        @PatchMapping("/{id}/close")
        public ResponseEntity<PatrolResponseDTO> closePatrol(
                        @PathVariable Long id) {

                return ResponseEntity.ok(
                                patrolService.closePatrol(id));
        }

        @GetMapping("/{id}/evaluation")
        public ResponseEntity<MissionEvaluationResponseDTO> getMissionEvaluation(
                        @PathVariable Long campaignId,
                        @PathVariable Long id) {

                MissionEvaluationResult result = patrolService.getMissionEvaluation(id);
                return ResponseEntity.ok(
                                MissionEvaluationMapper.toDTO(result));
        }

        @GetMapping("/paged")
        public ResponseEntity<Page<PatrolResponseDTO>> getPatrolsPaged(
                        @PathVariable Long campaignId,

                        @ParameterObject @PageableDefault(size = 10) Pageable pageable) {

                return ResponseEntity.ok(
                                patrolService.getPatrols(
                                                campaignId,
                                                pageable));
        }

        @GetMapping("/search")
        public ResponseEntity<Page<PatrolResponseDTO>> searchPatrols(

                        @PathVariable Long campaignId,

                        @RequestParam PatrolResult result,

                        @ParameterObject @PageableDefault(size = 10) Pageable pageable) {

                return ResponseEntity.ok(
                                patrolService.searchPatrols(
                                                campaignId,
                                                result,
                                                pageable));
        }

}
