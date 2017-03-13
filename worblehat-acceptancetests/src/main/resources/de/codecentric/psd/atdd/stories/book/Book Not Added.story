Narrative:
In order to keep a clean database of books
As a librarian
I want to have my entries validated

Scenario:

Given an empty library

When a librarian adds a book with title <title>, author <author>, edition <edition>, year <year>, isbn <isbn> and desc <desc>
Then the page contains error message <message>
And The library contains no books

Examples:
 
| isbn       | author           | title     |edition    | year  | message               | desc |
| 0XXXXXXXX5 | Terry Pratchett  | Sourcery  | 1         | 1989  | ISBN is invalid (numerical with 10 digits, e.g. 0123456789)     | bla |
| 0552131075 | Terry Pratchett  | Sourcery  | X         | 1989  | Edition should not be empty (numerical value, e.g. 2.0)  | bla |

