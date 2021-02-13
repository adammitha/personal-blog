package model;

import java.util.ArrayList;
import java.util.NoSuchElementException;

// Represents a blog with a title and a list of articles that make up the blog.
public class Blog {
    private String title;
    private ArrayList<Article> articles;

    // REQUIRES: title is a non-empty string
    // EFFECTS: creates a new blog with the given title
    public Blog(String title) {
        this.title = title;
        this.articles = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds an article to the list of articles in the blog
    public boolean addArticle(Article article) {
        return this.articles.add(article);
    }

    // EFFECTS: Returns the article with the provided id,
    //          or throws an exception if no such article exists.
    public Article findArticleById(int id) throws NoSuchElementException {
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

    public ArrayList<Article> getArticles() {
        return articles;
    }

}
