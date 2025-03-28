package com.openMRS.audit.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.openMRS.audit.dto.BookDto;
import com.openMRS.audit.serviceImpl.BookServiceImpl;

@RestController
public class BookController {
        
    @Autowired
    private BookServiceImpl bookService;

    @PostMapping("/save")
    public ResponseEntity<BookDto> saveBook(@RequestBody BookDto bookDto) {
        return ResponseEntity.ok().body(bookService.saveBook(bookDto));
    }

    @PutMapping("/update/{id}/{pages}")
    public ResponseEntity<String> saveBook(@PathVariable Integer id, @PathVariable Integer pages) {
        return ResponseEntity.ok().body(bookService.updateBook(id, pages));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Integer id) {
        return ResponseEntity.ok().body(bookService.deleteBook(id));
    }

}
