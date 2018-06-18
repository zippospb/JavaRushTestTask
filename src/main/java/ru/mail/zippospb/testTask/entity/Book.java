package ru.mail.zippospb.testTask.entity;

import javax.persistence.*;

@javax.persistence.Entity
//@Table(name = "book")
public class Book {

//    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

//    @Column(name = "title")
    private String title;

//    @Column(name = "description")
    private String description;

//    @Column(name = "author")
    private String author;

//    @Column(name = "isbn")
    private String isbn;

//    @Column(name = "printYear")
    private int printYear;

//    @Column(name = "readAlready")
    private boolean readAlready;

    private byte[] imageData;

    private String imageStr;

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {

        this.imageData = imageData;
    }

    public void setImageStr(String imageStr) {
        this.imageStr = imageStr;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPrintYear(int printYear) {
        this.printYear = printYear;
    }

    public void setReadAlready(boolean readAlready) {
        this.readAlready = readAlready;
    }

    public String getImageStr() {
        return imageStr;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getPrintYear() {
        return printYear;
    }

    public boolean isReadAlready() {
        return readAlready;
    }

    @Override
    public String toString() {
        return "Book{" + title + '}';
    }
}
