package com.jastigi.silentcampaignmanager.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jastigi.silentcampaignmanager.dto.PatrolRequestDTO;
import com.jastigi.silentcampaignmanager.dto.PatrolResponseDTO;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;
import com.jastigi.silentcampaignmanager.security.JwtService;
import com.jastigi.silentcampaignmanager.service.PatrolService;

@WebMvcTest(controllers = PatrolController.class, excludeAutoConfiguration = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
class PatrolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatrolService patrolService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void shouldCreatePatrolSuccessfully() throws Exception {

        PatrolResponseDTO response = new PatrolResponseDTO();
        response.setId(1L);
        response.setPatrolName("North Atlantic Transit");
        response.setPatrolDate(java.time.LocalDate.of(2026, 6, 12));
        response.setArea("North Atlantic");
        response.setResult(PatrolResult.SUCCESS);
        response.setCampaignId(1L);
        response.setSubmarineId(1L);
        response.setSubmarineName("USS Dallas");

        when(patrolService.createPatrol(
                eq(1L), any(PatrolRequestDTO.class)))
                .thenReturn(response);

        String requestJson = """
                {
                    "patrolName": "North Atlantic Transit",
                    "patrolDate": "2026-06-12",
                    "area": "North Atlantic",
                    "result": "SUCCESS",
                    "submarineId": 1
                }
                """;

        mockMvc.perform(post("/api/v1/campaigns/1/patrols")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.patrolName")
                        .value("North Atlantic Transit"))
                .andExpect(jsonPath("$.patrolDate")
                        .value("2026-06-12"))
                .andExpect(jsonPath("$.area")
                        .value("North Atlantic"))
                .andExpect(jsonPath("$.result")
                        .value("SUCCESS"))
                .andExpect(jsonPath("$.campaignId")
                        .value(1L))
                .andExpect(jsonPath("$.submarineId")
                        .value(1L))
                .andExpect(jsonPath("$.submarineName")
                        .value("USS Dallas"));
    }

    @Test
    void shouldReturnPatrolsByCampaign() throws Exception {

        PatrolResponseDTO patrol = new PatrolResponseDTO();
        patrol.setId(1L);
        patrol.setPatrolName("North Atlantic Transit");
        patrol.setResult(PatrolResult.SUCCESS);
        patrol.setCampaignId(1L);

        when(patrolService.getPatrolsByCampaign(1L))
                .thenReturn(List.of(patrol));

        mockMvc.perform(get("/api/v1/campaigns/1/patrols")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].patrolName")
                        .value("North Atlantic Transit"))
                .andExpect(jsonPath("$[0].result")
                        .value("SUCCESS"))
                .andExpect(jsonPath("$[0].campaignId")
                        .value(1L));
    }

    @Test
    void shouldReturnEmptyListWhenNoPatrols()
            throws Exception {

        when(patrolService.getPatrolsByCampaign(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/campaigns/1/patrols")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

}
