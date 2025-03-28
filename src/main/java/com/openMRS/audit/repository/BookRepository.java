package com.openMRS.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openMRS.audit.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
