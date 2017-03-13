package de.codecentric.psd.atdd.step.page;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import de.codecentric.psd.atdd.adapter.wrapper.Page;
import de.codecentric.psd.atdd.adapter.wrapper.PageElement;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import de.codecentric.psd.atdd.adapter.SeleniumAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.List;

@Component
public class InsertBook {

	private SeleniumAdapter seleniumAdapter;

	@Autowired
	public InsertBook(SeleniumAdapter seleniumAdapter) {
		this.seleniumAdapter = seleniumAdapter;
	}

	// *******************
	// *** G I V E N *****
	// *******************

	// *****************
	// *** W H E N *****
	// *****************

	@When("a librarian adds a book with title <title>, author <author>, edition <edition>, year <year>, isbn <isbn> and desc <desc>")
	public void whenABookWithISBNisbnIsAdded(@Named("title") String title,
											 @Named("author")String author,
											 @Named("edition") String edition,
											 @Named("year") String year,
											 @Named("isbn") String isbn,
											 @Named("desc") String desc) {
		seleniumAdapter.gotoPage(Page.INSERTBOOKS);
		fillInsertBookForm(title, author, edition, isbn, year, desc);
		seleniumAdapter.clickOnPageElement(PageElement.ADDBOOKBUTTON);
	}

	// *****************
	// *** T H E N *****
	// *****************

	@Then("the page contains error message <message>")
	public void pageContainsErrorMessage(@Named("message")String message){
		List<String> errorMsgs = seleniumAdapter.findAllStringsForElement(PageElement.ERROR);
		assertThat(errorMsgs, contains(message));
	}

	// *****************
	// *** U T I L ***** 
	// *****************


	private void fillInsertBookForm(String title, String author, String edition, String isbn,
			 String year, String desc) {
		seleniumAdapter.typeIntoField("title", title);
		seleniumAdapter.typeIntoField("edition", edition);
		seleniumAdapter.typeIntoField("isbn", isbn);
		seleniumAdapter.typeIntoField("author", author);
		seleniumAdapter.typeIntoField("yearOfPublication", year);
		seleniumAdapter.typeIntoField("desc", desc);
	}


}
