package com.chatloganalyzer.web;

import com.chatloganalyzer.service.MessageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import com.chatloganalyzer.service.ConversationService;
import com.chatloganalyzer.service.ParticipantService;

@WebServlet("/messages/search")
public class SearchServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("q");
        int page = parsePage(request.getParameter("page"));
        MessageService service = (MessageService) getServletContext().getAttribute("messageService");
        request.setAttribute("query", query == null ? "" : query);
        request.setAttribute("messages", service.search(query, page));
        request.setAttribute("page", page);
        request.setAttribute("hasNext", service.countSearch(query) > (long) (page + 1) * 20);
        request.setAttribute("conversations", ((ConversationService) getServletContext().getAttribute("conversationService")).findAll());
        request.setAttribute("participants", ((ParticipantService) getServletContext().getAttribute("participantService")).findAll());
        request.getRequestDispatcher("/WEB-INF/views/search-results.jsp").forward(request, response);
    }
    private int parsePage(String value) { try { return Math.max(0, Integer.parseInt(value)); } catch (RuntimeException exception) { return 0; } }
}

