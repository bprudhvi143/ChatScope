package com.chatloganalyzer.web;

import com.chatloganalyzer.service.ConversationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/conversations")
public class ConversationServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConversationService service = (ConversationService) getServletContext().getAttribute("conversationService");
        request.setAttribute("conversations", service.findAll());
        request.setAttribute("flash", request.getSession().getAttribute("flash"));
        request.getSession().removeAttribute("flash");
        request.getRequestDispatcher("/WEB-INF/views/conversations.jsp").forward(request, response);
    }
}
