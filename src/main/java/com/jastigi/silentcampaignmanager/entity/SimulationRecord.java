package com.jastigi.silentcampaignmanager.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "simulation_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimulationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patrol_id", nullable = false)
    private Patrol patrol;

    @Enumerated(EnumType.STRING)
    @Column(name = "mission_outcome", nullable = false, length = 30)
    private SimulationOutcome missionOutcome;

    @Column(name = "mission_score", nullable = false)
    private int missionScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "final_state", nullable = false, length = 30)
    private PatrolSimulationState finalState;

    @Column(name = "contacts_detected", nullable = false)
    private int contactsDetected;

    @Column(name = "contacts_lost", nullable = false)
    private int contactsLost;

    @Column(name = "intelligence_gathered", nullable = false)
    private int intelligenceGathered;

    @Column(name = "incidents", nullable = false)
    private int incidents;

    @Column(name = "completion_date", nullable = false)
    private LocalDate completionDate;

    @Column(name = "report_summary", nullable = false, length = 2000)
    private String reportSummary;

    @Column(name = "mission_debrief", nullable = false, length = 4000)
    private String missionDebrief;

    @Column(name = "recorded_at", nullable = false, updatable = false)
    private LocalDateTime recordedAt;

    @PrePersist
    void prePersist() {

        if (recordedAt == null) {
            recordedAt = LocalDateTime.now();
        }
    }

}
