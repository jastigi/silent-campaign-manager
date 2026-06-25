package com.jastigi.silentcampaignmanager.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jastigi.silentcampaignmanager.dto.ContactRequestDTO;
import com.jastigi.silentcampaignmanager.dto.ContactResponseDTO;
import com.jastigi.silentcampaignmanager.entity.Contact;
import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.exception.ContactNotFoundException;
import com.jastigi.silentcampaignmanager.exception.PatrolNotFoundException;
import com.jastigi.silentcampaignmanager.mapper.ContactMapper;
import com.jastigi.silentcampaignmanager.repository.ContactRepository;
import com.jastigi.silentcampaignmanager.repository.PatrolRepository;
import com.jastigi.silentcampaignmanager.service.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final PatrolRepository patrolRepository;

    public ContactServiceImpl(ContactRepository contactRepository, PatrolRepository patrolRepository) {
        this.contactRepository = contactRepository;
        this.patrolRepository = patrolRepository;
    }

    @Override
    public ContactResponseDTO createContact(
            Long patrolId,
            ContactRequestDTO request) {

        Patrol patrol = patrolRepository.findById(patrolId)
                .orElseThrow(() -> new PatrolNotFoundException(
                        patrolId));

        Contact contact = ContactMapper.toEntity(request);

        contact.setPatrol(patrol);

        Contact saved = contactRepository.save(contact);

        return ContactMapper.toDTO(saved);
    }

    @Override
    public List<ContactResponseDTO> getContactsByPatrol(
            Long patrolId) {
        return contactRepository
                .findByPatrolId(patrolId)
                .stream()
                .map(ContactMapper::toDTO)
                .toList();

    }

    @Override
    public ContactResponseDTO getContactById(
            Long id) {

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException(id));

        return ContactMapper.toDTO(contact);

    }

    @Override
    public ContactResponseDTO updateContact(
            Long id,
            ContactRequestDTO request) {

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException(id));

        contact.setContactName(
                request.getContactName());

        contact.setContactType(
                request.getContactType());

        contact.setThreatLevel(
                request.getThreatLevel());

        contact.setDetectionDate(
                request.getDetectionDate());

        Contact updated = contactRepository.save(contact);

        return ContactMapper.toDTO(updated);

    }

    @Override
    public void deleteContact(
            Long id) {

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException(id));

        contactRepository.delete(contact);

    }

}
