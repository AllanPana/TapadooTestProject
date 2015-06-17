package com.tapadoo.pana.allan.tapadootestproject.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by allan on 15/06/15.
 */
public class Books implements Parcelable{


    private int id;
    private String title;
    private String isbn;
    private String description;
    private int price;
    private String currencyCode;
    private String author;


    public Books() {
    }

    public Books(int id, String title, String author, int price) {
        this.id = id;
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

    public Books(Parcel in){

        id=in.readInt();
        title=in.readString();
        isbn=in.readString();
        description=in.readString();
        price=in.readInt();
        currencyCode=in.readString();
        author=in.readString();
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(isbn);
        dest.writeString(description);
        dest.writeInt(price);
        dest.writeString(currencyCode);
        dest.writeString(author);
    }

    /**
     *  a static field called CREATOR, which is an object implementing the Parcelable.Creator interface.
     */
    public static final Parcelable.Creator<Books> CREATOR
            = new Parcelable.Creator<Books>() {
        public Books createFromParcel(Parcel in) {
            return new Books(in);
        }

        public Books[] newArray(int size) {
            return new Books[size];
        }
    };

}
