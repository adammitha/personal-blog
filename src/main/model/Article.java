package model;

import java.util.Calendar;
import java.util.Date;
import java.time.LocalDate;

public class Article {
    private static int nextId = 1;
    private final int id;
    private String title;
    private String author;
    private String content;
    private Date dateCreated;
    private Date dateEdited;

    public Article(String title, String author, String content) {
        this.id = nextId++;
        this.title = title;
        this.author = author;
        this.content = content;
        this.dateCreated = Calendar.getInstance().getTime();
        this.dateEdited = this.dateCreated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateEdited() {
        return dateEdited;
    }

    public void setDateEdited(Date dateEdited) {
        this.dateEdited = dateEdited;
    }

    public int getId() {
        return id;
    }
}
