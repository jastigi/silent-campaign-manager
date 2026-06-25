package com.jastigi.silentcampaignmanager.exception;

public class PatrolEventNotFoundException extends RuntimeException {

    public PatrolEventNotFoundException(
            Long id) {

        super("Patrol event not found with id: " + id);
    }

}
