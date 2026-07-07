package com.jastigi.silentcampaignmanager.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class PatrolCloseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private String getAdminToken() throws Exception {
        String loginJson = """
                {
                    "username":"admin",
                    "password":"admin123"
                }
                """;

        String response = mockMvc.perform(
                post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return new ObjectMapper().readTree(response).get("token").asText();
    }

    @Test
    void shouldClosePatrolWithContactsAndReturnResult() throws Exception {
        String token = getAdminToken();

        String campaignResponse = mockMvc.perform(
                post("/api/v1/campaigns")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name":"Integration Test Campaign",
                                    "description":"Testing patrol close flow",
                                    "startDate":"2026-07-07",
                                    "status":"ACTIVE"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long campaignId = new ObjectMapper().readTree(campaignResponse).get("id").asLong();

        String submarineResponse = mockMvc.perform(
                post("/api/v1/submarines")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name":"USS Test",
                                    "type":"SSN",
                                    "submarineClass":"LOS_ANGELES",
                                    "nation":"USA",
                                    "status":"ACTIVE",
                                    "submarineRole":"SSN"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long submarineId = new ObjectMapper().readTree(submarineResponse).get("id").asLong();

        String patrolJson = """
                {
                    "patrolName":"Test Patrol",
                    "patrolDate":"2026-07-07",
                    "area":"North Atlantic",
                    "result":"SUCCESS",
                    "submarineId":%d,
                    "missionType":"DETERRENCE_PATROL"
                }
                """.formatted(submarineId);

        String patrolResponse = mockMvc.perform(
                post("/api/v1/campaigns/{campaignId}/patrols", campaignId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patrolJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long patrolId = new ObjectMapper().readTree(patrolResponse).get("id").asLong();

        String contactJson = """
                {
                    "contactName":"Hostile Sub",
                    "contactType":"SUBMARINE",
                    "threatLevel":"HIGH",
                    "detectionDate":"2026-07-07",
                    "nation":"USSR",
                    "confidenceLevel":85
                }
                """;

        mockMvc.perform(
                post("/api/v1/patrols/{patrolId}/contacts", patrolId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contactJson))
                .andExpect(status().isOk());

        String contact2Json = """
                {
                    "contactName":"Friendly Ship",
                    "contactType":"SURFACE_SHIP",
                    "threatLevel":"LOW",
                    "detectionDate":"2026-07-07",
                    "nation":"UK",
                    "confidenceLevel":70
                }
                """;

        mockMvc.perform(
                post("/api/v1/patrols/{patrolId}/contacts", patrolId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contact2Json))
                .andExpect(status().isOk());

        mockMvc.perform(
                patch("/api/v1/campaigns/{campaignId}/patrols/{patrolId}/close", campaignId, patrolId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }

}
