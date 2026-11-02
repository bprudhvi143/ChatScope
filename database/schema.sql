CREATE DATABASE IF NOT EXISTS chat_log_analyzer
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

USE chat_log_analyzer;

CREATE TABLE conversation (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    source_type VARCHAR(40) NOT NULL,
    imported_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE participant (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    display_name VARCHAR(150) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_participant_display_name (display_name)
) ENGINE=InnoDB;

CREATE TABLE conversation_participant (
    conversation_id BIGINT UNSIGNED NOT NULL,
    participant_id BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (conversation_id, participant_id),
    CONSTRAINT fk_cp_conversation FOREIGN KEY (conversation_id)
        REFERENCES conversation(id) ON DELETE CASCADE,
    CONSTRAINT fk_cp_participant FOREIGN KEY (participant_id)
        REFERENCES participant(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE message (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    conversation_id BIGINT UNSIGNED NOT NULL,
    participant_id BIGINT UNSIGNED NULL,
    sent_at DATETIME NOT NULL,
    body TEXT NOT NULL,
    PRIMARY KEY (id),
    KEY idx_message_conversation_sent_at (conversation_id, sent_at),
    KEY idx_message_participant (participant_id),
    CONSTRAINT fk_message_conversation FOREIGN KEY (conversation_id)
        REFERENCES conversation(id) ON DELETE CASCADE,
    CONSTRAINT fk_message_participant FOREIGN KEY (participant_id)
        REFERENCES participant(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- Run after creating the schema if you want a small manual test dataset.
-- INSERT INTO conversation (title, source_type) VALUES ('Team chat', 'sample');

