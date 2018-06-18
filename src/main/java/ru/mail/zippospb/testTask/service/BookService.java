package ru.mail.zippospb.testTask.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ru.mail.zippospb.testTask.entity.Book;


import java.io.IOException;
import java.util.List;

public interface BookService {
    List<Book> findAll();
    Page<Book> findAllByPage(Pageable pageable);
    Book findById(Long id);
    Book save(Book book);
    Book update(Book book, long id);
    void delete(Book book);


    Page<Book> search(String term, int printYear, Pageable pageable);
    Page<Book> search(String term, int printYear, boolean readAlReady, Pageable pageable);

    Book uploadFileData(Book book, MultipartFile file) throws IOException;
}
