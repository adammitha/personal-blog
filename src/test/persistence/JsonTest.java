package persistence;

import model.Article;
import model.Tag;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// This class is based on the JSONSerializationDemo project
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonTest {
    public void checkArticle(String expectedTitle, String expectedAuthor, String expectedContent, Article article) {
        assertEquals(expectedTitle, article.getTitle());
        assertEquals(expectedAuthor, article.getAuthor());
        assertEquals(expectedContent, article.getContent());
    }

    public void checkTags(ArrayList<String> tagNames, ArrayList<Tag> tags) {
        assertEquals(tagNames.size(), tags.size());
        for (Tag tag : tags) {
            if (!tagNames.contains(tag.getName())) {
                fail(String.format("Tag %s not expected", tag.getName()));
            }
        }
    }
}
