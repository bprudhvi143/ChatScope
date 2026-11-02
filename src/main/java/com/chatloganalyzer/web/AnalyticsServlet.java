package com.chatloganalyzer.web;

import com.chatloganalyzer.model.AnalyticsData;
import com.chatloganalyzer.service.DashboardService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/analytics")
public class AnalyticsServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DashboardService service = (DashboardService) getServletContext().getAttribute("dashboardService");
        request.setAttribute("stats", service.getStats());
        request.setAttribute("analytics", service.getAnalytics());
        request.getRequestDispatcher("/WEB-INF/views/analytics.jsp").forward(request, response);
    }
}
