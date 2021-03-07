package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.NoSuchElementException;

// Represents a blog with a title and a list of articles that make up the blog.
public class Blog implements Writable {
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

    // EFFECTS: returns JSON representation of blog
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("title", this.title);
        json.put("articles", articlesToJson());
        return json;
    }

    // EFFECTS: returns articles in this blog as a JSON array
    private JSONArray articlesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Article article : articles) {
            jsonArray.put(article.toJson());
        }

        return jsonArray;
    }

    // EFFECTS: Finds the ID of the next article added to the blog (i.e. highest article id + 1)
    public int findNextID() {
        int maxSoFar = 0;
        for (Article article : this.articles) {
            maxSoFar = Integer.max(maxSoFar, article.getId());
        }
        return maxSoFar + 1;
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
