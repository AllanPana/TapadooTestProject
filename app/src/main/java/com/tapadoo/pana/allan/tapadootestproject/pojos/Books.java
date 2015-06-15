package com.tapadoo.pana.allan.tapadootestproject.pojos;

/**
 * Created by allan on 15/06/15.
 */
public class Books {


    private int id;
    private String title;
    private String isbn;
    private String description;
    private int price;
    private String currencyCode;
    private String author;


    public Books() {
    }

    public Books(String title, String author, int price) {
        this.title = title;
        this.price = price;
        this.author = author;
    }


    public Books(int id,
                 String title,
                 String isbn,
                 String description,
                 int price,
                 String currencyCode,
                 String author) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.description = description;
        this.price = price;
        this.currencyCode = currencyCode;
        this.author = author;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Books{" +
                "title='" + title + '\'' +
                ", price=" + price +
                ", author='" + author + '\'' +
                '}';
    }
}
