package de.codecentric.psd.worblehat.web.controller;

import de.codecentric.psd.worblehat.domain.BookService;
import de.codecentric.psd.worblehat.web.formdata.BookBorrowListFormData;
import de.codecentric.psd.worblehat.web.formdata.ReturnAllBooksFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 *
 */

@Controller
@RequestMapping("/listBorrowedBooks")
public class BorrowBookListController {

    private BookService bookService;

    @Autowired
    public BorrowBookListController(BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public void prepareView(ModelMap modelMap) {
        modelMap.put("listBorrowedBooksFormData", new ReturnAllBooksFormData());
        modelMap.addAttribute("borrowedBooks", null);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String lsitBorrowedBooks(
            @ModelAttribute("listBorrowedBooksFormData") @Valid BookBorrowListFormData formData,
            BindingResult result,
            ModelMap modelMap) {
        if (result.hasErrors()) {
            return "listBorrowedBooks";
        } else {
            modelMap.addAttribute(
                    "borrowedBooks",
                    bookService.getAllBooksByBorrowerSorted(formData.getEmailAddress()));
            return "listBorrowedBooks";
        }
    }
}
