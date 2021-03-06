package persistence;

import model.Article;
import model.Blog;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
// Tests for the JsonReader class
// This class is based on the JSONSerializationDemo project
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReaderTest extends JsonTest {

    @Test
    public void testReaderNonexistentFile() {
        JsonReader reader = new JsonReader("./data/doesnotexist.json");
        try {
            Blog blog = reader.read();
            fail("Expected IOException");
        } catch (IOException e) {
            // Should catch IOException
        }
    }

    @Test
    public void testReaderEmptyBlog() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyBlog.json");
        try {
            Blog blog = reader.read();
            assertEquals("My Blog", blog.getTitle());
            ArrayList<Article> articles = blog.getArticles();
            assertEquals(articles.size(), 0);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    public void testReaderNonemptyBlog() {
        JsonReader reader = new JsonReader("./data/testReaderBlog.json");
        try {
            Blog blog = reader.read();
            assertEquals("Captain's Log", blog.getTitle());
            ArrayList<Article> articles = blog.getArticles();
            assertEquals(articles.size(), 2);
            checkArticle("My first blog post", "Adam Mitha",
                    "Some content goes here", articles.get(0));
            checkArticle("My second blog post", "Han Solo",
                    "Some other content goes here", articles.get(1));
            assertEquals(0, articles.get(0).getTags().size());
            checkTags(new ArrayList<String>(Arrays.asList("Tag 1", "Tag 2")), articles.get(1).getTags());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
