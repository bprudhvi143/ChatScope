package com.chatloganalyzer.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "participant_id")
    private Participant participant;
    @Column(name = "sent_at", nullable = false) private LocalDateTime sentAt;
    @Column(nullable = false, columnDefinition = "TEXT") private String body;

    protected Message() { }
    public Message(Conversation conversation, Participant participant, LocalDateTime sentAt, String body) {
        this.conversation = conversation;
        this.participant = participant;
        this.sentAt = sentAt;
        this.body = body;
    }

    public Long getId() { return id; }
    public Conversation getConversation() { return conversation; }
    public Participant getParticipant() { return participant; }
    public LocalDateTime getSentAt() { return sentAt; }
    public String getBody() { return body; }
}

