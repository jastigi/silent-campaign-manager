package com.jastigi.silentcampaignmanager.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.dto.PatrolEventRequestDTO;
import com.jastigi.silentcampaignmanager.dto.PatrolEventResponseDTO;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolEvent;
import com.jastigi.silentcampaignmanager.entity.PatrolEventType;
import com.jastigi.silentcampaignmanager.entity.MissionType;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;
import com.jastigi.silentcampaignmanager.exception.PatrolEventNotFoundException;
import com.jastigi.silentcampaignmanager.exception.PatrolNotFoundException;
import com.jastigi.silentcampaignmanager.repository.PatrolEventRepository;
import com.jastigi.silentcampaignmanager.repository.PatrolRepository;
import com.jastigi.silentcampaignmanager.service.impl.PatrolEventServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PatrolEventServiceImplTest {

    @Mock
    private PatrolEventRepository patrolEventRepository;

    @Mock
    private PatrolRepository patrolRepository;

    @InjectMocks
    private PatrolEventServiceImpl patrolEventService;

    @Test
    void shouldCreateEventSuccessfully() {

        Long patrolId = 1L;

        PatrolEventRequestDTO request = new PatrolEventRequestDTO();
        request.setEventType(PatrolEventType.SOSUS_DETECTION);
        request.setEventDate(LocalDate.of(2026, 6, 12));
        request.setDescription("Contact Sierra-01 detected on SOSUS array");
        request.setSeverity(7);

        Patrol patrol = new Patrol();
        patrol.setId(patrolId);
        patrol.setPatrolName("North Atlantic Transit");
        patrol.setResult(PatrolResult.SUCCESS);
        patrol.setMissionType(MissionType.DETERRENCE_PATROL);

        PatrolEvent savedEvent = new PatrolEvent();
        savedEvent.setId(1L);
        savedEvent.setEventType(request.getEventType());
        savedEvent.setEventDate(request.getEventDate());
        savedEvent.setDescription(request.getDescription());
        savedEvent.setSeverity(request.getSeverity());
        savedEvent.setPatrol(patrol);

        when(patrolRepository.findById(patrolId))
                .thenReturn(Optional.of(patrol));
        when(patrolEventRepository.save(any(PatrolEvent.class)))
                .thenReturn(savedEvent);

        PatrolEventResponseDTO result = patrolEventService
                .createEvent(patrolId, request);

        assertEquals(1L, result.getId());
        assertEquals(PatrolEventType.SOSUS_DETECTION,
                result.getEventType());
        assertEquals(7, result.getSeverity());
        assertEquals(patrolId, result.getPatrolId());

        verify(patrolRepository).findById(patrolId);
        verify(patrolEventRepository).save(any(PatrolEvent.class));
    }

    @Test
    void shouldThrowExceptionWhenPatrolNotFoundOnCreate() {

        Long patrolId = 999L;
        PatrolEventRequestDTO request = new PatrolEventRequestDTO();

        when(patrolRepository.findById(patrolId))
                .thenReturn(Optional.empty());

        assertThrows(
                PatrolNotFoundException.class,
                () -> patrolEventService.createEvent(
                        patrolId, request));
    }

    @Test
    void shouldReturnEventsByPatrol() {

        Long patrolId = 1L;

        PatrolEvent event = new PatrolEvent();
        event.setId(1L);
        event.setEventType(PatrolEventType.SOSUS_DETECTION);
        event.setSeverity(7);

        when(patrolEventRepository.findByPatrolId(patrolId))
                .thenReturn(List.of(event));

        List<PatrolEventResponseDTO> result = patrolEventService
                .getEventsByPatrol(patrolId);

        assertEquals(1, result.size());
        assertEquals(
                PatrolEventType.SOSUS_DETECTION,
                result.getFirst().getEventType());

        verify(patrolEventRepository)
                .findByPatrolId(patrolId);
    }

    @Test
    void shouldReturnEventById() {

        PatrolEvent event = new PatrolEvent();
        event.setId(1L);
        event.setEventType(PatrolEventType.SOSUS_DETECTION);
        event.setEventDate(LocalDate.of(2026, 6, 12));
        event.setSeverity(7);

        when(patrolEventRepository.findById(1L))
                .thenReturn(Optional.of(event));

        PatrolEventResponseDTO result = patrolEventService
                .getEventById(1L);

        assertEquals(1L, result.getId());
        assertEquals(PatrolEventType.SOSUS_DETECTION,
                result.getEventType());

        verify(patrolEventRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenEventNotFound() {

        when(patrolEventRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                PatrolEventNotFoundException.class,
                () -> patrolEventService.getEventById(999L));
    }

    @Test
    void shouldUpdateEventSuccessfully() {

        PatrolEvent existingEvent = new PatrolEvent();
        existingEvent.setId(1L);
        existingEvent.setEventType(PatrolEventType.STORM_CONDITIONS);
        existingEvent.setEventDate(LocalDate.of(2026, 1, 1));
        existingEvent.setDescription("Old description");
        existingEvent.setSeverity(3);

        PatrolEventRequestDTO request = new PatrolEventRequestDTO();
        request.setEventType(PatrolEventType.MISSILE_LAUNCH);
        request.setEventDate(LocalDate.of(2026, 6, 15));
        request.setDescription("Enemy missile launch detected");
        request.setSeverity(10);

        PatrolEvent updatedEvent = new PatrolEvent();
        updatedEvent.setId(1L);
        updatedEvent.setEventType(request.getEventType());
        updatedEvent.setEventDate(request.getEventDate());
        updatedEvent.setDescription(request.getDescription());
        updatedEvent.setSeverity(request.getSeverity());

        when(patrolEventRepository.findById(1L))
                .thenReturn(Optional.of(existingEvent));
        when(patrolEventRepository.save(any(PatrolEvent.class)))
                .thenReturn(updatedEvent);

        PatrolEventResponseDTO result = patrolEventService
                .updateEvent(1L, request);

        assertEquals(PatrolEventType.MISSILE_LAUNCH,
                result.getEventType());
        assertEquals(10, result.getSeverity());
        assertEquals(
                "Enemy missile launch detected",
                result.getDescription());

        verify(patrolEventRepository).findById(1L);
        verify(patrolEventRepository).save(any(PatrolEvent.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentEvent() {

        PatrolEventRequestDTO request = new PatrolEventRequestDTO();

        when(patrolEventRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                PatrolEventNotFoundException.class,
                () -> patrolEventService.updateEvent(
                        999L, request));
    }

    @Test
    void shouldDeleteEventSuccessfully() {

        PatrolEvent event = new PatrolEvent();
        event.setId(1L);

        when(patrolEventRepository.findById(1L))
                .thenReturn(Optional.of(event));

        patrolEventService.deleteEvent(1L);

        verify(patrolEventRepository).findById(1L);
        verify(patrolEventRepository).delete(event);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentEvent() {

        when(patrolEventRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                PatrolEventNotFoundException.class,
                () -> patrolEventService.deleteEvent(999L));
    }

}
