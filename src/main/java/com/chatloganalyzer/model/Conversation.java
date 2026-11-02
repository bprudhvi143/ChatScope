package com.chatloganalyzer.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "conversation")
public class Conversation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String title;
    @Column(name = "source_type", nullable = false) private String sourceType;
    @Column(name = "imported_at", nullable = false) private LocalDateTime importedAt;

    protected Conversation() { }
    public Conversation(String title, String sourceType) {
        this.title = title;
        this.sourceType = sourceType;
        this.importedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getSourceType() { return sourceType; }
    public LocalDateTime getImportedAt() { return importedAt; }
}

