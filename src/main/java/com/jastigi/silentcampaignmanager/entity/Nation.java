package com.jastigi.silentcampaignmanager.entity;

public enum Nation {

    USA,
    USSR,
    UK,
    FRANCE,
    NATO,
    UNKNOWN;

    public NationAlignment getAlignment() {

        return switch (this) {

            case USA, UK, NATO ->
                    NationAlignment.FRIENDLY;

            case USSR ->
                    NationAlignment.HOSTILE;

            case FRANCE ->
                    NationAlignment.NEUTRAL;

            default ->
                    NationAlignment.UNKNOWN;

        };
    }

}
