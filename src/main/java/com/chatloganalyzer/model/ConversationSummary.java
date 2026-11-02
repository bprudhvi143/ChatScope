package com.chatloganalyzer.model;

import java.time.LocalDateTime;

public record ConversationSummary(Long id, String title, String sourceType, Long messageCount,
                                  Long participantCount, LocalDateTime lastActivity) { }
