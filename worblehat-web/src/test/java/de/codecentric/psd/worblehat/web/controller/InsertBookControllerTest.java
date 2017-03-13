package de.codecentric.psd.worblehat.web.controller;

import java.util.HashMap;

import de.codecentric.psd.worblehat.domain.Book;
import de.codecentric.psd.worblehat.domain.BookService;
import de.codecentric.psd.worblehat.web.formdata.BookDataFormData;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InsertBookControllerTest {

    private InsertBookController insertBookController;

    private BookService bookService;

    private BookDataFormData bookDataFormData;

    private BindingResult bindingResult;

    private static final Book TEST_BOOK = new Book("title", "author", "edition", "isbn", 2016, "book description");

    @Before
    public void setUp() throws Exception {
        bookService = mock(BookService.class);
        insertBookController = new InsertBookController(bookService);
        bookDataFormData = new BookDataFormData();
        bindingResult = new MapBindingResult(new HashMap<>(), "");
    }

    @Test
    public void shouldSetupForm() throws Exception {
        ModelMap modelMap = new ModelMap();

        insertBookController.setupForm(modelMap);

        assertThat(modelMap.get("bookDataFormData"), is(not(nullValue())));
    }

    @Test
    public void shouldRejectErrors() throws Exception {
        bindingResult.addError(new ObjectError("", ""));

        String navigateTo = insertBookController.processSubmit(bookDataFormData, bindingResult);

        assertThat(navigateTo, is("insertBooks"));
    }

    @Test
    public void shouldCreateNewCopyOfExistingBook() throws Exception {
        setupFormData();
        when(bookService.bookExists(TEST_BOOK.getIsbn())).thenReturn(true);

        String navigateTo = insertBookController.processSubmit(bookDataFormData, bindingResult);

        verifyBookIsCreated();
        assertThat(navigateTo, is("redirect:bookList"));
    }

    @Test
    public void shouldCreateBookAndNavigateToBookList() throws Exception {
        setupFormData();
        when(bookService.bookExists(TEST_BOOK.getIsbn())).thenReturn(false);

        String navigateTo = insertBookController.processSubmit(bookDataFormData, bindingResult);

        verifyBookIsCreated();
        assertThat(navigateTo, is("redirect:bookList"));
    }

    private void verifyBookIsCreated() {
        verify(bookService).createBook(TEST_BOOK.getTitle(), TEST_BOOK.getAuthor(),
                TEST_BOOK.getEdition(), TEST_BOOK.getIsbn(), TEST_BOOK.getYearOfPublication(), TEST_BOOK.getDesc());
    }

    private void setupFormData() {
        bookDataFormData.setTitle(TEST_BOOK.getTitle());
        bookDataFormData.setAuthor(TEST_BOOK.getAuthor());
        bookDataFormData.setEdition(TEST_BOOK.getEdition());
        bookDataFormData.setIsbn(TEST_BOOK.getIsbn());
        bookDataFormData.setYearOfPublication(String.valueOf(TEST_BOOK.getYearOfPublication()));
        bookDataFormData.setDesc(TEST_BOOK.getDesc());
    }
}
