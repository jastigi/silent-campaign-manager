package com.jastigi.silentcampaignmanager.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "patrols")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patrol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String patrolName;

    @Column(nullable = false)
    private LocalDate patrolDate;

    private String area;

    @Enumerated(EnumType.STRING)
    private PatrolResult result;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "submarine_id")
    private Submarine submarine;

    @OneToMany(mappedBy = "patrol", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PatrolEvent> events = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionType missionType;

}
