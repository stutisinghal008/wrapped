package com.example.cs_2340_assignment2.data;

import com.example.cs_2340_assignment2.data.spotify.Wrapped;

import java.io.Serializable;

/**
 * Represents a container for message data in the app.
 */
public class MessageData implements Serializable {
    private Wrapped data;
    private String text;

    /**
     * Default constructor.
     */
    public MessageData() {
        this(new Wrapped(), "");
    }

    /**
     * Constructor with a wrapped object and text.
     *
     * @param data wrapped object
     * @param text text
     */
    public MessageData(Wrapped data, String text) {
        this.data = data;
        this.text = text;
    }

    /**
     * Get the wrapped object.
     *
     * @return wrapped object
     */
    public Wrapped getData() {
        return data;
    }

    /**
     * Get the text.
     *
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * Set the wrapped object.
     *
     * @param data wrapped object
     */
    public void setData(Wrapped data) {
        this.data = data;
    }

    /**
     * Set the text.
     *
     * @param text text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Check if this object is equal to another object.
     *
     * @param o object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof MessageData) {
            MessageData other = (MessageData) o;
            return data.equals(other.data) && text.equals(other.text);
        }
        return false;
    }
}
