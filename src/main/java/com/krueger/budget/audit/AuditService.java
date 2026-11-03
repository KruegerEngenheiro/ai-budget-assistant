package com.krueger.budget.audit;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Writes and reads the trust trail. Any part of the system can call record(...)
 * to leave evidence of what happened.
 */
@Service
public class AuditService {

    private final AuditLogRepository repository;

    public AuditService(AuditLogRepository repository) {
        this.repository = repository;
    }

    public void record(String channel, String event, String detail) {
        AuditLog log = new AuditLog();
        log.setCreatedAt(LocalDateTime.now());
        log.setChannel(channel);
        log.setEvent(event);
        log.setDetail(detail);
        repository.save(log);
    }

    public List<AuditLog> recent() {
        return repository.findAllByOrderByCreatedAtDesc();
    }
}
