package com.chatloganalyzer.config;

import com.chatloganalyzer.model.Conversation;
import com.chatloganalyzer.model.Message;
import com.chatloganalyzer.model.Participant;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class HibernateUtil {
    private static final SessionFactory SESSION_FACTORY = createSessionFactory();

    private HibernateUtil() { }

    private static SessionFactory createSessionFactory() {
        Properties properties = new Properties();
        try (InputStream stream = HibernateUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (stream == null) {
                throw new IllegalStateException("Missing db.properties. Copy db.properties.example and set local credentials.");
            }
            properties.load(stream);
        } catch (IOException exception) {
            throw new IllegalStateException("Cannot load database settings", exception);
        }

        return new Configuration()
                .addProperties(properties)
                .addAnnotatedClass(Conversation.class)
                .addAnnotatedClass(Participant.class)
                .addAnnotatedClass(Message.class)
                .buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() { return SESSION_FACTORY; }
    public static void shutdown() { SESSION_FACTORY.close(); }
}

