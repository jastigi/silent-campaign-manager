package com.jastigi.silentcampaignmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.dto.CampaignRequestDTO;
import com.jastigi.silentcampaignmanager.dto.CampaignResponseDTO;
import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.CampaignStatus;
import com.jastigi.silentcampaignmanager.repository.CampaignRepository;
import com.jastigi.silentcampaignmanager.service.impl.CampaignServiceImpl;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class CampaignServiceImplTest {

    @Mock
    private CampaignRepository campaignRepository;

    @InjectMocks
    private CampaignServiceImpl campaignService;

    @Test
    void shouldCreateCampaignSuccessfully() {

        CampaignRequestDTO request = new CampaignRequestDTO();
        request.setName("North Atlantic Patrols");
        request.setDescription("SSBN Operations 1984");
        request.setStartDate(LocalDate.of(2026, 6, 12));
        request.setStatus(CampaignStatus.ACTIVE);

        Campaign savedCampaign = new Campaign();
        savedCampaign.setId(1L);
        savedCampaign.setName(request.getName());
        savedCampaign.setDescription(request.getDescription());
        savedCampaign.setStartDate(request.getStartDate());
        savedCampaign.setStatus(request.getStatus());

        when(campaignRepository.save(any(Campaign.class)))
                .thenReturn(savedCampaign);

        CampaignResponseDTO result = campaignService.createCampaign(request);

        assertEquals(1L, result.getId());
        assertEquals("North Atlantic Patrols", result.getName());
        assertEquals(CampaignStatus.ACTIVE, result.getStatus());

        verify(campaignRepository).save(any(Campaign.class));
    }

}
