package authors;

import io.restassured.RestAssured;
import models.Author;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.List;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AuthorTests {

    @BeforeClass
    public void beforeClass() {
        RestAssured.port = 9090;
        RestAssured.basePath = "api";
    }

    @Test
    public void shouldBeAbleToGetAllAuthors() {
        List<Author> authors = Arrays.asList(RestAssured
                .given()
                .get("/authors")
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .extract()
                .as(Author[].class));

        assertThat(authors, is(not(empty())));
        assertThat(authors, hasItem(
                allOf(
                        hasProperty("name", equalTo("Joanne")),
                        hasProperty("surname", equalTo("Rowling"))
                )
        ));
    }
}
