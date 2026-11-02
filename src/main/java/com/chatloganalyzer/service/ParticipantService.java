package com.chatloganalyzer.service;

import com.chatloganalyzer.dao.ParticipantDao;
import com.chatloganalyzer.model.ParticipantSummary;
import java.util.List;

public class ParticipantService {
    private final ParticipantDao dao;
    public ParticipantService(ParticipantDao dao) { this.dao = dao; }
    public List<ParticipantSummary> findAll() { return dao.findAll(); }
}
