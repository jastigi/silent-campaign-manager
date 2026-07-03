package com.jastigi.silentcampaignmanager.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jastigi.silentcampaignmanager.dto.ContactRequestDTO;
import com.jastigi.silentcampaignmanager.dto.ContactResponseDTO;
import com.jastigi.silentcampaignmanager.entity.ContactType;
import com.jastigi.silentcampaignmanager.entity.Nation;
import com.jastigi.silentcampaignmanager.entity.ThreatLevel;
import com.jastigi.silentcampaignmanager.exception.PatrolNotFoundException;
import com.jastigi.silentcampaignmanager.security.JwtService;
import com.jastigi.silentcampaignmanager.service.ContactService;

@WebMvcTest(controllers = { ContactController.class,
        ContactManagementController.class }, excludeAutoConfiguration = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContactService contactService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void shouldCreateContactSuccessfully() throws Exception {

        ContactResponseDTO response = new ContactResponseDTO();
        response.setId(1L);
        response.setContactName("Sierra-01");
        response.setContactType(ContactType.SUBMARINE);
        response.setThreatLevel(ThreatLevel.HIGH);
        response.setDetectionDate(java.time.LocalDate.of(2026, 6, 12));
        response.setNation(Nation.USSR);
        response.setConfidenceLevel(85);
        response.setPatrolId(1L);

        when(contactService.createContact(
                eq(1L), any(ContactRequestDTO.class)))
                .thenReturn(response);

        String requestJson = """
                {
                    "contactName": "Sierra-01",
                    "contactType": "SUBMARINE",
                    "threatLevel": "HIGH",
                    "detectionDate": "2026-06-12",
                    "nation": "USSR",
                    "confidenceLevel": 85
                }
                """;

        mockMvc.perform(post("/api/v1/patrols/1/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.contactName")
                        .value("Sierra-01"))
                .andExpect(jsonPath("$.contactType")
                        .value("SUBMARINE"))
                .andExpect(jsonPath("$.threatLevel")
                        .value("HIGH"))
                .andExpect(jsonPath("$.detectionDate")
                        .value("2026-06-12"))
                .andExpect(jsonPath("$.patrolId").value(1L));
    }

    @Test
    void shouldReturnContactsByPatrol() throws Exception {

        ContactResponseDTO contact = new ContactResponseDTO();
        contact.setId(1L);
        contact.setContactName("Sierra-01");
        contact.setContactType(ContactType.SUBMARINE);
        contact.setThreatLevel(ThreatLevel.HIGH);
        contact.setNation(Nation.USSR);
        contact.setConfidenceLevel(85);
        contact.setPatrolId(1L);

        when(contactService.getContactsByPatrol(1L))
                .thenReturn(List.of(contact));

        mockMvc.perform(get("/api/v1/patrols/1/contacts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].contactName")
                        .value("Sierra-01"))
                .andExpect(jsonPath("$[0].contactType")
                        .value("SUBMARINE"))
                .andExpect(jsonPath("$[0].threatLevel")
                        .value("HIGH"))
                .andExpect(jsonPath("$[0].patrolId")
                        .value(1L));
    }

    @Test
    void shouldReturnEmptyListWhenNoContacts()
            throws Exception {

        when(contactService.getContactsByPatrol(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/patrols/1/contacts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldReturn404WhenPatrolNotFound() throws Exception {

        when(contactService.getContactsByPatrol(999L))
                .thenThrow(new PatrolNotFoundException(999L));

        mockMvc.perform(get("/api/v1/patrols/999/contacts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error")
                        .value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Patrol not found with id: 999"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/patrols/999/contacts"));
    }

    @Test
    void shouldHandleTypeMismatchOnGetById()
            throws Exception {

        mockMvc.perform(
                get("/api/v1/contacts/not-a-number")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error")
                        .value("Bad Request"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/contacts/not-a-number"));
    }

    @Test
    void shouldGetContactById() throws Exception {

        ContactResponseDTO response = new ContactResponseDTO();
        response.setId(1L);
        response.setContactName("Sierra-01");
        response.setContactType(ContactType.SUBMARINE);
        response.setThreatLevel(ThreatLevel.HIGH);
        response.setNation(Nation.USSR);
        response.setConfidenceLevel(85);

        when(contactService.getContactById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/contacts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.contactName")
                        .value("Sierra-01"));
    }

    @Test
    void shouldUpdateContactSuccessfully() throws Exception {

        ContactResponseDTO response = new ContactResponseDTO();
        response.setId(1L);
        response.setContactName("Sierra-01 Updated");
        response.setContactType(ContactType.AIRCRAFT);
        response.setThreatLevel(ThreatLevel.CRITICAL);
        response.setDetectionDate(java.time.LocalDate.of(2026, 6, 15));
        response.setNation(Nation.USA);
        response.setConfidenceLevel(95);

        when(contactService.updateContact(
                eq(1L), any(ContactRequestDTO.class)))
                .thenReturn(response);

        String requestJson = """
                {
                    "contactName": "Sierra-01 Updated",
                    "contactType": "AIRCRAFT",
                    "threatLevel": "CRITICAL",
                    "detectionDate": "2026-06-15",
                    "nation": "USA",
                    "confidenceLevel": 95
                }
                """;

        mockMvc.perform(put("/api/v1/contacts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.contactName")
                        .value("Sierra-01 Updated"))
                .andExpect(jsonPath("$.contactType")
                        .value("AIRCRAFT"))
                .andExpect(jsonPath("$.threatLevel")
                        .value("CRITICAL"));
    }

    @Test
    void shouldDeleteContactSuccessfully() throws Exception {

        doNothing().when(contactService)
                .deleteContact(1L);

        mockMvc.perform(delete("/api/v1/contacts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
