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

import com.jastigi.silentcampaignmanager.dto.PatrolEventRequestDTO;
import com.jastigi.silentcampaignmanager.dto.PatrolEventResponseDTO;
import com.jastigi.silentcampaignmanager.entity.PatrolEventType;
import com.jastigi.silentcampaignmanager.security.JwtService;
import com.jastigi.silentcampaignmanager.service.PatrolEventService;

@WebMvcTest(controllers = { PatrolEventController.class,
        PatrolEventManagementController.class }, excludeAutoConfiguration = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
class PatrolEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatrolEventService patrolEventService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void shouldCreateEventSuccessfully() throws Exception {

        PatrolEventResponseDTO response = new PatrolEventResponseDTO();
        response.setId(1L);
        response.setEventType(PatrolEventType.SOSUS_DETECTION);
        response.setEventDate(java.time.LocalDate.of(2026, 6, 12));
        response.setDescription("Contact Sierra-01 detected on SOSUS array");
        response.setSeverity(7);
        response.setPatrolId(1L);

        when(patrolEventService.createEvent(
                eq(1L), any(PatrolEventRequestDTO.class)))
                .thenReturn(response);

        String requestJson = """
                {
                    "eventType": "SOSUS_DETECTION",
                    "eventDate": "2026-06-12",
                    "description": "Contact Sierra-01 detected on SOSUS array",
                    "severity": 7
                }
                """;

        mockMvc.perform(post("/api/v1/patrols/1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.eventType")
                        .value("SOSUS_DETECTION"))
                .andExpect(jsonPath("$.eventDate")
                        .value("2026-06-12"))
                .andExpect(jsonPath("$.severity").value(7))
                .andExpect(jsonPath("$.patrolId").value(1L));
    }

    @Test
    void shouldReturnEventsByPatrol() throws Exception {

        PatrolEventResponseDTO event = new PatrolEventResponseDTO();
        event.setId(1L);
        event.setEventType(PatrolEventType.SOSUS_DETECTION);
        event.setSeverity(7);
        event.setPatrolId(1L);

        when(patrolEventService.getEventsByPatrol(1L))
                .thenReturn(List.of(event));

        mockMvc.perform(get("/api/v1/patrols/1/events")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].eventType")
                        .value("SOSUS_DETECTION"))
                .andExpect(jsonPath("$[0].severity").value(7))
                .andExpect(jsonPath("$[0].patrolId").value(1L));
    }

    @Test
    void shouldReturnEmptyListWhenNoEvents()
            throws Exception {

        when(patrolEventService.getEventsByPatrol(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/patrols/1/events")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldHandleTypeMismatchOnGetById()
            throws Exception {

        mockMvc.perform(
                get("/api/v1/patrol-events/not-a-number")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error")
                        .value("Bad Request"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/patrol-events/not-a-number"));
    }

    @Test
    void shouldGetEventById() throws Exception {

        PatrolEventResponseDTO response = new PatrolEventResponseDTO();
        response.setId(1L);
        response.setEventType(PatrolEventType.MISSILE_LAUNCH);
        response.setSeverity(10);

        when(patrolEventService.getEventById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/patrol-events/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.eventType")
                        .value("MISSILE_LAUNCH"))
                .andExpect(jsonPath("$.severity").value(10));
    }

    @Test
    void shouldUpdateEventSuccessfully() throws Exception {

        PatrolEventResponseDTO response = new PatrolEventResponseDTO();
        response.setId(1L);
        response.setEventType(PatrolEventType.SUCCESSFUL_TRACKING);
        response.setEventDate(java.time.LocalDate.of(2026, 6, 15));
        response.setSeverity(8);

        when(patrolEventService.updateEvent(
                eq(1L), any(PatrolEventRequestDTO.class)))
                .thenReturn(response);

        String requestJson = """
                {
                    "eventType": "SUCCESSFUL_TRACKING",
                    "eventDate": "2026-06-15",
                    "severity": 8
                }
                """;

        mockMvc.perform(put("/api/v1/patrol-events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.eventType")
                        .value("SUCCESSFUL_TRACKING"))
                .andExpect(jsonPath("$.severity").value(8));
    }

    @Test
    void shouldDeleteEventSuccessfully() throws Exception {

        doNothing().when(patrolEventService)
                .deleteEvent(1L);

        mockMvc.perform(delete("/api/v1/patrol-events/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
