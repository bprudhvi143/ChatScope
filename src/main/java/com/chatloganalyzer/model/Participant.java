package com.chatloganalyzer.model;

import jakarta.persistence.*;

@Entity
@Table(name = "participant")
public class Participant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "display_name", nullable = false, unique = true) private String displayName;

    protected Participant() { }
    public Participant(String displayName) { this.displayName = displayName; }

    public Long getId() { return id; }
    public String getDisplayName() { return displayName; }
}

