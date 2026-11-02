package com.chatloganalyzer.dao;

import com.chatloganalyzer.model.ParticipantSummary;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.List;

public class ParticipantDao {
    private final SessionFactory sessionFactory;
    public ParticipantDao(SessionFactory sessionFactory) { this.sessionFactory = sessionFactory; }
    public List<ParticipantSummary> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("select new com.chatloganalyzer.model.ParticipantSummary(p.id, p.displayName, count(m), coalesce(sum(length(trim(m.body)) - length(replace(trim(m.body), ' ', '')) + 1), 0)) from Participant p left join Message m on m.participant = p group by p.id, p.displayName order by count(m) desc, p.displayName", ParticipantSummary.class).list();
        }
    }
}
