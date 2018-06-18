package ru.mail.zippospb.testTask.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.mail.zippospb.testTask.entity.Book;
import ru.mail.zippospb.testTask.repository.BookRepository;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;


@Service("bookService")
@Repository
@Transactional
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository repository;

    @Override
    @Transactional
    public List<Book> findAll() {
        return Lists.newArrayList(repository.findAll());
    }

    @Override
    public Page<Book> findAllByPage(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Transactional
    public Book findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Book update(Book book, long id) {
        Book bookToUpdate = findById(id);

        if(bookToUpdate == null) return null;

        if(book.getTitle() != null) bookToUpdate.setTitle(book.getTitle());
        if(book.getDescription() != null) bookToUpdate.setDescription(book.getDescription());
        if(book.getAuthor() != null) bookToUpdate.setAuthor(book.getAuthor());
        if(book.getIsbn() != null) bookToUpdate.setIsbn(book.getIsbn());
        if(book.getPrintYear() != 0) bookToUpdate.setPrintYear(book.getPrintYear());
        if(book.getImageData() != null) bookToUpdate.setImageData(book.getImageData());
        bookToUpdate.setReadAlready(book.isReadAlready());

        return repository.save(bookToUpdate);
    }

    @Override
    public Book save(Book book) {

        return repository.save(book);
    }

    @Override
    public void delete(Book book) {
        repository.delete(book);
    }

    @Override
    public Page<Book> search(String term, int printYear, Pageable pageable) {
        return repository.findBySearchParams(term, printYear, pageable);
    }

    @Override
    public Page<Book> search(String term, int printYear, boolean readAlReady, Pageable pageable) {
        return repository.findBySearchParamsAndReadAlready(term, printYear, readAlReady, pageable);
    }

    @Override
    public Book uploadFileData(Book book, MultipartFile file) throws IOException {
//        String fileName = file.getName();
//        Path fileDir = Paths.get(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\");
//        Path newFile = Files.createTempFile(fileDir, null, "." + file.getOriginalFilename().split("\\.")[1]);
//        Path oldFile = Paths.get(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\" + book.getImageStr());
//        if(Files.exists(oldFile)){
//            Files.delete(oldFile);
//        }
//        Files.write(newFile, file.getBytes(), StandardOpenOption.CREATE);
//        book.setImageStr(newFile.getFileName().toString());
//        return book;

        if (!file.isEmpty()){
            String fileName = file.getOriginalFilename();

            book.setImageData(file.getBytes());
            book.setImageStr(fileName);
        }

        return book;
    }
}
