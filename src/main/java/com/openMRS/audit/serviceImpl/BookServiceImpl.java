package com.openMRS.audit.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openMRS.audit.dto.BookDto;
import com.openMRS.audit.entity.Book;
import com.openMRS.audit.repository.BookRepository;
import com.openMRS.audit.security.AuditContext;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class BookServiceImpl {
    @Autowired
    private BookRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    public BookDto saveBook(BookDto bookDto) {
        AuditContext.setUsername("Ajey");
        Book entity = new Book();
        entity.setName(bookDto.getName());
        entity.setPages(bookDto.getPages());
        bookDto.setId(repository.save(entity).getId());
        return bookDto;
    }

    @Transactional
    public String updateBook(Integer id, Integer pages) {
        AuditContext.setUsername("Ajey");
        Book book = repository.findById(id).get();
        book.setPages(pages);
        return "Book Updated";
    }

    public String deleteBook(Integer id) {
        AuditContext.setUsername("Ajey");
        repository.deleteById(id);
        return "Book deleted";
    }
    

}
