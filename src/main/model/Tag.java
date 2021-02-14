package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

// Represents a tag on an article having a name and a color
public class Tag {

    private String name;

    // EFFECTS: creates a tag with the given name
    public Tag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
