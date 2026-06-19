package com.jastigi.silentcampaignmanager.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class CampaignSecurityIntegrationTest {

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

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readTree(response)
                .get("token")
                .asText();
    }

    @Test
    void shouldAccessProtectedEndpointWithValidToken()
            throws Exception {

        String token = getAdminToken();

        mockMvc.perform(
                get("/api/v1/campaigns")
                        .header(
                                "Authorization",
                                "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectRequestWithoutToken()
            throws Exception {

        mockMvc.perform(
                get("/api/v1/campaigns"))
                .andExpect(status().isUnauthorized());
    }

    private String getUserToken() throws Exception {

        String loginJson = """
                {
                    "username":"user",
                    "password":"user123"
                }
                """;

        String response = mockMvc.perform(
                post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readTree(response)
                .get("token")
                .asText();
    }

    @Test
    void shouldRejectCampaignCreationForUserRole()
            throws Exception {

        String token = getUserToken();

        String campaignJson = """
                {
                    "name":"Test Campaign",
                    "description":"Security Test",
                    "startDate":"2026-06-19",
                    "status":"ACTIVE"
                }
                """;

        mockMvc.perform(
                post("/api/v1/campaigns")
                        .header(
                                "Authorization",
                                "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(campaignJson))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowCampaignCreationForAdmin()
            throws Exception {

        String token = getAdminToken();

        String campaignJson = """
                {
                    "name":"Admin Campaign",
                    "description":"Admin Test",
                    "startDate":"2026-06-19",
                    "status":"ACTIVE"
                }
                """;

        mockMvc.perform(
                post("/api/v1/campaigns")
                        .header(
                                "Authorization",
                                "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(campaignJson))
                .andExpect(status().isOk());
    }

}
