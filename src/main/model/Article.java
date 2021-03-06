package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.time.LocalDate;
import java.util.ArrayList;

// Article represents an article in a blog. It has a title, author, and content which are modifiable by the user.
// The date created, date edited and id are managed automatically by the constructor and edit methods.
public class Article implements Writable {
    private static int nextId = 1;
    private int id;
    private String title;
    private String author;
    private String content;
    private final LocalDate dateCreated;
    private LocalDate dateEdited;
    private ArrayList<Tag> tags;

    // REQUIRES: title, author, and content are nonempty strings
    public Article(int id, String title, String author, String content, LocalDate dateCreated) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
        this.dateCreated = dateCreated;
        this.dateEdited = dateCreated;
        this.tags = new ArrayList<>();
    }

    // REQUIRES: title, author, and content are nonempty strings
    // EFFECTS: article constructor which accepts a dateEdited. To be used with the JsonReader class
    public Article(int id, String title, String author, String content, LocalDate dateCreated, LocalDate dateEdited) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
        this.dateCreated = dateCreated;
        this.dateEdited = dateEdited;
        this.tags = new ArrayList<>();
    }

    // EFFECTS: returns the next id for a new article
    public static int getNextId() {
        return nextId++;
    }

    // MODIFIES: this
    // EFFECTS: sets the next id for a new article
    public static void setNextId(int id) {
        nextId = id;
    }

    // MODIFIES: this
    // EFFECTS: checks if the new tag collides with an existing tag (same name)
    //          and adds it to tags if there are no collisions
    public boolean addTag(Tag newTag) {
        // Check if a tag with the same name already exists
        for (Tag tag : tags) {
            if (tag.getName().equals(newTag.getName())) {
                return false;
            }
        }
        this.tags.add(newTag);
        return true;
    }

    // REQUIRES: name is a nonempty string
    // MODIFIES: this
    // EFFECTS: removes the tag with the given name from the Article's list of tags if it exists
    public boolean deleteTag(String name) {
        // Try to delete a tag
        for (Tag tag : this.tags) {
            if (tag.getName().equals(name)) {
                this.tags.remove(tag);
                return true;
            }
        }
        return false;
    }

    // REQUIRES: newTitle, newAuthor and newString are nonempty string
    // MODIFIES: this
    // EFFECTS: updates the title, author and content of the blog post with newTitle, newAuthor and newContent
    //          updates the dateEdited with today's date
    public void edit(String newTitle, String newAuthor, String newContent) {
        this.title = newTitle;
        this.author = newAuthor;
        this.content = newContent;
        this.dateEdited = LocalDate.now();
    }

    // EFFECTS: returns a string representation of the Article
    @Override
    public String toString() {
        return String.format(
                "%s by %s",
                this.title,
                this.author);
    }

    // EFFECTS: Renders article to json object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("title", title);
        json.put("author", author);
        json.put("content", content);
        json.put("dateCreated", dateCreated.toString());
        json.put("dateEdited", dateEdited.toString());
        json.put("tags", tagsToJson());
        return json;
    }

    // EFFECTS: Renders tags to json array
    private JSONArray tagsToJson() {
        JSONArray tagArray = new JSONArray();
        for (Tag tag : this.tags) {
            tagArray.put(tag.toJson());
        }
        return tagArray;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public LocalDate getDateEdited() {
        return dateEdited;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

}
