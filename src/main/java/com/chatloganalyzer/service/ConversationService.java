package com.chatloganalyzer.service;

import com.chatloganalyzer.dao.ConversationDao;
import com.chatloganalyzer.model.Conversation;
import com.chatloganalyzer.model.ConversationSummary;
import java.util.List;

public class ConversationService {
    private final ConversationDao conversationDao;
    public ConversationService(ConversationDao conversationDao) { this.conversationDao = conversationDao; }
    public List<ConversationSummary> findAll() { return conversationDao.findAll(); }
    public Conversation findById(long id) { return conversationDao.findById(id); }
}
