package com.jastigi.silentcampaignmanager.exception;

public class PatrolOperationNotAllowedException
        extends RuntimeException {

    public PatrolOperationNotAllowedException(
            String message) {

        super(message);
    }
}
