package com.chatloganalyzer.dao;

import com.chatloganalyzer.model.DashboardStats;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.chatloganalyzer.model.AnalyticsData;

public class DashboardDao {
    private final SessionFactory sessionFactory;
    public DashboardDao(SessionFactory sessionFactory) { this.sessionFactory = sessionFactory; }
    public DashboardStats getStats() {
        try (Session session = sessionFactory.openSession()) {
            long conversations = session.createQuery("select count(c) from Conversation c", Long.class).getSingleResult();
            long messages = session.createQuery("select count(m) from Message m", Long.class).getSingleResult();
            long participants = session.createQuery("select count(p) from Participant p", Long.class).getSingleResult();
            long spam = session.createQuery("select count(m) from Message m where lower(m.body) like '%http%' or lower(m.body) like '%winner%' or lower(m.body) like '%free money%'", Long.class).getSingleResult();
            List<Object[]> rows = session.createNativeQuery("select date(sent_at), count(*) from message where sent_at >= date_sub(curdate(), interval 6 day) group by date(sent_at) order by date(sent_at)").getResultList();
            List<DashboardStats.DailyCount> activity = new ArrayList<>();
            for (Object[] row : rows) activity.add(new DashboardStats.DailyCount(String.valueOf(row[0]), ((Number) row[1]).longValue()));
            return new DashboardStats(conversations, messages, participants, spam, activity);
        }
    }

    public AnalyticsData getAnalytics() {
        try (Session session = sessionFactory.openSession()) {
            List<DashboardStats.DailyCount> daily = toDaily(session.createNativeQuery("select date(sent_at), count(*) from message group by date(sent_at) order by date(sent_at) desc limit 30").getResultList());
            java.util.Collections.reverse(daily);
            List<DashboardStats.DailyCount> hourly = toDaily(session.createNativeQuery("select hour(sent_at), count(*) from message group by hour(sent_at) order by hour(sent_at)").getResultList());
            List<AnalyticsData.NamedCount> participants = toNamed(session.createNativeQuery("select p.display_name, count(m.id) from participant p join message m on m.participant_id = p.id group by p.id, p.display_name order by count(m.id) desc limit 8").getResultList());
            List<AnalyticsData.NamedCount> conversations = toNamed(session.createNativeQuery("select c.title, count(m.id) from conversation c left join message m on m.conversation_id = c.id group by c.id, c.title order by count(m.id) desc limit 8").getResultList());
            return new AnalyticsData(daily, hourly, participants, conversations);
        }
    }
    private List<DashboardStats.DailyCount> toDaily(List<Object[]> rows) {
        List<DashboardStats.DailyCount> result = new ArrayList<>();
        for (Object[] row : rows) result.add(new DashboardStats.DailyCount(String.valueOf(row[0]), ((Number) row[1]).longValue()));
        return result;
    }
    private List<AnalyticsData.NamedCount> toNamed(List<Object[]> rows) {
        List<AnalyticsData.NamedCount> result = new ArrayList<>();
        for (Object[] row : rows) result.add(new AnalyticsData.NamedCount(String.valueOf(row[0]), ((Number) row[1]).longValue()));
        return result;
    }
}
