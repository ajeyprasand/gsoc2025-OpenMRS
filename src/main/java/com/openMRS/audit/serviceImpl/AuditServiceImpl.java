package com.openMRS.audit.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Object> getAudit(FilterDto filters) throws IllegalArgumentException{
        String user = filters.getUser();
        String revType = filters.getRevType();
        String starDateTime = filters.getStarDateTime();
        String endDateTime = filters.getEndDateTime();
        String entityName = filters.getEntity();
        String fieldName = filters.getField();
       
        AuditReader reader = AuditReaderFactory.get(entityManager);
        AuditQuery query = null;
        
        if(entityName == null){
            throw new IllegalArgumentException("Entity Name is mandatory");
        }

        Class<?> entityClass;
        try{
            entityClass = Class.forName(entityName);
        }catch(ClassNotFoundException e){
            throw new IllegalArgumentException("Entity Name not found");
        }

        query = reader.createQuery().forRevisionsOfEntity(entityClass, false, true);

        if(fieldName != null && !fieldName.isEmpty()){
            query.add(AuditEntity.property(fieldName+"_MOD").eq(true));
        }

        if(user != null && !user.isEmpty()){
            query.add(AuditEntity.revisionProperty("username").eq(user));
        }

        if(revType != null && !revType.isEmpty()){
            try {
                RevisionType revisionType = RevisionType.valueOf(revType);
                query.add(AuditEntity.revisionType().eq(revisionType));
            }catch(Exception e) {
                throw new IllegalArgumentException("Invalid RevisionType: " + revType);
            }    
        }

        if(starDateTime != null && !starDateTime.isEmpty()){
            try{
                LocalDateTime start = LocalDateTime.parse(starDateTime);
                query.add(AuditEntity.revisionProperty("timestamp").ge(start));    
            }catch(Exception e) {
                throw new IllegalArgumentException("StartDateTime must be in proper format");
            }    
        }

        if(endDateTime != null && !endDateTime.isEmpty()){
            try{
                LocalDateTime end = LocalDateTime.parse(endDateTime);
                query.add(AuditEntity.revisionProperty("timestamp").le(end));
            }catch(Exception e) {
                throw new IllegalArgumentException("EndDateTime must be in proper format");
            }    
        }

        List<Object> responseList = processQueryResult(query.getResultList());
        return responseList;
    }

    public List<String> getEntitiesList(){
        return entityManager.getMetamodel().getEntities().stream()
                            .map(e -> e.getName())
                            .filter(a -> !a.equals("CustomRevisionEntity") )
                            .collect(Collectors.toList());
    }

    public List<String> getFieldsList(String entityName){
        return entityManager.getMetamodel().getEntities().stream()
                            .filter(e -> e.getName().equals(entityName)).findFirst()
                            .map(e -> {
                                    return e.getDeclaredAttributes().stream()
                                    .map(a -> a.getName())
                                    .filter(a -> !a.equals("id"))
                                    .collect(Collectors.toList());
                                })
                            .get();
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
