package model;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Blog {
    private static int nextId = 1; // next id
    private final int id;
    private String title;
    private ArrayList<Article> articles;

    // REQUIRES: author is a non-empty string
    // EFFECTS: creates a new blog with the given title
    public Blog(String author) {
        this.id = nextId++;
        this.title = author;
        this.articles = new ArrayList<Article>();
    }

    // MODIFIES: this
    // EFFECTS: adds an article to the list of articles in the blog
    public boolean addArticle(Article article) {
        return this.articles.add(article);
    }

    // EFFECTS: Returns the article with the provided id,
    //          or throws an exception if no such article exists.
    public Article findArticle(int id) throws NoSuchElementException {
        for (Article article : this.articles) {
            if (article.getId() == id) {
                return article;
            }
        }
        throw new NoSuchElementException(String.format("Could not find article with id %s", id));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

}
