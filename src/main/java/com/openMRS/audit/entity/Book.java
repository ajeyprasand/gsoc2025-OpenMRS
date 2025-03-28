package com.openMRS.audit.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;


@Entity
@NoArgsConstructor
@Data
@Audited
public class Book {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private Integer pages;
}
