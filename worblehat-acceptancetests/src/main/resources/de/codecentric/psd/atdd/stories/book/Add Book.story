Meta:
@themes Book

Narrative:
In order to add new books to the library
As a librarian
I want to add books through the website

Scenario:

Given an empty library
When a librarian adds a book with title <title>, author <author>, edition <edition>, year <year>, isbn <isbn> and desc <desc>
Then The booklist contains a book with values title <title>, author <author>, year <year>, edition <edition>, isbn <isbn>, desc <desc>

Examples:
 
| isbn       | author           | title     | edition   | year  | desc |
| 0552131075 | Terry Pratchett  | Sourcery  | 1         | 1989  | bla  |






