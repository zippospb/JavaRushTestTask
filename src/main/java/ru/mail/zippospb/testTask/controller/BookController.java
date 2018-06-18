package ru.mail.zippospb.testTask.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.mail.zippospb.testTask.entity.Book;
import ru.mail.zippospb.testTask.service.BookService;

import java.io.IOException;
import java.nio.file.Files;

@Controller
@RequestMapping("/books")
public class BookController {
    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping(path = "")
    public String viewBookList(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @RequestParam(required = false, defaultValue = "") String term,
            @RequestParam(required = false, defaultValue = "0") int afterYear,
            @RequestParam(required = false, defaultValue = "") String ready,
            Model uiModel
    ){
        Pageable pageRequest = Controllers.getPageRequest(page, sortBy, order);
        Page<Book> books;

        if (!ready.isEmpty() && (ready.equals("true") || ready.equals("false")))
            books = bookService.search(term, afterYear-1, Boolean.parseBoolean(ready), pageRequest);
        else
            books = bookService.search(term, afterYear-1, pageRequest);

        uiModel.addAttribute("books", books);
        uiModel.addAttribute("current", page > 0 ? page - 1 : 0);
        uiModel.addAttribute("term", term);
        uiModel.addAttribute("year", afterYear);
        uiModel.addAttribute("ready", ready);


        return "books/list";
    }

    @GetMapping("/{id}")
    public String viewBook (@PathVariable long id, Model uiModel){
        Book book = bookService.findById(id);

        uiModel.addAttribute("book", book);

        return "books/view";
    }

    @PostMapping(path = "/delete/{id}")
    public String deleteBook(@PathVariable long id){
        Book book = bookService.findById(id);
        if(book != null){
            bookService.delete(book);
        }
        return "redirect:/books";
    }

    @PostMapping(path = "/ready/{id}")
    public String isReadyBook(@PathVariable long id, RedirectAttributes redirectAttributes){
        Book book = bookService.findById(id);
        book.setReadAlready(true);
        bookService.save(book);

        redirectAttributes.addAttribute("id", id);

        return "redirect:/books/{id}";
    }

    @GetMapping(path = "/edition/{id}")
    public String newEditionBook(@PathVariable long id, Model uiModel){
        Book book = bookService.findById(id);

        uiModel.addAttribute("book", book);
        return "books/edition";
    }

    @PostMapping(path = "/edition/{id}")
    public String submitEditBook(
            @ModelAttribute Book book,
            @PathVariable long id,
            @RequestParam MultipartFile file,
            RedirectAttributes redirectAttributes
            ) throws IOException {
        bookService.uploadFileData(book, file);
        bookService.update(book, id);
        return "redirect:/books/{id}";
    }

    @GetMapping(path = "/add")
    public String addNewBook(Model uiModel){
        uiModel.addAttribute("book", new Book());
        return "books/newBook";
    }

    @PostMapping(path = "/add")
    public String submitAddNewBook(
            @ModelAttribute Book book,
            @RequestParam MultipartFile file
    )throws IOException{
        bookService.uploadFileData(book, file);
        bookService.save(book);

        return "redirect:/books";
    }

    @GetMapping(path = "/{id}/image")
    @ResponseBody
    public ResponseEntity<byte[]> getImageData(@PathVariable Long id){

        byte[] imageData = bookService.findById(id).getImageData();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                .header(HttpHeaders.CACHE_CONTROL, CacheControl.noCache().getHeaderValue())
                .body(imageData);
    }
}
