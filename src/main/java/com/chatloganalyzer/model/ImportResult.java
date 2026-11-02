package com.chatloganalyzer.model;

public record ImportResult(int importedMessages, int skippedRows, String conversationTitle) { }
