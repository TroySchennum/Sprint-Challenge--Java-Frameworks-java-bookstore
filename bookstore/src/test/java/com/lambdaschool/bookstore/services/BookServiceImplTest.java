package com.lambdaschool.bookstore.services;

import com.lambdaschool.bookstore.BookstoreApplication;
import com.lambdaschool.bookstore.exceptions.ResourceNotFoundException;
import com.lambdaschool.bookstore.models.Book;
import com.lambdaschool.bookstore.models.Section;
import com.lambdaschool.bookstore.models.Wrote;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookstoreApplication.class)
//**********
// Note security is handled at the controller, hence we do not need to worry about security here!
//**********
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BookServiceImplTest
{

    @Autowired
    private SectionService sectionService;

    @Autowired
    private BookService bookService;

    @Autowired UserService userService;

    @Autowired AuthorService authorService;

    @Before
    public void setUp() throws
            Exception
    {
        MockitoAnnotations.initMocks(this);

        List<Book> myList = bookService.findAll();
        for (Book u: myList)
        {
            System.out.println(u.getBookid() + " " + u.getTitle());
        }

    }

    @After
    public void tearDown() throws
            Exception
    {
    }

    @Test
    public void a_findAll()
    {
        assertEquals(5, bookService.findAll().size());
    }

    @Test
    public void b_findBookById()
    {
        assertEquals("Flatterland", bookService.findBookById(26).getTitle());
    }



    @Test(expected = ResourceNotFoundException.class)
    public void c_notFindBookById()
    {
        assertEquals("cinnamon", bookService.findBookById(7).getTitle());
    }

    @Test
    public void d_delete()
    {
        bookService.delete(27);
        assertEquals(4, bookService.findAll().size());
    }

    @Test
    public void e_save()
    {
        Section s1 = new Section("Fiction");
        Section s2 = new Section("Technology");
        Section s3 = new Section("Travel");
        Section s4 = new Section("Business");
        Section s5 = new Section("Religion");

        s1 = sectionService.save(s1);
        s2 = sectionService.save(s2);
        s3 = sectionService.save(s3);
        s4 = sectionService.save(s4);
        s5 = sectionService.save(s5);

        String troy = "troy";
        Book b1 = new Book(troy, "9780738205687", 2001, s2);

        Book addBook = bookService.save(b1);
        assertNotNull(addBook);
        assertEquals(troy, addBook.getTitle());
    }

    @Test
    public void f_update()
    {
    }

    @Test
    public void deleteAll()
    {
    }
}