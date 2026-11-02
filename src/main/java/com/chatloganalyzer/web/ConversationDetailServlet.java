package com.chatloganalyzer.web;

import com.chatloganalyzer.service.ConversationService;
import com.chatloganalyzer.service.MessageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/conversation")
public class ConversationDetailServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            long id = Long.parseLong(request.getParameter("id"));
            ConversationService conversations = (ConversationService) getServletContext().getAttribute("conversationService");
            if (conversations.findById(id) == null) { response.sendError(404); return; }
            request.setAttribute("conversation", conversations.findById(id));
            request.setAttribute("messages", ((MessageService) getServletContext().getAttribute("messageService")).findByConversation(id));
            request.getRequestDispatcher("/WEB-INF/views/conversation-detail.jsp").forward(request, response);
        } catch (NumberFormatException exception) { response.sendError(400); }
    }
}
