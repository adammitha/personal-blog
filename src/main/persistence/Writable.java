package persistence;

import org.json.JSONObject;

// Interface for an object that implements a toJson method
public interface Writable {
    // EFFECTS: returns this as a JSON object
    JSONObject toJson();
}
