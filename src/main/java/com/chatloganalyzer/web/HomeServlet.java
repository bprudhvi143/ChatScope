package com.chatloganalyzer.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import com.chatloganalyzer.service.DashboardService;

@WebServlet("")
public class HomeServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DashboardService service = (DashboardService) getServletContext().getAttribute("dashboardService");
        request.setAttribute("stats", service.getStats());
        request.setAttribute("analytics", service.getAnalytics());
        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}

