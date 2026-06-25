package com.jastigi.silentcampaignmanager.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jastigi.silentcampaignmanager.dto.PatrolEventRequestDTO;
import com.jastigi.silentcampaignmanager.dto.PatrolEventResponseDTO;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolEvent;
import com.jastigi.silentcampaignmanager.exception.PatrolEventNotFoundException;
import com.jastigi.silentcampaignmanager.exception.PatrolNotFoundException;
import com.jastigi.silentcampaignmanager.mapper.PatrolEventMapper;
import com.jastigi.silentcampaignmanager.repository.PatrolEventRepository;
import com.jastigi.silentcampaignmanager.repository.PatrolRepository;
import com.jastigi.silentcampaignmanager.service.PatrolEventService;

@Service
public class PatrolEventServiceImpl implements PatrolEventService {

    private final PatrolEventRepository patrolEventRepository;
    private final PatrolRepository patrolRepository;

    public PatrolEventServiceImpl(PatrolEventRepository patrolEventRepository, PatrolRepository patrolRepository) {
        this.patrolEventRepository = patrolEventRepository;
        this.patrolRepository = patrolRepository;
    }

    @Override
    public PatrolEventResponseDTO createEvent(
            Long patrolId,
            PatrolEventRequestDTO request) {

        Patrol patrol = patrolRepository.findById(patrolId)
                .orElseThrow(() -> new PatrolNotFoundException(
                        patrolId));

        PatrolEvent event = PatrolEventMapper.toEntity(request);

        event.setPatrol(patrol);

        PatrolEvent saved = patrolEventRepository.save(event);

        return PatrolEventMapper.toDTO(saved);
    }

    @Override
    public List<PatrolEventResponseDTO> getEventsByPatrol(Long patrolId) {

        return patrolEventRepository
                .findByPatrolId(patrolId)
                .stream()
                .map(PatrolEventMapper::toDTO)
                .toList();

    }

    @Override
    public PatrolEventResponseDTO getEventById(Long id) {

        PatrolEvent event = patrolEventRepository.findById(id)
                .orElseThrow(() -> new PatrolEventNotFoundException(id));

        return PatrolEventMapper.toDTO(event);

    }

    @Override
    public PatrolEventResponseDTO updateEvent(
            Long id,
            PatrolEventRequestDTO request) {

        PatrolEvent event = patrolEventRepository.findById(id)
                .orElseThrow(() -> new PatrolEventNotFoundException(id));

        event.setEventType(
                request.getEventType());

        event.setEventDate(
                request.getEventDate());

        event.setDescription(
                request.getDescription());

        event.setSeverity(
                request.getSeverity());

        PatrolEvent updated = patrolEventRepository.save(event);

        return PatrolEventMapper.toDTO(updated);

    }

    @Override
    public void deleteEvent(Long id) {

        PatrolEvent event = patrolEventRepository.findById(id)
                .orElseThrow(() -> new PatrolEventNotFoundException(id));

        patrolEventRepository.delete(event);

    }

}
