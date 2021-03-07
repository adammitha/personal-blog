package persistence;

import jdk.nashorn.internal.ir.debug.JSONWriter;
import model.Article;
import model.Blog;
import model.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest extends JsonTest {
    @Test
    void testWriterInvalidFile() {
        try {
            Blog blog = new Blog("My blog");
            JsonWriter writer = new JsonWriter("\"./data/my\\0illegal:fileName.json\"");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // Should catch an IOException
        }
    }

    @Test
    void testWriterEmptyBlog() {
        try {
            Blog blog = new Blog("My Blog");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyBlog.json");
            writer.open();
            writer.write(blog);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyBlog.json");
            blog = reader.read();
            assertEquals("My Blog", blog.getTitle());
            assertEquals(0, blog.getArticles().size());
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }

    @Test
    void testWriterNonemptyBlog() {
        try {
            Blog blog = new Blog("My Other Blog");
            blog.addArticle(new Article(1, "A blog post", "Adam Mitha", "Some content", LocalDate.now()));
            Article article2 = new Article(2, "Another post", "Han Solo", "I shot first", LocalDate.now());
            article2.addTag(new Tag("Tag 1"));
            article2.addTag(new Tag("Tag 2"));
            blog.addArticle(article2);
            JsonWriter writer = new JsonWriter("./data/testWriterBlog.json");
            writer.open();
            writer.write(blog);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterBlog.json");
            blog = reader.read();
            assertEquals("My Other Blog", blog.getTitle());
            ArrayList<Article> articles = blog.getArticles();
            assertEquals(articles.size(), 2);
            checkArticle("A blog post", "Adam Mitha", "Some content", articles.get(0));
            checkArticle("Another post", "Han Solo", "I shot first", articles.get(1));
            checkTags(new ArrayList<String>(Arrays.asList("Tag 1", "Tag 2")), articles.get(1).getTags());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
