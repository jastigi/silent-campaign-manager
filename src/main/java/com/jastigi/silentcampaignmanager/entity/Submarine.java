package com.jastigi.silentcampaignmanager.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "submarines")
@Data
public class Submarine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubmarineType submarineType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubmarineClass submarineClass;

    @Column(nullable = false)
    private String nation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubmarineStatus status;

    @OneToMany(mappedBy = "submarine")
    @JsonIgnore
    private List<Patrol> patrols = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private SubmarineRole submarineRole;

}
