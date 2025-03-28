package com.openMRS.audit.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.openMRS.audit.dto.FilterDto;
import com.openMRS.audit.serviceImpl.AuditServiceImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@RestController
@Validated
public class AuditController {

    @Autowired
    private AuditServiceImpl auditService;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/audit")
    public ResponseEntity<?> getAudit(@RequestBody FilterDto filters) {
        try{
            List<Object> response = auditService.getAudit(filters);
            return ResponseEntity.ok().body(response);
        } catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
