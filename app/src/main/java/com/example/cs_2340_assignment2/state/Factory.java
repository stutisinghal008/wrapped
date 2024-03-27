package com.example.cs_2340_assignment2.state;

import com.example.cs_2340_assignment2.data.Message;
import com.example.cs_2340_assignment2.data.MessageData;
import com.example.cs_2340_assignment2.data.User;
import com.example.cs_2340_assignment2.data.UserScope;
import com.example.cs_2340_assignment2.data.spotify.Wrapped;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Factory class for creating objects from POJO and API values.
 */
public class Factory {
    protected static FirebaseFirestore db = State.getDB();

    /**
     * Create users from POJO values.
     *
     * @param o POJO object
     */
    public static User createUser(Object o) {
        if (!(o instanceof Map)) {
            throw new IllegalArgumentException("Object must be a Map! Invalid POJO!");
        } else {
            Map<String, Object> m = (Map<String, Object>) o;
            Long id = (Long) m.get("id");
            String username = (String) m.get("username");
            String password = (String) m.get("password");

            List<Long> messages = (List<Long>) m.get("messages");
            List<Long> likes = (List<Long>) m.get("likes");
            List<Long> views = (List<Long>) m.get("views");

            List<Long> followers = (List<Long>) m.get("followers");
            List<Long> following = (List<Long>) m.get("following");

            String s = (String) m.get("scope");
            UserScope scope = UserScope.valueOf(s);

            Wrapped w = createWrapped(m.get("spotifyData"));

            return new User(
                    id,
                    username,
                    password,
                    messages,
                    likes,
                    views,
                    followers,
                    following,
                    scope,
                    w);
        }
    }

    /**
     * Create messages from POJO values.
     *
     * @param o POJO object
     */
    public static Message createMessage(Object o) {
        if (!(o instanceof Map)) {
            throw new IllegalArgumentException("Object must be a Map! Invalid POJO!");
        } else {
            Map<String, Object> m = (Map<String, Object>) o;
            int id = (int) m.get("id");
            int root = (int) m.get("rootId");
            int parent = (int) m.get("parentId");
            Map<String, Object> content = (Map<String, Object>) m.get("content");

            List<Long> authors = (List<Long>) m.get("authors");
            List<Long> likes = (List<Long>) m.get("likes");
            List<Long> views = (List<Long>) m.get("views");
            List<Long> replies = (List<Long>) m.get("replies");

            Date date = (Date) m.get("date");
            boolean isDeleted = (boolean) m.get("isDeleted");

            MessageData data = new MessageData(
                    createWrapped(content.get("data")),
                    content.get("text").toString()
            );

            return new Message(
                    root,
                    parent,
                    id,
                    data,
                    authors,
                    likes,
                    views,
                    replies,
                    date,
                    isDeleted
            );
        }
    }

    /**
     * Create Wrapped objects from POJO values.
     *
     * @param o POJO object
     */
    public static Wrapped createWrapped(Object o) {
        if (!(o instanceof Map)) {
            throw new IllegalArgumentException("Object must be a Map! Invalid POJO!");
        } else {
            Map<String, Object> m = (Map<String, Object>) o;
            Wrapped wr = new Wrapped();
            var w = new ArrayList<Wrapped.Song>();
            var ls = (List<Object>) m.get("wrapped");
            for (Object ot : ls) {
                Map<String, Object> mt = (Map<String, Object>) ot;
                Map<String, Long> freq = (Map<String, Long>) mt.get("monthlyFrequencyOfListens");
                Wrapped.Song s = new Wrapped.Song(
                        (String) mt.get("title"),
                        (List<String>) mt.get("artists"),
                        (Long) mt.get("duration"),
                        (String) mt.get("genre"),
                        (GeoPoint) mt.get("mostPopularLocation"),
                        (Long) mt.get("numerOfSecondsListened"),
                        freq
                );
                w.add(s);
            }
            wr.setWrapped(null);
            return wr;
        }
    }

    /**
     * Create SpotifyWrapper objects from API values.
     */
    public static class SpotifyFactory {

    }
}
