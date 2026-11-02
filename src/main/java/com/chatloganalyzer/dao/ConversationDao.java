package com.chatloganalyzer.dao;

import com.chatloganalyzer.model.Conversation;
import com.chatloganalyzer.model.ConversationSummary;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.List;

public class ConversationDao {
    private final SessionFactory sessionFactory;
    public ConversationDao(SessionFactory sessionFactory) { this.sessionFactory = sessionFactory; }

    public List<ConversationSummary> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("select new com.chatloganalyzer.model.ConversationSummary(c.id, c.title, c.sourceType, count(m), count(distinct m.participant), max(m.sentAt)) from Conversation c left join Message m on m.conversation = c group by c.id, c.title, c.sourceType order by max(m.sentAt) desc, c.importedAt desc", ConversationSummary.class).list();
        }
    }
    public Conversation findById(long id) {
        try (Session session = sessionFactory.openSession()) { return session.find(Conversation.class, id); }
    }
}
