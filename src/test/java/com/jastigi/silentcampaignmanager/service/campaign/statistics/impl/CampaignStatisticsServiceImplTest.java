package com.jastigi.silentcampaignmanager.service.campaign.statistics.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jastigi.silentcampaignmanager.entity.Patrol;
import com.jastigi.silentcampaignmanager.entity.PatrolResult;
import com.jastigi.silentcampaignmanager.repository.PatrolRepository;
import com.jastigi.silentcampaignmanager.service.campaign.statistics.CampaignStatistics;

@ExtendWith(MockitoExtension.class)
class CampaignStatisticsServiceImplTest {

    @Mock
    private PatrolRepository patrolRepository;

    @InjectMocks
    private CampaignStatisticsServiceImpl campaignStatisticsService;

    @Test
    void shouldCalculateCampaignStatistics() {

        Patrol patrol1 = new Patrol();
        patrol1.setResult(PatrolResult.SUCCESS);

        Patrol patrol2 = new Patrol();
        patrol2.setResult(PatrolResult.SUCCESS);

        Patrol patrol3 = new Patrol();
        patrol3.setResult(PatrolResult.PARTIAL_SUCCESS);

        Patrol patrol4 = new Patrol();
        patrol4.setResult(PatrolResult.FAILURE);

        when(patrolRepository.findByCampaignId(1L))
                .thenReturn(List.of(
                        patrol1,
                        patrol2,
                        patrol3,
                        patrol4));

        CampaignStatistics statistics = campaignStatisticsService.calculate(1L);

        assertEquals(4, statistics.getTotalPatrols());
        assertEquals(2, statistics.getSuccessfulPatrols());
        assertEquals(1, statistics.getPartialSuccessfulPatrols());
        assertEquals(1, statistics.getFailedPatrols());
    }
}
