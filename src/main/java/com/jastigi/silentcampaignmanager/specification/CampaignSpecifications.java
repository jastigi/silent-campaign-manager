package com.jastigi.silentcampaignmanager.specification;

import org.springframework.data.jpa.domain.Specification;

import com.jastigi.silentcampaignmanager.entity.Campaign;
import com.jastigi.silentcampaignmanager.entity.CampaignStatus;

public final class CampaignSpecifications {

    private CampaignSpecifications() {
    }

    public static Specification<Campaign> hasStatus(
            CampaignStatus status) {

        return (root, query, cb) -> cb.equal(root.get("status"), status);

    }

    public static Specification<Campaign> nameContains(
            String name) {

        return (root, query, cb) -> {

            if (name == null || name.isBlank()) {
                return cb.conjunction();
            }

            return cb.like(
                    cb.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%");
        };

    }

}
