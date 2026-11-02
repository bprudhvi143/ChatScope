package com.chatloganalyzer.service;

import com.chatloganalyzer.dao.MessageDao;
import com.chatloganalyzer.model.Message;
import java.util.List;
import java.time.LocalDateTime;

public class MessageService {
    private final MessageDao messageDao;
    public MessageService(MessageDao messageDao) { this.messageDao = messageDao; }
    public List<Message> search(String query, int page) {
        return query == null || query.isBlank() ? List.of() : messageDao.search(query.trim(), page, 20);
    }
    public long countSearch(String query) { return query == null || query.isBlank() ? 0 : messageDao.countSearch(query.trim()); }
    public List<Message> findByConversation(long conversationId) { return messageDao.findByConversation(conversationId); }
}

