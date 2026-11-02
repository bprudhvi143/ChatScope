package com.chatloganalyzer.model;

import java.util.List;

public record AnalyticsData(List<DashboardStats.DailyCount> dailyActivity,
                            List<DashboardStats.DailyCount> hourlyActivity,
                            List<NamedCount> participants,
                            List<NamedCount> conversations) {
    public record NamedCount(String name, long count) { }
}
