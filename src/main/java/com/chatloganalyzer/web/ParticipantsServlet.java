package com.chatloganalyzer.web;

import com.chatloganalyzer.service.ParticipantService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/participants")
public class ParticipantsServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("participants", ((ParticipantService) getServletContext().getAttribute("participantService")).findAll());
        request.getRequestDispatcher("/WEB-INF/views/participants.jsp").forward(request, response);
    }
}
