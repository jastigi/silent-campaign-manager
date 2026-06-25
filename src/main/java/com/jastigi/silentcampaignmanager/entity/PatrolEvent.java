package com.jastigi.silentcampaignmanager.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "patrol_events")
@Data
public class PatrolEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PatrolEventType eventType;

    @Column(nullable = false)
    private LocalDate eventDate;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Integer severity;

    @ManyToOne
    @JoinColumn(name = "patrol_id")
    private Patrol patrol;

}
