package com.chatloganalyzer.service;

import com.chatloganalyzer.dao.DashboardDao;
import com.chatloganalyzer.model.DashboardStats;
import com.chatloganalyzer.model.AnalyticsData;

public class DashboardService {
    private final DashboardDao dashboardDao;
    public DashboardService(DashboardDao dashboardDao) { this.dashboardDao = dashboardDao; }
    public DashboardStats getStats() { return dashboardDao.getStats(); }
    public AnalyticsData getAnalytics() { return dashboardDao.getAnalytics(); }
}
