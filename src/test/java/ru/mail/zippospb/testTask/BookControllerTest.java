package ru.mail.zippospb.testTask;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestTaskApplication.class)
@WebAppConfiguration
public class BookControllerTest {
    private static final String PAGE_NUMBER_STRING = "2";
//    private static final int PAGE_NUMBER = Integer.parseInt(PAGE_NUMBER_STRING) - 1;
//    private static final int PAGE_SIZE = 10;
    private static final String FIELD_NAME_TITLE = "printYear";
    private static final String SORT_ORDER = "desc";
    private static final Long TEST_BOOK_ID = 1L;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Before
    public void setUp(){
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    public void viewBooksList_shouldReturnHttpResponseStatusOkNoParam() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(this.contentType))
                .andExpect(content().string(containsString("список книг")))
    ;}

    @Test
    public void viewBooksList_shouldReturnHttpResponseStatusOkWithParam() throws Exception{
        mockMvc.perform(get("/books")
                .param("page", PAGE_NUMBER_STRING)
                .param("sortBy", FIELD_NAME_TITLE)
                .param("order", SORT_ORDER)
        ).andExpect(status().isOk());
    }

    @Test
    public void viewBook_shouldReturnHttpResponseStatusIsOKAndTitle() throws Exception{
        mockMvc.perform(get("/books/{id}", TEST_BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("JAVA EE APPLICATIONS ON THE ORACLE JAVA CLOUD: DEVELOP, DEPLOY, MONITOR")));
    }

    @Test
    public void deleteBook_shouldReturnHttpResponseStatusIsRedirection() throws Exception{
        mockMvc.perform(put("/books/delete/{id}", TEST_BOOK_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

    }

    @Test
    public void isReadyBook_shouldReturnHttpResponseStatusIsRedirection() throws Exception{
        mockMvc.perform(post("/books/ready/{id}", TEST_BOOK_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/" + TEST_BOOK_ID));
    }
}
