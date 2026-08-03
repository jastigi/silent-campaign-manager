package com.jastigi.silentcampaignmanager.service.campaign.lifecycle.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.CampaignStatus;
import com.jastigi.silentcampaignmanager.exception.CampaignNotFoundException;
import com.jastigi.silentcampaignmanager.exception.InvalidCampaignTransitionException;
import com.jastigi.silentcampaignmanager.repository.CampaignRepository;
import com.jastigi.silentcampaignmanager.service.campaign.lifecycle.CampaignLifecycleService;
import com.jastigi.silentcampaignmanager.service.campaign.progress.CampaignProgressService;
import com.jastigi.silentcampaignmanager.service.campaign.progress.result.CampaignProgress;

@ExtendWith(MockitoExtension.class)
class CampaignLifecycleServiceImplTest {

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private CampaignProgressService campaignProgressService;

    private CampaignLifecycleService campaignLifecycleService;

    @BeforeEach
    void setUp() {

        campaignLifecycleService = new CampaignLifecycleServiceImpl(
                campaignRepository,
                campaignProgressService);
    }

    @Test
    void shouldFinishCompletedActiveCampaign() {

        Long campaignId = 1L;

        Campaign campaign = campaign(
                campaignId,
                CampaignStatus.ACTIVE);

        CampaignProgress progress = new CampaignProgress(
                3,
                3);

        when(
                campaignRepository.findById(
                        campaignId))
                .thenReturn(
                        Optional.of(
                                campaign));

        when(
                campaignProgressService.getProgress(
                        campaignId))
                .thenReturn(
                        progress);

        when(
                campaignRepository.save(
                        campaign))
                .thenReturn(
                        campaign);

        Campaign result = campaignLifecycleService.finishCampaign(
                campaignId);

        assertSame(
                campaign,
                result);

        assertEquals(
                CampaignStatus.FINISHED,
                result.getStatus());

        verify(
                campaignProgressService)
                .getProgress(
                        campaignId);

        verify(
                campaignRepository)
                .save(
                        campaign);
    }

    @Test
    void shouldRejectFinishingIncompleteCampaign() {

        Long campaignId = 2L;

        Campaign campaign = campaign(
                campaignId,
                CampaignStatus.ACTIVE);

        CampaignProgress progress = new CampaignProgress(
                3,
                2);

        when(
                campaignRepository.findById(
                        campaignId))
                .thenReturn(
                        Optional.of(
                                campaign));

        when(
                campaignProgressService.getProgress(
                        campaignId))
                .thenReturn(
                        progress);

        InvalidCampaignTransitionException exception = assertThrows(
                InvalidCampaignTransitionException.class,
                () -> campaignLifecycleService
                        .finishCampaign(
                                campaignId));

        assertEquals(
                "Campaign cannot be finished because its patrol progression is incomplete",
                exception.getMessage());

        assertEquals(
                CampaignStatus.ACTIVE,
                campaign.getStatus());

        verify(
                campaignRepository,
                never())
                .save(
                        campaign);
    }

    @Test
    void shouldRejectFinishingCampaignWithoutPatrols() {

        Long campaignId = 3L;

        Campaign campaign = campaign(
                campaignId,
                CampaignStatus.ACTIVE);

        CampaignProgress progress = new CampaignProgress(
                0,
                0);

        when(
                campaignRepository.findById(
                        campaignId))
                .thenReturn(
                        Optional.of(
                                campaign));

        when(
                campaignProgressService.getProgress(
                        campaignId))
                .thenReturn(
                        progress);

        InvalidCampaignTransitionException exception = assertThrows(
                InvalidCampaignTransitionException.class,
                () -> campaignLifecycleService
                        .finishCampaign(
                                campaignId));

        assertEquals(
                "Campaign cannot be finished because it has no patrols",
                exception.getMessage());

        assertEquals(
                CampaignStatus.ACTIVE,
                campaign.getStatus());

        verify(
                campaignRepository,
                never())
                .save(
                        campaign);
    }

    @Test
    void shouldAbandonActiveCampaign() {

        Long campaignId = 4L;

        Campaign campaign = campaign(
                campaignId,
                CampaignStatus.ACTIVE);

        when(
                campaignRepository.findById(
                        campaignId))
                .thenReturn(
                        Optional.of(
                                campaign));

        when(
                campaignRepository.save(
                        campaign))
                .thenReturn(
                        campaign);

        Campaign result = campaignLifecycleService.abandonCampaign(
                campaignId);

        assertSame(
                campaign,
                result);

        assertEquals(
                CampaignStatus.ABANDONED,
                result.getStatus());

        verify(
                campaignRepository)
                .save(
                        campaign);

        verify(
                campaignProgressService,
                never())
                .getProgress(
                        campaignId);
    }

    @Test
    void shouldRejectFinishingFinishedCampaign() {

        Long campaignId = 5L;

        Campaign campaign = campaign(
                campaignId,
                CampaignStatus.FINISHED);

        when(
                campaignRepository.findById(
                        campaignId))
                .thenReturn(
                        Optional.of(
                                campaign));

        assertThrows(
                InvalidCampaignTransitionException.class,
                () -> campaignLifecycleService.finishCampaign(
                        campaignId));

        verify(
                campaignProgressService,
                never())
                .getProgress(
                        campaignId);

        verify(
                campaignRepository,
                never())
                .save(
                        campaign);
    }

    @Test
    void shouldRejectAbandoningFinishedCampaign() {

        Long campaignId = 6L;

        Campaign campaign = campaign(
                campaignId,
                CampaignStatus.FINISHED);

        when(
                campaignRepository.findById(
                        campaignId))
                .thenReturn(
                        Optional.of(
                                campaign));

        assertThrows(
                InvalidCampaignTransitionException.class,
                () -> campaignLifecycleService.abandonCampaign(
                        campaignId));

        verify(
                campaignRepository,
                never())
                .save(
                        campaign);
    }

    @Test
    void shouldRejectFinishingAbandonedCampaign() {

        Long campaignId = 7L;

        Campaign campaign = campaign(
                campaignId,
                CampaignStatus.ABANDONED);

        when(
                campaignRepository.findById(
                        campaignId))
                .thenReturn(
                        Optional.of(
                                campaign));

        assertThrows(
                InvalidCampaignTransitionException.class,
                () -> campaignLifecycleService.finishCampaign(
                        campaignId));

        verify(
                campaignProgressService,
                never())
                .getProgress(
                        campaignId);

        verify(
                campaignRepository,
                never())
                .save(
                        campaign);
    }

    @Test
    void shouldRejectAbandoningAbandonedCampaign() {

        Long campaignId = 8L;

        Campaign campaign = campaign(
                campaignId,
                CampaignStatus.ABANDONED);

        when(
                campaignRepository.findById(
                        campaignId))
                .thenReturn(
                        Optional.of(
                                campaign));

        assertThrows(
                InvalidCampaignTransitionException.class,
                () -> campaignLifecycleService.abandonCampaign(
                        campaignId));

        verify(
                campaignRepository,
                never())
                .save(
                        campaign);
    }

    @Test
    void shouldThrowExceptionWhenFinishingMissingCampaign() {

        Long campaignId = 999L;

        when(
                campaignRepository.findById(
                        campaignId))
                .thenReturn(
                        Optional.empty());

        assertThrows(
                CampaignNotFoundException.class,
                () -> campaignLifecycleService.finishCampaign(
                        campaignId));

        verify(
                campaignProgressService,
                never())
                .getProgress(
                        campaignId);

        verify(
                campaignRepository,
                never())
                .save(
                        org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldThrowExceptionWhenAbandoningMissingCampaign() {

        Long campaignId = 998L;

        when(
                campaignRepository.findById(
                        campaignId))
                .thenReturn(
                        Optional.empty());

        assertThrows(
                CampaignNotFoundException.class,
                () -> campaignLifecycleService.abandonCampaign(
                        campaignId));

        verify(
                campaignRepository,
                never())
                .save(
                        org.mockito.ArgumentMatchers.any());
    }

    private Campaign campaign(
            Long campaignId,
            CampaignStatus status) {

        Campaign campaign = new Campaign();

        campaign.setId(
                campaignId);

        campaign.setName(
                "Campaign " + campaignId);

        campaign.setStatus(
                status);

        return campaign;
    }

}
