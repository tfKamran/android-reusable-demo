package com.tf.reusable.demoapplication.model;

/**
 * Created by kamran on 1/9/15.
 */
public class SimpleKeyValuePair {
    private String key;
    private String value;

    public SimpleKeyValuePair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
