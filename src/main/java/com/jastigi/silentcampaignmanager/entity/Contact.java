package com.jastigi.silentcampaignmanager.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "contacts")
@Data
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contactName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContactType contactType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ThreatLevel threatLevel;

    @Column(nullable = false)
    private LocalDate detectionDate;

    @ManyToOne
    @JoinColumn(name = "patrol_id")
    private Patrol patrol;

}
