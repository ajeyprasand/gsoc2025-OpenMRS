package com.openMRS.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openMRS.audit.entity.CustomRevisionEntity;

public interface CustomRevisionRepository extends JpaRepository<CustomRevisionEntity, Integer> {

}
