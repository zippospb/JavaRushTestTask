package ru.mail.zippospb.testTask;

import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import ru.mail.zippospb.testTask.controller.BookRestController;
import ru.mail.zippospb.testTask.entity.Book;
import ru.mail.zippospb.testTask.service.BookService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.*;

public class BookServiceTest {
    private final List<Book> books = new ArrayList<>();

    @Before
    public void initBooks(){
        Book book = new BookBuilder()
                .id(1L)
                .author("Иван Портянкин")
                .title("Swing. Эффектные пользовательские интерфейсы")
                .description("Создание пользовательских интерфейсов Java-приложений с помощью библиотеки Swing и Java Foundation Classes")
                .isbn("978-5-85582-305-9")
                .printYear(2011)
                .readAlready(false)
                .build();

        books.add(book);
    }

    @Test
    public void getAllBooksTest(){
        BookService bookService = mock(BookService.class);
        when(bookService.findAll()).thenReturn(books);

        BookRestController bookRestController = new BookRestController(bookService);
        ReflectionTestUtils.setField(bookRestController, "bookService", bookService);

        ExtendedModelMap uiModel = new ExtendedModelMap();
        uiModel.addAttribute("books", bookRestController.getAllBooks());
        assertEquals(1, books.size());
    }

    @Test
    public void getPageBookTest(){
        Sort sort = new Sort(Sort.Direction.ASC, "id");

        Page<Book> bookPage = new PageBuilder<Book>()
                .elements(books)
                .pageRequest(PageRequest.of(0,10, sort))
                .totalElements(1)
                .build();
        BookService bookService = mock(BookService.class);

        when(bookService.findAllByPage(isA(Pageable.class))).thenReturn(bookPage);

        BookRestController controller = new BookRestController(bookService);
        ReflectionTestUtils.setField(controller, "bookService", bookService);

        Page<Book> books = controller.getPageBooks(1, "id", "ask");

        verify(bookService).findAllByPage(notNull());

        assertEquals(1, books.getTotalElements());
        assertEquals(1, books.getTotalPages());
    }

    @Test
    public void findBookByIdTest() throws Exception{
        BookService bookService = mock(BookService.class);
        when(bookService.findById(1L)).thenAnswer((Answer<Book>) invocationOnMock -> {
            for(Book book : books){
                if(book.getId() == 1L) return book;
            }
            return null;
        });

        BookRestController bookRestController = new BookRestController(bookService);
        ReflectionTestUtils.setField(bookRestController, "bookService", bookService);

        ExtendedModelMap uiModel = new ExtendedModelMap();
        uiModel.addAttribute("books", bookRestController.getAllBooks());
        assertEquals(1, books.size());
    }

    @Test
    public void createTest(){
        final Book newBook = new Book();
        newBook.setId(999L);
        newBook.setAuthor("Джошуа Блох");
        newBook.setTitle("Java. Эффективное программирование");
        newBook.setDescription("Первое издание книги \"Java. Эффективное программирование\", содержащей пятьдесят семь ценных правил, предлагает решение задач программирования, с которыми большинство разработчиков сталкиваются каждый день");
        newBook.setIsbn("978-5-85582-347-9");
        newBook.setPrintYear(2014);
        newBook.setReadAlready(false);

        BookService bookService = mock(BookService.class);
        when(bookService.save(newBook)).thenAnswer((Answer<Book>) invocationOnMock -> {
            books.add(newBook);
            return newBook;
        });

        BookRestController bookRestController = new BookRestController(bookService);
        ReflectionTestUtils.setField(bookRestController, "bookService", bookService);
        Book book = bookRestController.create(newBook);
        assertEquals(999L, book.getId());
        assertEquals("Джошуа Блох", book.getAuthor());
        assertEquals("978-5-85582-347-9", book.getIsbn());

        assertEquals(2, books.size());
    }

    @Test
    public void updateTest() throws Exception{
        final Book updateDataBook = new BookBuilder().readAlready(true).build();

        BookService bookService = mock(BookService.class);
        when(bookService.update(updateDataBook, 1L)).thenAnswer((Answer<Book>) invocationOnMock -> {
            Book book = books.get(0);
            if (updateDataBook.getAuthor() != null) book.setAuthor(book.getAuthor());
            if (updateDataBook.getTitle() != null) book.setTitle(book.getTitle());
            if (updateDataBook.getDescription() != null) book.setDescription(book.getDescription());
            if (updateDataBook.getIsbn() != null) book.setIsbn(book.getIsbn());
            if (updateDataBook.getPrintYear() != 0) book.setPrintYear(book.getPrintYear());
            book.setReadAlready(true);
            return book;
        });

        BookRestController bookRestController = new BookRestController(bookService);
        ReflectionTestUtils.setField(bookRestController, "bookService", bookService);

        Book book = bookRestController.update(updateDataBook, 1L);
        assertEquals(1L, book.getId());
        assertTrue(book.isReadAlready());

        assertEquals(1, books.size());
    }

    @Test
    public void deleteTest() throws Exception{
        BookService bookService = mock(BookService.class);

        doAnswer((Answer<Void>) invocationOnMock -> {
            books.remove(books.get(0));
            return null;
        }).when(bookService).delete(any(Book.class));
        doAnswer((Answer<Book>) invocationOnMock -> books.get(0)).when(bookService).findById(any(Long.class));

        BookRestController bookRestController = new BookRestController(bookService);
        ReflectionTestUtils.setField(bookRestController, "bookService", bookService);

        bookRestController.delete(1L);

        assertEquals(0, books.size());
    }
}
