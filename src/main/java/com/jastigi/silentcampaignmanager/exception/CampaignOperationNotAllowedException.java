package com.jastigi.silentcampaignmanager.exception;

public class CampaignOperationNotAllowedException
                extends RuntimeException {

        public CampaignOperationNotAllowedException(
                        String message) {

                super(message);
        }

}
