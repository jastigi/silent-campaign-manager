package com.jastigi.silentcampaignmanager.mapper;

import com.jastigi.silentcampaignmanager.dto.ContactRequestDTO;
import com.jastigi.silentcampaignmanager.dto.ContactResponseDTO;
import com.jastigi.silentcampaignmanager.entity.Contact;

public class ContactMapper {

        private ContactMapper() {
        }

        public static Contact toEntity(
                        ContactRequestDTO dto) {

                Contact contact = new Contact();

                contact.setContactName(dto.getContactName());
                contact.setContactType(dto.getContactType());
                contact.setThreatLevel(dto.getThreatLevel());
                contact.setDetectionDate(dto.getDetectionDate());
                contact.setNation(dto.getNation());
                contact.setConfidenceLevel(dto.getConfidenceLevel());
                contact.setNotes(dto.getNotes());
                contact.setSubmarineClass(
                                dto.getSubmarineClass());

                return contact;
        }

        public static ContactResponseDTO toDTO(
                        Contact contact) {

                ContactResponseDTO dto = new ContactResponseDTO();

                dto.setId(contact.getId());
                dto.setContactName(
                                contact.getContactName());
                dto.setContactType(
                                contact.getContactType());
                dto.setThreatLevel(
                                contact.getThreatLevel());
                dto.setDetectionDate(
                                contact.getDetectionDate());
                dto.setNation(contact.getNation());
                dto.setNationAlignment(
                                contact.getNation().getAlignment());
                dto.setConfidenceLevel(contact.getConfidenceLevel());
                dto.setNotes(contact.getNotes());

                if (contact.getPatrol() != null) {
                        dto.setPatrolId(
                                        contact.getPatrol().getId());
                }

                dto.setSubmarineClass(
                                contact.getSubmarineClass());

                return dto;
        }

}
