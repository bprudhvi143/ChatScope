package com.chatloganalyzer.web;

import com.chatloganalyzer.model.AnalyticsData;
import com.chatloganalyzer.model.DashboardStats;
import com.chatloganalyzer.service.DashboardService;
import com.chatloganalyzer.util.ViewUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/** Supplies the same real analytics data used by the JSP views to chart clients. */
@WebServlet("/api/analytics")
public class AnalyticsDataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DashboardService service = (DashboardService) getServletContext().getAttribute("dashboardService");
        AnalyticsData analytics = service.getAnalytics();

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        response.getWriter().write("{\"daily\":" + daily(analytics.dailyActivity())
                + ",\"hourly\":" + daily(analytics.hourlyActivity())
                + ",\"people\":" + named(analytics.participants())
                + ",\"conversations\":" + named(analytics.conversations()) + "}");
    }

    private String daily(List<DashboardStats.DailyCount> values) {
        StringBuilder json = new StringBuilder("[");
        for (DashboardStats.DailyCount value : values) {
            if (json.length() > 1) json.append(',');
            json.append("{\"label\":\"").append(ViewUtil.escapeJson(value.day()))
                    .append("\",\"value\":").append(value.count()).append('}');
        }
        return json.append(']').toString();
    }

    private String named(List<AnalyticsData.NamedCount> values) {
        StringBuilder json = new StringBuilder("[");
        for (AnalyticsData.NamedCount value : values) {
            if (json.length() > 1) json.append(',');
            json.append("{\"label\":\"").append(ViewUtil.escapeJson(value.name()))
                    .append("\",\"value\":").append(value.count()).append('}');
        }
        return json.append(']').toString();
    }
}
