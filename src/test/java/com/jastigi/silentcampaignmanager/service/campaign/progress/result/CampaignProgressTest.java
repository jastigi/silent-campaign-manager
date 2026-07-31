package com.jastigi.silentcampaignmanager.service.campaign.progress.result;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CampaignProgressTest {

    @Test
    void shouldRepresentPartialCampaignProgress() {

        CampaignProgress progress = new CampaignProgress(
                3,
                2);

        assertEquals(
                3,
                progress.getTotalPatrols());

        assertEquals(
                2,
                progress.getCompletedPatrols());

        assertEquals(
                1,
                progress.getPendingPatrols());

        assertEquals(
                66.67,
                progress.getCompletionPercentage(),
                0.001);

        assertFalse(
                progress.isCompleted());
    }

    @Test
    void shouldRepresentCompletedCampaign() {

        CampaignProgress progress = new CampaignProgress(
                4,
                4);

        assertEquals(
                0,
                progress.getPendingPatrols());

        assertEquals(
                100.0,
                progress.getCompletionPercentage());

        assertTrue(
                progress.isCompleted());
    }

    @Test
    void shouldRepresentEmptyCampaignAsNotCompleted() {

        CampaignProgress progress = new CampaignProgress(
                0,
                0);

        assertEquals(
                0,
                progress.getPendingPatrols());

        assertEquals(
                0.0,
                progress.getCompletionPercentage());

        assertFalse(
                progress.isCompleted());
    }

    @Test
    void shouldRejectNegativeValues() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new CampaignProgress(
                        -1,
                        0));

        assertThrows(
                IllegalArgumentException.class,
                () -> new CampaignProgress(
                        1,
                        -1));
    }

    @Test
    void shouldRejectCompletedPatrolsGreaterThanTotal() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new CampaignProgress(
                        2,
                        3));
    }

}
