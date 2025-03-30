package com.openMRS.audit.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openMRS.audit.dto.FilterDto;
import com.openMRS.audit.serviceImpl.AuditServiceImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@RestController
public class AuditController {

    @Autowired
    private AuditServiceImpl auditService;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/audit")
    public ResponseEntity<?> getAudit(
        @RequestParam String entity,
        @RequestParam(required = false) String field,
        @RequestParam(required = false) String user,
        @RequestParam(required = false) String revType,
        @RequestParam(required = false) String startDateTime,
        @RequestParam(required = false) String endDateTime
    )
    {
        try{
            FilterDto filters = new FilterDto(entity, field, user, revType, startDateTime, endDateTime);
            List<Object> response = auditService.getAudit(filters);
            return ResponseEntity.ok().body(response);
        } catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/entities")
    public ResponseEntity<List<String>> getEntitiesList(){
        return ResponseEntity.ok().body(auditService.getEntitiesList());
    }

    @GetMapping("/fields")
    public ResponseEntity<List<String>> getFieldsList(@RequestParam String entity){
        return ResponseEntity.ok().body(auditService.getFieldsList(entity));
    }

    @RequestMapping("/dummy")
    public String index(Model model) {
        model.addAttribute("greeting", "Hello");
        return "index";
    }

}
