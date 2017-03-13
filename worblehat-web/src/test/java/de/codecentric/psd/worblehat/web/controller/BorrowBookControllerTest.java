package de.codecentric.psd.worblehat.web.controller;

import java.util.HashMap;

import de.codecentric.psd.worblehat.domain.Book;
import de.codecentric.psd.worblehat.domain.BookAlreadyBorrowedException;
import de.codecentric.psd.worblehat.domain.BookService;
import de.codecentric.psd.worblehat.web.formdata.BookBorrowFormData;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BorrowBookControllerTest {

    private BookService bookService;

    private  BorrowBookController borrowBookController;

    private BindingResult bindingResult;

    private BookBorrowFormData bookBorrowFormData;

    private static final Book TEST_BOOK = new Book("title", "author", "edition", "isbn", 2016, "book description");

    public static final String BORROWER_EMAIL = "someone@codecentric.de";

    @Before
    public void setUp() throws Exception {
        bookService = mock(BookService.class);
        bindingResult = new MapBindingResult(new HashMap<>(), "");
        bookBorrowFormData = new BookBorrowFormData();
        borrowBookController = new BorrowBookController(bookService);
    }

    @Test
    public void shouldSetupForm() throws Exception {
        ModelMap modelMap = new ModelMap();

        borrowBookController.setupForm(modelMap);

        assertThat(modelMap.get("borrowFormData"), is(not(nullValue())));
    }

    @Test
    public void shouldNavigateToBorrowWhenResultHasErrors() throws Exception {
        bindingResult.addError(new ObjectError("", ""));

        String navigateTo = borrowBookController.processSubmit(bookBorrowFormData, bindingResult);

        assertThat(navigateTo, is("borrow"));
    }

    @Test
    public void shouldRejectBorrowingIfBookDoesNotExist() throws Exception {
        when(bookService.findBookByIsbn(TEST_BOOK.getIsbn())).thenReturn(null);

        String navigateTo = borrowBookController.processSubmit(bookBorrowFormData, bindingResult);

        assertThat(bindingResult.hasFieldErrors("isbn"), is(true));
        assertThat(navigateTo, is("borrow"));
    }

    @Test
    public void shouldRejectAlreadyBorrowedBooks() throws Exception {
        bookBorrowFormData.setEmail(BORROWER_EMAIL);
        bookBorrowFormData.setIsbn(TEST_BOOK.getIsbn());
        when(bookService.findBookByIsbn(TEST_BOOK.getIsbn())).thenReturn(TEST_BOOK);
        doThrow(BookAlreadyBorrowedException.class).when(bookService).borrowBook(TEST_BOOK, BORROWER_EMAIL);

        String navigateTo = borrowBookController.processSubmit(bookBorrowFormData, bindingResult);

        assertThat(bindingResult.hasFieldErrors("isbn"), is(true));
        assertThat(navigateTo, is("borrow"));
    }

    @Test
    public void shouldNavigateHomeOnSuccess() throws Exception {
        bookBorrowFormData.setEmail(BORROWER_EMAIL);
        bookBorrowFormData.setIsbn(TEST_BOOK.getIsbn());
        when(bookService.findBookByIsbn(TEST_BOOK.getIsbn())).thenReturn(TEST_BOOK);

        String navigateTo = borrowBookController.processSubmit(bookBorrowFormData, bindingResult);

        verify(bookService).borrowBook(TEST_BOOK, BORROWER_EMAIL);
        assertThat(navigateTo, is("home"));
    }

    @Test
    public void shouldNavigateToHomeOnErrors() throws Exception {
        String navigateTo = borrowBookController.handleErrors(new Exception(), new MockHttpServletRequest());

        assertThat(navigateTo, is("home"));
    }
}
