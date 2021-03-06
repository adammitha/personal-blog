package persistence;

import model.Article;
import model.Blog;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.stream.Stream;

import model.Tag;
import org.json.*;

// Reads in a blog stored as a JSON text file
// This class is based on the JSONSerializationDemo project
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReader {
    private String source;

    // EFFECTS: constructs a JsonReader to read a blog in from file
    public JsonReader(String source) {
        this.source = source;
    }

    public Blog read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseBlog(jsonObject);
    }

    // EFFECTS: reads source file into a string a returns the result
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses blog from JSON object and returns it
    private Blog parseBlog(JSONObject jsonObject) {
        String title = jsonObject.getString("title");
        Blog blog = new Blog(title);
        addArticles(blog, jsonObject);
        return blog;
    }

    // MODIFIES: blog
    // EFFECTS: parses blog articles from JSON object and adds them to the blog
    private void addArticles(Blog blog, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("articles");
        for (Object json : jsonArray) {
            JSONObject nextArticle = (JSONObject) json;
            addArticle(blog, nextArticle);
        }
    }

    // MODIFIES: blog
    // EFFECTS: parses article from JSON object and adds it to blog
    private void addArticle(Blog blog, JSONObject jsonObject) {
        int id = jsonObject.getInt("id");
        String title = jsonObject.getString("title");
        String author = jsonObject.getString("author");
        String content = jsonObject.getString("content");
        String created = jsonObject.getString("dateCreated");
        LocalDate dateCreated = LocalDate.parse(created);
        String edited = jsonObject.getString("dateEdited");
        LocalDate dateEdited = LocalDate.parse(edited);
        Article article = new Article(id, title, author, content, dateCreated, dateEdited);

        JSONArray jsonArray = jsonObject.getJSONArray("tags");
        for (Object json : jsonArray) {
            JSONObject nextTag = (JSONObject) json;
            String name = nextTag.getString("name");
            article.addTag(new Tag(name));
        }

        blog.addArticle(article);

    }

}
