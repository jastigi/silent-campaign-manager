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

import com.jastigi.silentcampaignmanager.dto.SubmarineRequestDTO;
import com.jastigi.silentcampaignmanager.dto.SubmarineResponseDTO;
import com.jastigi.silentcampaignmanager.entity.Submarine;
import com.jastigi.silentcampaignmanager.entity.SubmarineClass;
import com.jastigi.silentcampaignmanager.entity.SubmarineStatus;
import com.jastigi.silentcampaignmanager.entity.SubmarineType;
import com.jastigi.silentcampaignmanager.exception.SubmarineNotFoundException;
import com.jastigi.silentcampaignmanager.repository.SubmarineRepository;
import com.jastigi.silentcampaignmanager.service.impl.SubmarineServiceImpl;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SubmarineServiceImplTest {

    @Mock
    private SubmarineRepository submarineRepository;

    @InjectMocks
    private SubmarineServiceImpl submarineService;

    @Test
    void shouldCreateSubmarineSuccessfully() {

        SubmarineRequestDTO request = new SubmarineRequestDTO();
        request.setName("USS Dallas");
        request.setType(SubmarineType.SSN);
        request.setSubmarineClass(SubmarineClass.LOS_ANGELES);
        request.setNation("USA");
        request.setStatus(SubmarineStatus.ACTIVE);

        Submarine savedSubmarine = new Submarine();
        savedSubmarine.setId(1L);
        savedSubmarine.setName(request.getName());
        savedSubmarine.setSubmarineType(request.getType());
        savedSubmarine.setSubmarineClass(request.getSubmarineClass());
        savedSubmarine.setNation(request.getNation());
        savedSubmarine.setStatus(request.getStatus());

        when(submarineRepository.save(any(Submarine.class)))
                .thenReturn(savedSubmarine);

        SubmarineResponseDTO result = submarineService
                .createSubmarine(request);

        assertEquals(1L, result.getId());
        assertEquals("USS Dallas", result.getName());
        assertEquals(SubmarineType.SSN, result.getType());
        assertEquals(SubmarineClass.LOS_ANGELES,
                result.getSubmarineClass());
        assertEquals("USA", result.getNation());
        assertEquals(SubmarineStatus.ACTIVE, result.getStatus());

        verify(submarineRepository).save(any(Submarine.class));
    }

    @Test
    void shouldReturnAllSubmarines() {

        Submarine submarine = new Submarine();
        submarine.setId(1L);
        submarine.setName("USS Dallas");
        submarine.setSubmarineType(SubmarineType.SSN);
        submarine.setSubmarineClass(SubmarineClass.LOS_ANGELES);
        submarine.setNation("USA");
        submarine.setStatus(SubmarineStatus.ACTIVE);

        when(submarineRepository.findAll())
                .thenReturn(List.of(submarine));

        List<SubmarineResponseDTO> result = submarineService
                .getAllSubmarines();

        assertEquals(1, result.size());
        assertEquals("USS Dallas",
                result.getFirst().getName());

        verify(submarineRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoSubmarines() {

        when(submarineRepository.findAll())
                .thenReturn(List.of());

        List<SubmarineResponseDTO> result = submarineService
                .getAllSubmarines();

        assertTrue(result.isEmpty());

        verify(submarineRepository).findAll();
    }

    @Test
    void shouldReturnSubmarineById() {

        Submarine submarine = new Submarine();
        submarine.setId(1L);
        submarine.setName("USS Dallas");
        submarine.setSubmarineType(SubmarineType.SSN);
        submarine.setSubmarineClass(SubmarineClass.LOS_ANGELES);
        submarine.setNation("USA");
        submarine.setStatus(SubmarineStatus.ACTIVE);

        when(submarineRepository.findById(1L))
                .thenReturn(Optional.of(submarine));

        SubmarineResponseDTO result = submarineService
                .getSubmarineById(1L);

        assertEquals(1L, result.getId());
        assertEquals("USS Dallas", result.getName());

        verify(submarineRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenSubmarineNotFound() {

        when(submarineRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                SubmarineNotFoundException.class,
                () -> submarineService.getSubmarineById(999L));
    }

    @Test
    void shouldUpdateSubmarineSuccessfully() {

        Submarine existingSubmarine = new Submarine();
        existingSubmarine.setId(1L);
        existingSubmarine.setName("Old Name");
        existingSubmarine.setSubmarineType(SubmarineType.SSBN);
        existingSubmarine.setSubmarineClass(SubmarineClass.OHIO);
        existingSubmarine.setNation("Old Nation");
        existingSubmarine.setStatus(SubmarineStatus.RETIRED);

        SubmarineRequestDTO request = new SubmarineRequestDTO();
        request.setName("USS Dallas");
        request.setType(SubmarineType.SSN);
        request.setSubmarineClass(SubmarineClass.LOS_ANGELES);
        request.setNation("USA");
        request.setStatus(SubmarineStatus.ACTIVE);

        Submarine updatedSubmarine = new Submarine();
        updatedSubmarine.setId(1L);
        updatedSubmarine.setName(request.getName());
        updatedSubmarine.setSubmarineType(request.getType());
        updatedSubmarine.setSubmarineClass(request.getSubmarineClass());
        updatedSubmarine.setNation(request.getNation());
        updatedSubmarine.setStatus(request.getStatus());

        when(submarineRepository.findById(1L))
                .thenReturn(Optional.of(existingSubmarine));
        when(submarineRepository.save(any(Submarine.class)))
                .thenReturn(updatedSubmarine);

        SubmarineResponseDTO result = submarineService
                .updateSubmarine(1L, request);

        assertEquals("USS Dallas", result.getName());
        assertEquals(SubmarineType.SSN, result.getType());
        assertEquals(SubmarineClass.LOS_ANGELES,
                result.getSubmarineClass());
        assertEquals("USA", result.getNation());
        assertEquals(SubmarineStatus.ACTIVE, result.getStatus());

        verify(submarineRepository).findById(1L);
        verify(submarineRepository).save(any(Submarine.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentSubmarine() {

        SubmarineRequestDTO request = new SubmarineRequestDTO();

        when(submarineRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                SubmarineNotFoundException.class,
                () -> submarineService.updateSubmarine(
                        999L, request));
    }

    @Test
    void shouldDeleteSubmarineSuccessfully() {

        Submarine submarine = new Submarine();
        submarine.setId(1L);

        when(submarineRepository.findById(1L))
                .thenReturn(Optional.of(submarine));

        submarineService.deleteSubmarine(1L);

        verify(submarineRepository).findById(1L);
        verify(submarineRepository).delete(submarine);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentSubmarine() {

        when(submarineRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                SubmarineNotFoundException.class,
                () -> submarineService.deleteSubmarine(999L));
    }

}
