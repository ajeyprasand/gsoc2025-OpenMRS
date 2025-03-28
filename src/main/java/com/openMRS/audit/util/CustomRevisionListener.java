package com.openMRS.audit.util;

import java.time.LocalDateTime;

import org.hibernate.envers.RevisionListener;
import org.springframework.stereotype.Component;

import com.openMRS.audit.entity.CustomRevisionEntity;
import com.openMRS.audit.security.AuditContext;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomRevisionListener implements RevisionListener{

    @Override
    public void newRevision(Object revisionEntity) {
        CustomRevisionEntity revEntity = (CustomRevisionEntity) revisionEntity;
        String username = AuditContext.getUsername();
        LocalDateTime timestamp = LocalDateTime.now();
        revEntity.setUsername(username);
        revEntity.setModifiedTime(timestamp);
    }

}