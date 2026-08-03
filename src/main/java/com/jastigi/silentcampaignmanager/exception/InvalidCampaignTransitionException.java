package com.jastigi.silentcampaignmanager.exception;

import com.jastigi.silentcampaignmanager.entity.CampaignStatus;

public class InvalidCampaignTransitionException
        extends RuntimeException {

    public InvalidCampaignTransitionException(
            CampaignStatus currentStatus,
            CampaignStatus targetStatus) {

        super(
                "Invalid campaign transition from "
                        + currentStatus
                        + " to "
                        + targetStatus);
    }

    public InvalidCampaignTransitionException(
            String message) {

        super(message);
    }

}
