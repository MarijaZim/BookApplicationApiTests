package books;
import enums.BookType;
import enums.TypeFormat;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import models.Author;
import models.Book;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.List;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class BookTests {
    @BeforeClass
    public void beforeClass() {
        RestAssured.port = 9090;
        RestAssured.basePath = "/api";
    }

    @Test
    public void shouldBeAbleToGetAllBooks() {
        List<Book> books = Arrays.asList(RestAssured
                .given()
                .get("/books")
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .extract()
                .as(Book[].class));

        assertThat(books, is(not(empty())));
    }

    @Test
    public void shouldReturnBookWhenQueryByIsbn() {
        Integer isbn = 4;
        Book book = RestAssured.given()
                .pathParam("isbn", isbn)
                .get("/books/{isbn}")
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .extract()
                .as(Book.class);

        assertThat(book, anyOf(
                hasProperty("author", anyOf(
                        hasProperty("name", is("Joanne")),
                        hasProperty("surname", is("Rowling"))
                )),
                hasProperty("isbn", is(isbn)),
                hasProperty("title", is("The Silkworm"))
        ));
    }

    @Test
    public void shouldBeAbleToAddBook() {
        Author author = new Author();
        author.setName("Adam");
        author.setSurname("Kay");
        author.setYearOfBirth(1980);

        Book book = new Book();
        book.setTitle("This is going to hurt: Secret Diaries of a Junior Doctor");
        book.setAuthor(author);
        book.setIsbn(1509858636L);
        book.setYear(1997);
        book.setFormat(TypeFormat.pdf);
        book.setBookType(BookType.EBook);
        book.setSize(10.0);

        Integer addBookAndGetBookIsbn = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(book)
                .post("/books/add")
                .then()
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .extract()
                .path("isbn");

        Long bookIsbn = Long.valueOf(addBookAndGetBookIsbn.longValue());

        Book addedBook = RestAssured.given()
                .pathParam("isbn", bookIsbn)
                .get("/books/{isbn}")
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .extract()
                .as(Book.class);

        assertThat(addedBook.getIsbn(), is(bookIsbn));
        assertThat(addedBook.getTitle(), is("This is going to hurt: Secret Diaries of a Junior Doctor"));
        assertThat(addedBook.getSize(), is(10.0));
    }

    @Test
    public void shouldBeAbleToUpdateBook() {
        Author author = new Author();
        author.setName("Sally");
        author.setSurname("Rooney");
        author.setYearOfBirth(1991);

        Book addBook = new Book();
        addBook.setTitle("Abnormal People");
        addBook.setAuthor(author);
        addBook.setNumberOfPages(288);
        addBook.setYear(1997);
        addBook.setBookType(BookType.PrintCopy);
        addBook.setIsbn(571334652L);
        addBook.setWeight(0.1);

        Integer addBookAndGetBookIsbn = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(addBook)
                .post("/books/add")
                .then()
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .extract()
                .path("isbn");

        Long bookIsbn=Long.valueOf(addBookAndGetBookIsbn.longValue());

        Book updateBook = new Book();
        updateBook.setTitle("Normal People");
        updateBook.setAuthor(author);
        updateBook.setNumberOfPages(288);
        updateBook.setYear(1997);
        updateBook.setBookType(BookType.PrintCopy);
        updateBook.setIsbn(bookIsbn);
        updateBook.setWeight(0.12);

        RestAssured.given()
                .pathParam("isbn", updateBook.getIsbn())
                .contentType(ContentType.JSON)
                .body(updateBook)
                .put("/books/edit/{isbn}");

        Book updatedBook = RestAssured.given()
                .pathParam("isbn", bookIsbn)
                .get("/books/{isbn}")
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .extract()
                .as(Book.class);

        assertThat(updatedBook, allOf(
                hasProperty("title", is("Normal People")),
                hasProperty("numberOfPages", is(288)),
                hasProperty("isbn", is(bookIsbn)),
                hasProperty("year",is(1997)),
                hasProperty("weight",is(0.12))
        ));
    }

    @Test
    public void shouldBeAbleToDeleteBook() {
        Author author = new Author();
        author.setName("Tara");
        author.setSurname("Western");
        author.setYearOfBirth(1986);

        Book addBook = new Book();
        addBook.setTitle("Educated: The international bestselling memoir");
        addBook.setAuthor(author);
        addBook.setNumberOfPages(400);
        addBook.setYear(2010);
        addBook.setBookType(BookType.PrintCopy);
        addBook.setWeight(0.1);
        addBook.setIsbn(99511029L);

        Integer addBookAndGetBookIsbn = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(addBook)
                .post("/books/add")
                .then()
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .extract()
                .path("isbn");
        Long bookIsbn=Long.valueOf(addBookAndGetBookIsbn.longValue());

        RestAssured.given()
                .pathParam("isbn", bookIsbn)
                .delete("/books/delete/{isbn}");

        RestAssured.given()
                .pathParam("isbn", bookIsbn)
                .get("/books/{isbn}")
                .then().assertThat()
                .statusCode(SC_NOT_FOUND);
    }
}
