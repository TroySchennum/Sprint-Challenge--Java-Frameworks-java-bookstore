package com.lambdaschool.bookstore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.bookstore.BookstoreApplication;
import com.lambdaschool.bookstore.models.Book;
import com.lambdaschool.bookstore.models.Section;
import com.lambdaschool.bookstore.models.Wrote;
import com.lambdaschool.bookstore.services.BookService;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)

/*****
 * Due to security being in place, we have to switch out WebMvcTest for SpringBootTest
 * @WebMvcTest(value = BookController.class)
 */
@SpringBootTest(classes = BookstoreApplication.class)

/****
 * This is the user and roles we will use to test!
 */
@WithMockUser(username = "admin", roles = {"ADMIN", "DATA"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BookControllerTest
{
    /******
     * WebApplicationContext is needed due to security being in place.
     */
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    List<Book> booksList = new ArrayList<>();

    @Before
    public void setUp() throws
            Exception
    {
        /*****
         * The following is needed due to security being in place!
         */
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        booksList = new ArrayList<>();

        Section s1 = new Section("Fiction");
        s1.setSectionid(1);
        Section s2 = new Section("Technology");
        s1.setSectionid(2);
        Section s3 = new Section("Travel");
        s1.setSectionid(3);
        Section s4 = new Section("Business");
        s1.setSectionid(4);
        Section s5 = new Section("Religion");
        s1.setSectionid(5);



        Book b1 = new Book("Flatterland", "9780738206752", 2001, s1);

        b1.setBookid(9);
        booksList.add(b1);

        Book b2 = new Book("Digital Fortess", "9788489367012", 2007, s1);

        b2.setBookid(14);
        booksList.add(b2);

        Book b3 = new Book("The Da Vinci Code", "9780307474278", 2009, s1);

        b3.setBookid(134);
        booksList.add(b3);

        Book b4 = new Book("Essentials of Finance", "1314241651234", 0, s4);

        b4.setBookid(132);
        booksList.add(b4);

        Book b5 = new Book("Calling Texas Home", "1885171382134", 2000, s3);

        b5.setBookid(135);
        booksList.add(b5);

        /*****
         * Note that since we are only testing bookstore data, you only need to mock up bookstore data.
         * You do NOT need to mock up user data. You can. It is not wrong, just extra work.
         */
    }

    @After
    public void tearDown() throws
            Exception
    {
    }

    @Test
    public void a_listAllBooks() throws
            Exception
    {
        String apiUrl = "/books/books";
        Mockito.when(bookService.findAll()).thenReturn(booksList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(booksList);

        assertEquals(er, tr);
    }

    @Test
    public void b_getBookById() throws
            Exception
    {
        String apiUrl = "/books/book/9";
        Mockito.when(bookService.findBookById(9)).thenReturn(booksList.get(0));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(booksList.get(0));

        assertEquals(er, tr);
    }

    @Test
    public void c_getNoBookById() throws
            Exception
    {
        String apiUrl = "/books/book/777";
        Mockito.when(bookService.findBookById(777)).thenReturn(null);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();


        String er = "";

        assertEquals(er, tr);
    }

    @Test
    public void d_addNewBook() throws
            Exception
    {
        String apiUrl="/books/book";

        Section s1 = new Section("Fiction");
        s1.setSectionid(1);

        String troys = "troys";
        Book b1 = new Book(troys, "97807382021345", 2001, s1);

        b1.setBookid(100);

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(b1);

        Mockito.when(bookService.save(any(Book.class))).thenReturn(b1);
        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userString);
        mockMvc.perform(rb).andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateFullBook()
    {
    }

    @Test
    public void e_deleteBookById() throws
            Exception
    {
        String apiUrl = "/books/book/{id}";

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl, "135")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(rb)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}