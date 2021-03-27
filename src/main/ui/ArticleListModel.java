package ui;

import model.Article;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

// ArticleListModel represents a list of articles in the BlogApp GUI
public class ArticleListModel extends AbstractListModel<Article> {
    private List<Article> articles;

    // EFFECTS: constructs an ArticleListModel instance
    public ArticleListModel(List<Article> input) {
        articles = input;
    }

    // MODIFIES: this
    // EFFECTS:
    public void add(Article a) {
        articles.add(a);
    }

    public void delete(Article a) {
        articles.remove(a);
    }

    public void update(Article a) {
        int index = articles.indexOf(a);
        articles.set(index, a);
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

    // EFFECTS: Returns true if there are no articles in the list
    public boolean isEmpty() {
        return articles.isEmpty();
    }
}
