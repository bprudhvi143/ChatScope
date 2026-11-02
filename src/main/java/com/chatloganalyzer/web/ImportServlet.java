package com.chatloganalyzer.web;

import com.chatloganalyzer.model.ImportResult;
import com.chatloganalyzer.service.ChatImportService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@WebServlet("/import") @MultipartConfig(maxFileSize = 10 * 1024 * 1024)
public class ImportServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/import.jsp").forward(request, response);
    }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part file = request.getPart("chatFile");
        if (file == null || file.getSize() == 0) { request.setAttribute("error", "Choose a CSV file first."); doGet(request, response); return; }
        if (!file.getSubmittedFileName().toLowerCase().endsWith(".csv")) { request.setAttribute("error", "Only CSV files are accepted."); doGet(request, response); return; }
        try (InputStreamReader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {
            ChatImportService service = (ChatImportService) getServletContext().getAttribute("chatImportService");
            ImportResult result = service.importCsv(reader);
            request.getSession().setAttribute("flash", result.importedMessages() + " messages imported into “" + result.conversationTitle() + "”.");
            response.sendRedirect(request.getContextPath() + "/conversations");
        } catch (IllegalArgumentException exception) { request.setAttribute("error", exception.getMessage()); doGet(request, response); }
    }
}
