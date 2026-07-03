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

import com.jastigi.silentcampaignmanager.dto.ContactRequestDTO;
import com.jastigi.silentcampaignmanager.dto.ContactResponseDTO;
import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.Nation;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;
import com.jastigi.silentcampaignmanager.exception.ContactNotFoundException;
import com.jastigi.silentcampaignmanager.exception.PatrolNotFoundException;
import com.jastigi.silentcampaignmanager.repository.ContactRepository;
import com.jastigi.silentcampaignmanager.repository.PatrolRepository;
import com.jastigi.silentcampaignmanager.service.impl.ContactServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ContactServiceImplTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private PatrolRepository patrolRepository;

    @InjectMocks
    private ContactServiceImpl contactService;

    @Test
    void shouldCreateContactSuccessfully() {

        Long patrolId = 1L;

        ContactRequestDTO request = new ContactRequestDTO();
        request.setContactName("Sierra-01");
        request.setContactType(ContactType.SUBMARINE);
        request.setThreatLevel(ThreatLevel.HIGH);
        request.setDetectionDate(LocalDate.of(2026, 6, 12));
        request.setNation(Nation.USSR);
        request.setConfidenceLevel(85);
        request.setNotes("Possible Victor-III class");

        Patrol patrol = new Patrol();
        patrol.setId(patrolId);
        patrol.setPatrolName("North Atlantic Transit");
        patrol.setResult(PatrolResult.SUCCESS);

        Contact savedContact = new Contact();
        savedContact.setId(1L);
        savedContact.setContactName(request.getContactName());
        savedContact.setContactType(request.getContactType());
        savedContact.setThreatLevel(request.getThreatLevel());
        savedContact.setDetectionDate(request.getDetectionDate());
        savedContact.setNation(request.getNation());
        savedContact.setConfidenceLevel(request.getConfidenceLevel());
        savedContact.setNotes(request.getNotes());
        savedContact.setPatrol(patrol);

        when(patrolRepository.findById(patrolId))
                .thenReturn(Optional.of(patrol));
        when(contactRepository.save(any(Contact.class)))
                .thenReturn(savedContact);

        ContactResponseDTO result = contactService
                .createContact(patrolId, request);

        assertEquals(1L, result.getId());
        assertEquals("Sierra-01", result.getContactName());
        assertEquals(ContactType.SUBMARINE, result.getContactType());
        assertEquals(ThreatLevel.HIGH, result.getThreatLevel());
        assertEquals(Nation.USSR, result.getNation());
        assertEquals(85, result.getConfidenceLevel());
        assertEquals(patrolId, result.getPatrolId());

        verify(patrolRepository).findById(patrolId);
        verify(contactRepository).save(any(Contact.class));
    }

    @Test
    void shouldThrowExceptionWhenPatrolNotFoundOnCreate() {

        Long patrolId = 999L;
        ContactRequestDTO request = new ContactRequestDTO();

        when(patrolRepository.findById(patrolId))
                .thenReturn(Optional.empty());

        assertThrows(
                PatrolNotFoundException.class,
                () -> contactService.createContact(
                        patrolId, request));
    }

    @Test
    void shouldReturnContactsByPatrol() {

        Long patrolId = 1L;

        Patrol patrol = new Patrol();
        patrol.setId(patrolId);

        Contact contact = new Contact();
        contact.setId(1L);
        contact.setContactName("Sierra-01");
        contact.setContactType(ContactType.SUBMARINE);
        contact.setThreatLevel(ThreatLevel.HIGH);
        contact.setNation(Nation.USSR);
        contact.setConfidenceLevel(85);

        when(patrolRepository.findById(patrolId))
                .thenReturn(Optional.of(patrol));
        when(contactRepository.findByPatrolId(patrolId))
                .thenReturn(List.of(contact));

        List<ContactResponseDTO> result = contactService
                .getContactsByPatrol(patrolId);

        assertEquals(1, result.size());
        assertEquals(
                "Sierra-01",
                result.getFirst().getContactName());

        verify(patrolRepository).findById(patrolId);
        verify(contactRepository)
                .findByPatrolId(patrolId);
    }

    @Test
    void shouldThrowExceptionWhenPatrolNotFoundOnGetContacts() {

        Long patrolId = 999L;

        when(patrolRepository.findById(patrolId))
                .thenReturn(Optional.empty());

        assertThrows(
                PatrolNotFoundException.class,
                () -> contactService.getContactsByPatrol(
                        patrolId));
    }

    @Test
    void shouldReturnEmptyListWhenNoContacts() {

        Long patrolId = 1L;

        when(patrolRepository.findById(patrolId))
                .thenReturn(Optional.of(new Patrol()));
        when(contactRepository.findByPatrolId(patrolId))
                .thenReturn(List.of());

        List<ContactResponseDTO> result = contactService
                .getContactsByPatrol(patrolId);

        assertTrue(result.isEmpty());

        verify(contactRepository)
                .findByPatrolId(patrolId);
    }

    @Test
    void shouldReturnContactById() {

        Contact contact = new Contact();
        contact.setId(1L);
        contact.setContactName("Sierra-01");
        contact.setContactType(ContactType.SUBMARINE);
        contact.setThreatLevel(ThreatLevel.HIGH);
        contact.setDetectionDate(LocalDate.of(2026, 6, 12));
        contact.setNation(Nation.USSR);
        contact.setConfidenceLevel(85);

        when(contactRepository.findById(1L))
                .thenReturn(Optional.of(contact));

        ContactResponseDTO result = contactService
                .getContactById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Sierra-01", result.getContactName());

        verify(contactRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenContactNotFound() {

        when(contactRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ContactNotFoundException.class,
                () -> contactService.getContactById(999L));
    }

    @Test
    void shouldUpdateContactSuccessfully() {

        Contact existingContact = new Contact();
        existingContact.setId(1L);
        existingContact.setContactName("Old Contact");
        existingContact.setContactType(ContactType.UNKNOWN);
        existingContact.setThreatLevel(ThreatLevel.LOW);
        existingContact.setDetectionDate(LocalDate.of(2026, 1, 1));
        existingContact.setNation(Nation.UNKNOWN);
        existingContact.setConfidenceLevel(0);

        ContactRequestDTO request = new ContactRequestDTO();
        request.setContactName("Sierra-01");
        request.setContactType(ContactType.SUBMARINE);
        request.setThreatLevel(ThreatLevel.HIGH);
        request.setDetectionDate(LocalDate.of(2026, 6, 12));
        request.setNation(Nation.USSR);
        request.setConfidenceLevel(85);
        request.setNotes("Updated notes");

        Contact updatedContact = new Contact();
        updatedContact.setId(1L);
        updatedContact.setContactName(request.getContactName());
        updatedContact.setContactType(request.getContactType());
        updatedContact.setThreatLevel(request.getThreatLevel());
        updatedContact.setDetectionDate(request.getDetectionDate());
        updatedContact.setNation(request.getNation());
        updatedContact.setConfidenceLevel(request.getConfidenceLevel());
        updatedContact.setNotes(request.getNotes());

        when(contactRepository.findById(1L))
                .thenReturn(Optional.of(existingContact));
        when(contactRepository.save(any(Contact.class)))
                .thenReturn(updatedContact);

        ContactResponseDTO result = contactService
                .updateContact(1L, request);

        assertEquals("Sierra-01", result.getContactName());
        assertEquals(ContactType.SUBMARINE, result.getContactType());
        assertEquals(ThreatLevel.HIGH, result.getThreatLevel());
        assertEquals(Nation.USSR, result.getNation());
        assertEquals(85, result.getConfidenceLevel());
        assertEquals(
                LocalDate.of(2026, 6, 12),
                result.getDetectionDate());

        verify(contactRepository).findById(1L);
        verify(contactRepository).save(any(Contact.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentContact() {

        ContactRequestDTO request = new ContactRequestDTO();

        when(contactRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ContactNotFoundException.class,
                () -> contactService.updateContact(
                        999L, request));
    }

    @Test
    void shouldDeleteContactSuccessfully() {

        Contact contact = new Contact();
        contact.setId(1L);

        when(contactRepository.findById(1L))
                .thenReturn(Optional.of(contact));

        contactService.deleteContact(1L);

        verify(contactRepository).findById(1L);
        verify(contactRepository).delete(contact);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentContact() {

        when(contactRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ContactNotFoundException.class,
                () -> contactService.deleteContact(999L));
    }

}
