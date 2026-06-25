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

import com.jastigi.silentcampaignmanager.dto.PatrolRequestDTO;
import com.jastigi.silentcampaignmanager.dto.PatrolResponseDTO;
import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.CampaignStatus;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;
import com.jastigi.silentcampaignmanager.entity.Submarine;
import com.jastigi.silentcampaignmanager.entity.SubmarineClass;
import com.jastigi.silentcampaignmanager.entity.SubmarineStatus;
import com.jastigi.silentcampaignmanager.entity.SubmarineType;
import com.jastigi.silentcampaignmanager.exception.CampaignNotFoundException;
import com.jastigi.silentcampaignmanager.exception.PatrolNotFoundException;
import com.jastigi.silentcampaignmanager.exception.SubmarineNotFoundException;
import com.jastigi.silentcampaignmanager.repository.CampaignRepository;
import com.jastigi.silentcampaignmanager.repository.PatrolRepository;
import com.jastigi.silentcampaignmanager.repository.SubmarineRepository;
import com.jastigi.silentcampaignmanager.service.impl.PatrolServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PatrolServiceImplTest {

    @Mock
    private PatrolRepository patrolRepository;

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private SubmarineRepository submarineRepository;

    @InjectMocks
    private PatrolServiceImpl patrolService;

    @Test
    void shouldCreatePatrolSuccessfully() {

        Long campaignId = 1L;

        PatrolRequestDTO request = new PatrolRequestDTO();
        request.setPatrolName("North Atlantic Transit");
        request.setPatrolDate(LocalDate.of(2026, 6, 12));
        request.setArea("North Atlantic");
        request.setResult(PatrolResult.SUCCESS);
        request.setSubmarineId(1L);

        Campaign campaign = new Campaign();
        campaign.setId(campaignId);
        campaign.setName("Op. Atlantic Sentinel");
        campaign.setStatus(CampaignStatus.ACTIVE);

        Submarine submarine = new Submarine();
        submarine.setId(1L);
        submarine.setName("USS Dallas");
        submarine.setNation("USA");
        submarine.setSubmarineClass(SubmarineClass.LOS_ANGELES);
        submarine.setSubmarineType(SubmarineType.SSN);
        submarine.setStatus(SubmarineStatus.ACTIVE);

        Patrol savedPatrol = new Patrol();
        savedPatrol.setId(1L);
        savedPatrol.setPatrolName(request.getPatrolName());
        savedPatrol.setPatrolDate(request.getPatrolDate());
        savedPatrol.setArea(request.getArea());
        savedPatrol.setResult(request.getResult());
        savedPatrol.setCampaign(campaign);
        savedPatrol.setSubmarine(submarine);

        when(campaignRepository.findById(campaignId))
                .thenReturn(Optional.of(campaign));
        when(submarineRepository.findById(1L))
                .thenReturn(Optional.of(submarine));
        when(patrolRepository.save(any(Patrol.class)))
                .thenReturn(savedPatrol);

        PatrolResponseDTO result = patrolService.createPatrol(
                campaignId, request);

        assertEquals(1L, result.getId());
        assertEquals("North Atlantic Transit", result.getPatrolName());
        assertEquals(PatrolResult.SUCCESS, result.getResult());
        assertEquals(campaignId, result.getCampaignId());
        assertEquals(1L, result.getSubmarineId());
        assertEquals("USS Dallas", result.getSubmarineName());

        verify(campaignRepository).findById(campaignId);
        verify(submarineRepository).findById(1L);
        verify(patrolRepository).save(any(Patrol.class));
    }

    @Test
    void shouldThrowExceptionWhenCampaignNotFoundOnCreate() {

        Long campaignId = 999L;
        PatrolRequestDTO request = new PatrolRequestDTO();
        request.setSubmarineId(1L);

        when(campaignRepository.findById(campaignId))
                .thenReturn(Optional.empty());

        assertThrows(
                CampaignNotFoundException.class,
                () -> patrolService.createPatrol(
                        campaignId, request));
    }

    @Test
    void shouldThrowExceptionWhenSubmarineNotFoundOnCreate() {

        Long campaignId = 1L;
        PatrolRequestDTO request = new PatrolRequestDTO();
        request.setSubmarineId(999L);

        when(campaignRepository.findById(campaignId))
                .thenReturn(Optional.of(new Campaign()));
        when(submarineRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                SubmarineNotFoundException.class,
                () -> patrolService.createPatrol(
                        campaignId, request));
    }

    @Test
    void shouldReturnPatrolsByCampaign() {

        Long campaignId = 1L;

        Patrol patrol = new Patrol();
        patrol.setId(1L);
        patrol.setPatrolName("North Atlantic Transit");
        patrol.setResult(PatrolResult.SUCCESS);

        when(patrolRepository.findByCampaignId(campaignId))
                .thenReturn(List.of(patrol));

        List<PatrolResponseDTO> result = patrolService
                .getPatrolsByCampaign(campaignId);

        assertEquals(1, result.size());
        assertEquals(
                "North Atlantic Transit",
                result.getFirst().getPatrolName());

        verify(patrolRepository)
                .findByCampaignId(campaignId);
    }

    @Test
    void shouldReturnPatrolById() {

        Patrol patrol = new Patrol();
        patrol.setId(1L);
        patrol.setPatrolName("North Atlantic Transit");
        patrol.setResult(PatrolResult.SUCCESS);

        when(patrolRepository.findById(1L))
                .thenReturn(Optional.of(patrol));

        PatrolResponseDTO result = patrolService
                .getPatrolById(1L);

        assertEquals(1L, result.getId());
        assertEquals(
                "North Atlantic Transit",
                result.getPatrolName());

        verify(patrolRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenPatrolNotFound() {

        when(patrolRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                PatrolNotFoundException.class,
                () -> patrolService.getPatrolById(999L));
    }

    @Test
    void shouldUpdatePatrolSuccessfully() {

        Patrol existingPatrol = new Patrol();
        existingPatrol.setId(1L);
        existingPatrol.setPatrolName("Old Name");
        existingPatrol.setPatrolDate(LocalDate.of(2026, 1, 1));
        existingPatrol.setArea("Old Area");
        existingPatrol.setResult(PatrolResult.FAILURE);

        PatrolRequestDTO request = new PatrolRequestDTO();
        request.setPatrolName("Updated Name");
        request.setPatrolDate(LocalDate.of(2026, 6, 15));
        request.setArea("Updated Area");
        request.setResult(PatrolResult.SUCCESS);
        request.setSubmarineId(1L);

        Patrol updatedPatrol = new Patrol();
        updatedPatrol.setId(1L);
        updatedPatrol.setPatrolName(request.getPatrolName());
        updatedPatrol.setPatrolDate(request.getPatrolDate());
        updatedPatrol.setArea(request.getArea());
        updatedPatrol.setResult(request.getResult());

        when(patrolRepository.findById(1L))
                .thenReturn(Optional.of(existingPatrol));
        when(patrolRepository.save(any(Patrol.class)))
                .thenReturn(updatedPatrol);

        PatrolResponseDTO result = patrolService
                .updatePatrol(1L, request);

        assertEquals("Updated Name", result.getPatrolName());
        assertEquals(
                LocalDate.of(2026, 6, 15),
                result.getPatrolDate());
        assertEquals(PatrolResult.SUCCESS, result.getResult());

        verify(patrolRepository).findById(1L);
        verify(patrolRepository).save(any(Patrol.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentPatrol() {

        PatrolRequestDTO request = new PatrolRequestDTO();

        when(patrolRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                PatrolNotFoundException.class,
                () -> patrolService.updatePatrol(999L, request));
    }

    @Test
    void shouldDeletePatrolSuccessfully() {

        Patrol patrol = new Patrol();
        patrol.setId(1L);

        when(patrolRepository.findById(1L))
                .thenReturn(Optional.of(patrol));

        patrolService.deletePatrol(1L);

        verify(patrolRepository).findById(1L);
        verify(patrolRepository).delete(patrol);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentPatrol() {

        when(patrolRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                PatrolNotFoundException.class,
                () -> patrolService.deletePatrol(999L));
    }

}
