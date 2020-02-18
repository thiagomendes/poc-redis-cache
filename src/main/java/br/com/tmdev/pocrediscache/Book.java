package br.com.tmdev.pocrediscache;

public class Book {

    private int id;

    private String author;

    private String isbn;

    private Double price;

    public Book() {
    }

    public Book(int id, String author, String isbn, Double price) {
        this.id = id;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
