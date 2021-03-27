package ui;

import model.Article;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

// ArticleListModel represents a list of articles in the BlogApp GUI
public class ArticleListModel extends AbstractListModel<Article> {
    private ArrayList<Article> articles;

    // EFFECTS: constructs an ArticleListModel instance
    public ArticleListModel(ArrayList<Article> input) {
        articles = input;
    }

    // MODIFIES: this
    // EFFECTS: Adds article to list of articles in GUI
    public void add(Article a) {
        articles.add(a);
    }

    // MODIFIES: this
    // EFFECTS: Removes article from list of articles in GUI
    public void delete(Article a) {
        articles.remove(a);
    }

    // MODIFIES: this
    // EFFECTS: Updates provided article
    public void update(Article a) {
        int index = articles.indexOf(a);
        articles.set(index, a);
    }

    // EFFECTS: Returns index of provided note
    public int indexOf(Article a) {
        return articles.indexOf(a);
    }

    // EFFECTS: Returns size of underlying articles list
    @Override
    public int getSize() {
        return articles.size();
    }

    // EFFECTS: Returns article at provided index
    @Override
    public Article getElementAt(int index) {
        if (articles.isEmpty()) {
            return null;
        }

        return articles.get(index);
    }

    // EFFECTS: Returns underlying list of articles
    public ArrayList<Article> getArticles() {
        return this.articles;
    }

    // EFFECTS: Returns true if there are no articles in the list
    public boolean isEmpty() {
        return articles.isEmpty();
    }
}
