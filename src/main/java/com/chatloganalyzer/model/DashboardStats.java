package com.chatloganalyzer.model;

import java.util.List;

public record DashboardStats(long conversationCount, long messageCount,
                             long participantCount, long spamCount, List<DailyCount> activity) {
    public record DailyCount(String day, long count) { }
}
