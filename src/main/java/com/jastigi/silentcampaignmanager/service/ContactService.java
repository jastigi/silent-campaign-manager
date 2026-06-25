package com.jastigi.silentcampaignmanager.service;

import java.util.List;

import com.jastigi.silentcampaignmanager.dto.ContactRequestDTO;
import com.jastigi.silentcampaignmanager.dto.ContactResponseDTO;

public interface ContactService {

    ContactResponseDTO createContact(
            Long patrolId,
            ContactRequestDTO request);

    List<ContactResponseDTO> getContactsByPatrol(
            Long patrolId);

    ContactResponseDTO getContactById(
            Long id);

    ContactResponseDTO updateContact(
            Long id,
            ContactRequestDTO request);

    void deleteContact(
            Long id);

}
