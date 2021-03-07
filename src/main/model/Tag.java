package model;

import org.json.JSONObject;
import persistence.Writable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

// Represents a tag on an article having a name and a color
public class Tag implements Writable {

    private String name;

    // REQUIRES: name is a nonempty string
    // EFFECTS: creates a tag with the given name
    public Tag(String name) {
        this.name = name;
    }

    // EFFECTS: returns a JSON representation of this
    @Override
    public JSONObject toJson() {
        JSONObject tag = new JSONObject();
        tag.put("name", name);
        return tag;
    }

    public String getName() {
        return name;
    }

}
