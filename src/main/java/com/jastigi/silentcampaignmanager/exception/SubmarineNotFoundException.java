package com.jastigi.silentcampaignmanager.exception;

public class SubmarineNotFoundException extends RuntimeException {

    public SubmarineNotFoundException(Long id) {

        super("Submarine not found with id: " + id);
    }

}
