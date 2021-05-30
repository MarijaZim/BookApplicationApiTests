package models;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import enums.BookType;
import enums.TypeFormat;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Book {
    private Long isbn;
    private String title;
    private Integer year;
    private BookType bookType;
    private Author author;
    private Integer numberOfPages;
    private Double weight;
    private TypeFormat format;
    private Double size;

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setBookType(BookType bookType) {
        this.bookType = bookType;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFormat(TypeFormat format) {
        this.format = format;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Author getAuthor() {
        return author;
    }

    public TypeFormat getFormat() {
        return format;
    }

    public BookType getBookType() {
        return bookType;
    }

    public Double getSize() {
        return size;
    }

    public Double getWeight() {
        return weight;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public Long getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }
}
