package com.jastigi.silentcampaignmanager.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jastigi.silentcampaignmanager.dto.PatrolReportDTO;
import com.jastigi.silentcampaignmanager.dto.PatrolRequestDTO;
import com.jastigi.silentcampaignmanager.dto.PatrolResponseDTO;
import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.MissionStatus;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolEvent;
import com.jastigi.silentcampaignmanager.entity.Submarine;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;
import com.jastigi.silentcampaignmanager.exception.CampaignNotFoundException;
import com.jastigi.silentcampaignmanager.exception.PatrolNotFoundException;
import com.jastigi.silentcampaignmanager.exception.SubmarineNotFoundException;
import com.jastigi.silentcampaignmanager.mapper.PatrolMapper;
import com.jastigi.silentcampaignmanager.repository.CampaignRepository;
import com.jastigi.silentcampaignmanager.repository.ContactRepository;
import com.jastigi.silentcampaignmanager.repository.PatrolEventRepository;
import com.jastigi.silentcampaignmanager.repository.PatrolRepository;
import com.jastigi.silentcampaignmanager.repository.SubmarineRepository;
import com.jastigi.silentcampaignmanager.service.PatrolService;
import com.jastigi.silentcampaignmanager.service.report.PatrolReportGenerator;

@Service
public class PatrolServiceImpl implements PatrolService {

        private final PatrolRepository patrolRepository;
        private final CampaignRepository campaignRepository;
        private final SubmarineRepository submarineRepository;
        private final ContactRepository contactRepository;
        private final PatrolEventRepository patrolEventRepository;
        private final PatrolReportGenerator patrolReportGenerator;

        public PatrolServiceImpl(
                        PatrolRepository patrolRepository,
                        CampaignRepository campaignRepository,
                        SubmarineRepository submarineRepository,
                        ContactRepository contactRepository,
                        PatrolEventRepository patrolEventRepository,
                        PatrolReportGenerator patrolReportGenerator) {

                this.patrolRepository = patrolRepository;
                this.campaignRepository = campaignRepository;
                this.submarineRepository = submarineRepository;
                this.contactRepository = contactRepository;
                this.patrolEventRepository = patrolEventRepository;
                this.patrolReportGenerator = patrolReportGenerator;
        }

        @Override
        public PatrolResponseDTO createPatrol(
                        Long campaignId,
                        PatrolRequestDTO request) {

                Campaign campaign = campaignRepository.findById(campaignId)
                                .orElseThrow(() -> new CampaignNotFoundException(
                                                campaignId));

                Patrol patrol = PatrolMapper.toEntity(request);

                Submarine submarine = submarineRepository.findById(
                                request.getSubmarineId())
                                .orElseThrow(() -> new SubmarineNotFoundException(
                                                request.getSubmarineId()));

                patrol.setCampaign(campaign);

                patrol.setSubmarine(submarine);

                Patrol savedPatrol = patrolRepository.save(patrol);

                return PatrolMapper.toDTO(savedPatrol);
        }

        @Override
        public List<PatrolResponseDTO> getPatrolsByCampaign(
                        Long campaignId) {

                return patrolRepository
                                .findByCampaignId(campaignId)
                                .stream()
                                .map(PatrolMapper::toDTO)
                                .toList();
        }

        @Override
        public PatrolResponseDTO getPatrolById(
                        Long id) {

                Patrol patrol = patrolRepository.findById(id)
                                .orElseThrow(() -> new PatrolNotFoundException(id));

                return PatrolMapper.toDTO(patrol);
        }

        @Override
        public PatrolResponseDTO updatePatrol(
                        Long id,
                        PatrolRequestDTO request) {

                Patrol patrol = patrolRepository.findById(id)
                                .orElseThrow(() -> new PatrolNotFoundException(id));

                patrol.setPatrolName(request.getPatrolName());
                patrol.setPatrolDate(request.getPatrolDate());
                patrol.setArea(request.getArea());
                patrol.setResult(request.getResult());

                Patrol updatedPatrol = patrolRepository.save(patrol);

                return PatrolMapper.toDTO(updatedPatrol);
        }

        @Override
        public void deletePatrol(Long id) {

                Patrol patrol = patrolRepository.findById(id)
                                .orElseThrow(() -> new PatrolNotFoundException(id));

                patrolRepository.delete(patrol);
        }

        @Override
        public PatrolReportDTO generatePatrolReport(
                        Long patrolId) {

                Patrol patrol = patrolRepository.findById(patrolId)
                                .orElseThrow(() -> new PatrolNotFoundException(
                                                patrolId));

                List<Contact> contacts = contactRepository.findByPatrolId(
                                patrolId);

                List<PatrolEvent> events = patrolEventRepository.findByPatrolId(
                                patrolId);

                return patrolReportGenerator.generate(

                                patrol,

                                contacts,

                                events);

        }

}
