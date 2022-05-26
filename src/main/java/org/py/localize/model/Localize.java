package org.py.localize.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaorongyi on 16/3/26.
 */
public class Localize {

    private static final String COMMENT_KEY = "[COMMENT]";

    private String key;
    private String description;
    private List<String > values = new ArrayList<String>();

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getValues() {
        return values;
    }

    public void putValue(String value) {
        this.values.add(value);
    }

    public int getComment() {
        return this.key != null && this.key.equalsIgnoreCase(COMMENT_KEY) ? 1 : 0;
    }

    public String getValue() {
        if(this.getValues().size() > 0) {
            return this.getValues().get(0);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Localize{" +
                "key='" + key + '\'' +
                ", description='" + description + '\'' +
                ", values=" + values +
                '}';
    }
}