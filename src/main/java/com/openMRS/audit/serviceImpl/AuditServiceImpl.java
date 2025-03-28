package com.openMRS.audit.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Service;

import com.openMRS.audit.dto.FilterDto;
import com.openMRS.audit.dto.ResponseDto;
import com.openMRS.audit.entity.Book;
import com.openMRS.audit.entity.CustomRevisionEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class AuditServiceImpl {
    
    @PersistenceContext
    private EntityManager entityManager;

    public List<Object> getAudit(FilterDto filters){
        String user = filters.getUser();
        String revType = filters.getRevType();
        String starDateTime = filters.getStarDateTime();
        String endDateTime = filters.getEndDateTime();
        String entity = filters.getEntity();
        String fieldName = filters.getFieldName();
        String fieldValue = filters.getFieldValue();

        AuditReader reader = AuditReaderFactory.get(entityManager);
        AuditQuery query = null;
        
        if(entity == null){
            throw new IllegalArgumentException("Entity is mandatory");
        }

        try {
            Class<?> entityClass  = Class.forName("com.openMRS.audit.entity." + entity);
            query = reader.createQuery().forRevisionsOfEntity(entityClass, false, true);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Cannot find entity with name: " + entity);
        }

        if(fieldName != null && fieldValue != null){
            query.add(AuditEntity.property(fieldName).eq(fieldValue));
        }

        if(user != null){
            query.add(AuditEntity.revisionProperty("username").eq(user));
        }

        if(revType != null){
            try {
                RevisionType revisionType = RevisionType.valueOf(revType);
                query.add(AuditEntity.revisionType().eq(revisionType));
            }catch(Exception e) {
                throw new IllegalArgumentException("Invalid RevisionType: " + revType);
            }    
        }

        if(starDateTime != null){
            try{
                LocalDateTime start = LocalDateTime.parse(starDateTime);
                query.add(AuditEntity.revisionProperty("modifiedTime").ge(start));    
            }catch(Exception e) {
                throw new IllegalArgumentException("StartDateTime must be in proper format");
            }    
        }

        if(endDateTime != null){
            try{
                LocalDateTime end = LocalDateTime.parse(endDateTime);
                query.add(AuditEntity.revisionProperty("modifiedTime").le(end));
            }catch(Exception e) {
                throw new IllegalArgumentException("EndDateTime must be in proper format");
            }    
        }

        List<Object> responseList = processQueryResult(query.getResultList());
        return responseList;
    }

    public List<Object> getAuditByUser(String user){
        AuditReader reader = AuditReaderFactory.get(entityManager);
        AuditQuery query = reader.createQuery().forRevisionsOfEntity(Book.class, false, true);
        List<Object> responseList = processQueryResult(query.getResultList());
        return responseList;
    }

    private List<Object> processQueryResult(List<?> queryResult){
        List<Object> responseList = new ArrayList<>();
        for (Object object : queryResult) {
            Object[] obj = (Object[]) object;
            Book book = (Book) obj[0];
            CustomRevisionEntity revision = (CustomRevisionEntity) obj[1];
            RevisionType type = (RevisionType) obj[2];

            ResponseDto<Book> response = new ResponseDto<>();
            response.setUsername(revision.getUsername());
            response.setRevision(revision.getId());
            response.setType(type);
            response.setTimestamp(revision.getModifiedTime());
            response.setData(book);
            responseList.add(response);
        }
        return responseList;
    }
}
