package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ArticleTest {
    Article article;

    @BeforeEach
    public void setUp() {
        // Create new article with dateCreated yesterday
        article = new Article(
                1,
                "How to destroy the Death Star",
                "Luke Skywalker",
                "Fire a photon torpedo into the exhaust vent.",
                LocalDate.now().minusDays(1));
    }

    @Test
    public void testGetNextID() {
        int first_id = Article.getNextId();
        int second_id = Article.getNextId();
        int third_id = Article.getNextId();
        int fourth_id = Article.getNextId();
        assertEquals(second_id, first_id + 1);
        assertEquals(third_id, first_id + 2);
        assertEquals(fourth_id, first_id + 3);
    }

    @Test
    public void testEditArticle() {
        article.edit(
                "How to destroy the Death Star (version 2)",
                "Han Solo",
                "Enlist the Ewoks!"
        );
        assertEquals(article.getTitle(), "How to destroy the Death Star (version 2)");
        assertEquals(article.getAuthor(), "Han Solo");
        assertEquals(article.getContent(), "Enlist the Ewoks!");
        assertEquals(article.getDateEdited().getDayOfMonth(), LocalDate.now().getDayOfMonth());
        assertEquals(article.getDateEdited().getMonthValue(), LocalDate.now().getMonthValue());
        assertEquals(article.getDateEdited().getYear(), LocalDate.now().getYear());
    }

    @Test
    public void testAddTags() {
        Tag tutorials = new Tag("Tutorials", Tag.BLUE);
        Tag stories = new Tag("Stories", Tag.YELLOW);

        assertTrue(article.addTag(tutorials));
        assertEquals(article.getTags().size(), 1);

        assertTrue(article.addTag(stories));
        assertEquals(article.getTags().size(), 2);
    }

    @Test
    public void testAddDuplicateNameTags() {
        Tag tutorials1 = new Tag("Tutorials", Tag.BLUE);
        Tag tutorials2 = new Tag("Tutorials", Tag.RED);

        assertTrue(article.addTag(tutorials1));
        assertEquals(article.getTags().size(), 1);

        assertFalse(article.addTag(tutorials2));
        assertEquals(article.getTags().size(), 1);
    }

    @Test
    public void testAddDuplicateColorTags() {
        Tag tutorials = new Tag("Tutorials", Tag.BLUE);
        Tag stories = new Tag("Stories", Tag.BLUE);

        assertTrue(article.addTag(tutorials));
        assertEquals(article.getTags().size(), 1);

        assertFalse(article.addTag(stories));
        assertEquals(article.getTags().size(), 1);
    }

    @Test
    public void testDeleteTag() {
        Tag tutorials = new Tag("Tutorials", Tag.BLUE);
        Tag stories = new Tag("Stories", Tag.YELLOW);

        assertTrue(article.addTag(tutorials));
        assertTrue(article.addTag(stories));
        assertTrue(article.deleteTag("Tutorials"));
        assertEquals(article.getTags().size(), 1);
    }

    @Test
    public void testDeleteTagDoesNotExist() {
        Tag tutorials = new Tag("Tutorials", Tag.BLUE);
        Tag stories = new Tag("Stories", Tag.YELLOW);

        assertTrue(article.addTag(tutorials));
        assertTrue(article.addTag(stories));
        assertFalse(article.deleteTag("Travel"));
        assertEquals(article.getTags().size(), 2);
    }

    @Test
    public void testToString() {
        assertTrue(article.toString().contains(article.getTitle()));
        assertTrue(article.toString().contains(article.getAuthor()));
        assertTrue(article.toString().contains(article.getDateCreated().toString()));
        assertTrue(article.toString().contains(article.getDateEdited().toString()));
    }

}
