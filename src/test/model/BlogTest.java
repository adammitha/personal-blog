package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class BlogTest {
    Blog blog;

    @BeforeEach
    public void setUp() {
        blog = new Blog("My Personal Blog");
    }

    @Test
    public void testChangeTitle() {
        blog.setTitle("My Secret Diary");
        assertEquals(blog.getTitle(), "My Secret Diary");
    }

    @Test
    public void testAddArticle() {
        Article article = new Article(
                1,
                "How to destroy the death star",
                "Luke Skywalker",
                "Fire a photon torpedo into the exhaust vent",
                LocalDate.now());
        assertTrue(blog.addArticle(article));
        assertEquals(blog.getArticles().size(), 1);
        blog.setArticles(new ArrayList<Article>());
        assertEquals(blog.getArticles().size(), 0);
    }

    @Test
    public void testFindArticleById() {
        for (int i = 1; i <= 10; i++) {
            Article article = new Article(
                    i,
                    String.format("Article #%s", i),
                    "Han Solo",
                    String.format("Content #%s", i),
                    LocalDate.now());
            blog.addArticle(article);
        }
        try {
            assertEquals(blog.findArticleById(1).getId(), 1);
            assertEquals(blog.findArticleById(2).getTitle(), "Article #2");
            assertEquals(blog.findArticleById(8).getContent(), "Content #8");
        } catch (NoSuchElementException e) {
            fail("Should not throw an exception");
        }

    }

    @Test
    public void testFindArticleByIdDoesNotExist() {
        for (int i = 1; i <= 5; i++) {
            Article article = new Article(
                    i,
                    String.format("Article #%s", i),
                    "Han Solo",
                    String.format("Content #%s", i),
                    LocalDate.now());
            blog.addArticle(article);
        }
        try {
            Article article = blog.findArticleById(6);
            fail("Should throw a NoSuchElementException");
        } catch (NoSuchElementException e) {
            assertEquals(e.getMessage(), "Could not find article with id 6");
        }
    }
}