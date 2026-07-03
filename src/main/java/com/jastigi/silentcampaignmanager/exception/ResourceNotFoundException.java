package com.jastigi.silentcampaignmanager.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Long id) {
        super("Patrol not found with id: " + id);
    }

}
