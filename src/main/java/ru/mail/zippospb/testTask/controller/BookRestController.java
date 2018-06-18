package ru.mail.zippospb.testTask.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.mail.zippospb.testTask.entity.Book;
import ru.mail.zippospb.testTask.service.BookService;

@Controller
@RequestMapping(path = "/books/api")
public class BookRestController {
    private final Logger logger = LoggerFactory.getLogger(BookRestController.class);

    private BookService bookService;

    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Book> getAllBooks(){
        return bookService.findAll();
    }

    @GetMapping(path = "")
    public @ResponseBody Page<Book> getPageBooks(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order
    ){
        return bookService.findAllByPage(Controllers.getPageRequest(page, sortBy, order));
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody Book findBookBiId(@PathVariable Long id){
        return bookService.findById(id);
    }

    @PostMapping(value = "")
    public @ResponseBody Book create(@RequestBody Book book){
        logger.info("Creating book " + book);
        Book createBook = bookService.save(book);
        logger.info("Created book " + book);
        return createBook;
    }

    @PutMapping(value = "/{id}")
    public @ResponseBody Book update(@RequestBody Book book, @PathVariable Long id){
        logger.info(("Updating book " + book));
        book = bookService.update(book, id);
        logger.info("Book update successfully with info: " + book);
        return book;
    }

    @DeleteMapping(value = "/{id}")
    public @ResponseBody ResponseEntity<?> delete(@PathVariable Long id){
        logger.info("Deleting book with id " + id);
        Book book = bookService.findById(id);
        if(book == null){
            logger.info("Book with id " + id + " not found");
            return null;
        }
        bookService.delete(book);
        return ResponseEntity.ok("deleted Book #" + id);
    }

    @GetMapping(path = "/search")
    public @ResponseBody Page<Book> search(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @RequestParam(required = false, defaultValue = "") String searchTerm,
            @RequestParam(required = false, defaultValue = "0") int afterYear,
            @RequestParam(required = false, defaultValue = "") String ready
    ){
        Pageable pageRequest = Controllers.getPageRequest(page,sortBy,order);
        if(!ready.isEmpty() && (ready.equals("true") || ready.equals("false"))){

            return bookService.search(searchTerm, afterYear, Boolean.parseBoolean(ready), pageRequest);
        }
        return bookService.search(searchTerm, afterYear, pageRequest);
    }


}
