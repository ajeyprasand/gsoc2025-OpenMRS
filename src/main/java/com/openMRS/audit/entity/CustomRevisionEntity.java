package com.openMRS.audit.entity;

import java.time.LocalDateTime;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import com.openMRS.audit.util.CustomRevisionListener;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "CUSTOM_REV_INFO")
@RevisionEntity( CustomRevisionListener.class )
public class CustomRevisionEntity extends DefaultRevisionEntity {
	private String username;
	private LocalDateTime modifiedTime;
}
