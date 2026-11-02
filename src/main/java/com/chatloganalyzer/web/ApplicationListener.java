package com.chatloganalyzer.web;

import com.chatloganalyzer.config.HibernateUtil;
import com.chatloganalyzer.dao.MessageDao;
import com.chatloganalyzer.dao.ConversationDao;
import com.chatloganalyzer.dao.DashboardDao;
import com.chatloganalyzer.dao.ParticipantDao;
import com.chatloganalyzer.service.ChatImportService;
import com.chatloganalyzer.service.ConversationService;
import com.chatloganalyzer.service.DashboardService;
import com.chatloganalyzer.service.MessageService;
import com.chatloganalyzer.service.ParticipantService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationListener implements ServletContextListener {
    @Override public void contextInitialized(ServletContextEvent event) {
        event.getServletContext().setAttribute("messageService", new MessageService(new MessageDao(HibernateUtil.getSessionFactory())));
        event.getServletContext().setAttribute("conversationService", new ConversationService(new ConversationDao(HibernateUtil.getSessionFactory())));
        event.getServletContext().setAttribute("dashboardService", new DashboardService(new DashboardDao(HibernateUtil.getSessionFactory())));
        event.getServletContext().setAttribute("chatImportService", new ChatImportService(HibernateUtil.getSessionFactory()));
        event.getServletContext().setAttribute("participantService", new ParticipantService(new ParticipantDao(HibernateUtil.getSessionFactory())));
    }
    @Override public void contextDestroyed(ServletContextEvent event) { HibernateUtil.shutdown(); }
}

