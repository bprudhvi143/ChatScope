package com.chatloganalyzer.dao;

import com.chatloganalyzer.model.Message;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.List;

public class MessageDao {
    private final SessionFactory sessionFactory;
    public MessageDao(SessionFactory sessionFactory) { this.sessionFactory = sessionFactory; }

    public List<Message> search(String query, int page, int pageSize) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("select m from Message m left join fetch m.participant join fetch m.conversation where lower(m.body) like :query order by m.sentAt desc", Message.class)
                    .setParameter("query", "%" + query.toLowerCase() + "%")
                    .setFirstResult(page * pageSize)
                    .setMaxResults(pageSize)
                    .list();
        }
    }

    public long countSearch(String query) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("select count(m) from Message m where lower(m.body) like :query", Long.class)
                    .setParameter("query", "%" + query.toLowerCase() + "%").getSingleResult();
        }
    }

    public List<Message> findByConversation(long conversationId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("select m from Message m left join fetch m.participant where m.conversation.id = :id order by m.sentAt", Message.class)
                    .setParameter("id", conversationId).setMaxResults(500).list();
        }
    }
}

