package com.chatloganalyzer.service;

import com.chatloganalyzer.model.Conversation;
import com.chatloganalyzer.model.ImportResult;
import com.chatloganalyzer.model.Message;
import com.chatloganalyzer.model.Participant;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ChatImportService {
    private final SessionFactory sessionFactory;
    public ChatImportService(SessionFactory sessionFactory) { this.sessionFactory = sessionFactory; }

    public ImportResult importCsv(Reader reader) throws IOException {
        List<ImportRow> rows = parseCsv(reader);
        if (rows.isEmpty()) throw new IllegalArgumentException("The CSV has no valid message rows.");
        String title = rows.get(0).conversationTitle();
        int imported = 0;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                Conversation conversation = session.createQuery("from Conversation c where c.title = :title and c.sourceType = :source", Conversation.class)
                        .setParameter("title", title).setParameter("source", rows.get(0).sourceType()).uniqueResult();
                if (conversation == null) { conversation = new Conversation(title, rows.get(0).sourceType()); session.persist(conversation); session.flush(); }
                for (ImportRow row : rows) {
                    Participant participant = session.createQuery("from Participant p where p.displayName = :name", Participant.class)
                            .setParameter("name", row.sender()).uniqueResult();
                    if (participant == null) { participant = new Participant(row.sender()); session.persist(participant); session.flush(); }
                    session.createNativeQuery("insert ignore into conversation_participant (conversation_id, participant_id) values (:conversationId, :participantId)")
                            .setParameter("conversationId", conversation.getId()).setParameter("participantId", participant.getId()).executeUpdate();
                    session.persist(new Message(conversation, participant, row.sentAt(), row.body()));
                    imported++;
                }
                tx.commit();
            } catch (RuntimeException exception) { tx.rollback(); throw exception; }
        }
        return new ImportResult(imported, 0, title);
    }

    private List<ImportRow> parseCsv(Reader reader) throws IOException {
        BufferedReader buffered = new BufferedReader(reader);
        String header = buffered.readLine();
        if (header == null || !header.replace("\ufeff", "").toLowerCase().contains("conversation_title")) {
            throw new IllegalArgumentException("Use a CSV header: conversation_title,source_type,sender,sent_at,message");
        }
        List<ImportRow> rows = new ArrayList<>();
        String line;
        while ((line = buffered.readLine()) != null) {
            if (line.isBlank()) continue;
            List<String> values = splitCsv(line);
            if (values.size() != 5) throw new IllegalArgumentException("Each row must have five columns. Check quoted commas in row " + (rows.size() + 2) + ".");
            if (values.stream().anyMatch(String::isBlank)) throw new IllegalArgumentException("Blank values are not allowed in row " + (rows.size() + 2) + ".");
            rows.add(new ImportRow(values.get(0).trim(), values.get(1).trim(), values.get(2).trim(), parseDate(values.get(3).trim()), values.get(4).trim()));
        }
        return rows;
    }
    private LocalDateTime parseDate(String value) {
        for (DateTimeFormatter formatter : List.of(DateTimeFormatter.ISO_LOCAL_DATE_TIME, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))) {
            try { return LocalDateTime.parse(value, formatter); } catch (DateTimeParseException ignored) { }
        }
        throw new IllegalArgumentException("Use sent_at as yyyy-MM-dd HH:mm or ISO date-time.");
    }
    private List<String> splitCsv(String line) {
        List<String> cells = new ArrayList<>(); StringBuilder cell = new StringBuilder(); boolean quoted = false;
        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            if (character == '"') { if (quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') { cell.append('"'); i++; } else quoted = !quoted; }
            else if (character == ',' && !quoted) { cells.add(cell.toString()); cell.setLength(0); }
            else cell.append(character);
        }
        if (quoted) throw new IllegalArgumentException("An import row has an unmatched quote.");
        cells.add(cell.toString()); return cells;
    }
    private record ImportRow(String conversationTitle, String sourceType, String sender, LocalDateTime sentAt, String body) { }
}
