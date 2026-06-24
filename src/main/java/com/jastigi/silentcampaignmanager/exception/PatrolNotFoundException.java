package com.jastigi.silentcampaignmanager.exception;

public class PatrolNotFoundException extends RuntimeException {

    public PatrolNotFoundException(Long id) {

        super("Patrol not found with id: " + id);
    }

}
