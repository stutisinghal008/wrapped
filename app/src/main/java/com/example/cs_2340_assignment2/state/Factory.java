package com.example.cs_2340_assignment2.state;

import com.example.cs_2340_assignment2.data.Message;
import com.example.cs_2340_assignment2.data.MessageData;
import com.example.cs_2340_assignment2.data.User;
import com.example.cs_2340_assignment2.data.UserScope;
import com.example.cs_2340_assignment2.data.spotify.Wrapped;
import com.example.cs_2340_assignment2.ui.PostAdapter;
import com.google.firebase.firestore.GeoPoint;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Factory class for creating objects from POJO and API values.
 */
public class Factory {

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
            String id = (String) m.get("id");
            String username = (String) m.get("username");
            String password = (String) m.get("password");

            List<String> messages = (List<String>) m.get("messages");
            List<String> likes = (List<String>) m.get("likes");
            List<String> views = (List<String>) m.get("views");

            List<String> followers = (List<String>) m.get("followers");
            List<String> following = (List<String>) m.get("following");

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
            String id = (String) m.get("id");
            String root = (String) m.get("rootId");
            String parent = (String) m.get("parentId");
            String scope = (String) m.get("scope");
            Map<String, Object> content = (Map<String, Object>) m.get("content");

            List<String> authors = (List<String>) m.get("authors");
            List<String> likes = (List<String>) m.get("likes");
            List<String> views = (List<String>) m.get("views");
            List<String> replies = (List<String>) m.get("replies");

            Date date = null;

            boolean isDeleted = "true".equals(m.get("isDeleted"));

            MessageData data = new MessageData(
                    createWrapped(content.get("data")),
                    content.get("text").toString()
            );

            var message = new Message(
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
            message.setScope(UserScope.valueOf(scope));
            return message;
        }
    }

    public static PostAdapter.Post createPost(Object o) {
        if (!(o instanceof Map)) {
            throw new IllegalArgumentException("Object must be a Map! Invalid POJO!");
        } else {
            Map<String, Object> m = (Map<String, Object>) o;
            String author = (String) m.get("author");
            String a1 = (String) m.get("artist1");
            String a2 = (String) m.get("artist2");
            String a3 = (String) m.get("artist3");
            String a4 = (String) m.get("artist4");
            String a5 = (String) m.get("artist5");
            String t1 = (String) m.get("track1");
            String t2 = (String) m.get("track2");
            String t3 = (String) m.get("track3");
            String t4 = (String) m.get("track4");
            String t5 = (String) m.get("track5");
            String genre = (String) m.get("genre");
            long time = 0;
            if (m.get("time") != null) {
                time = Long.parseLong(m.get("time").toString());
            }
            var post = new PostAdapter.Post(author, a1, a2, a3, a4, a5, t1, t2, t3, t4, t5, genre);
            post.setTime(time);
            post.setId((String) m.get("id"));
            return post;
        }
    }

    /**
     * Create Wrapped objects from POJO values.
     *
     * @param o POJO object
     */
    public static Wrapped createWrapped(Object o) {
        if (o == null) {
            return null;
        }
        if (!(o instanceof Map)) {
            throw new IllegalArgumentException("Object must be a Map! Invalid POJO!");
        } else {
            Map<String, Object> m = (Map<String, Object>) o;
            Wrapped wr = new Wrapped();
            var t = new ArrayList<Wrapped.Track>();
            var a = new ArrayList<Wrapped.Artist>();

            for (Object track : (List<Object>) m.get("tracks")) {
                Map<String, Object> tm = (Map<String, Object>) track;
                t.add(new Wrapped.Track(
                        tm.get("title").toString(),
                        (List<String>) tm.get("artists"),
                        tm.get("album_name").toString(),
                        0,
                        tm.get("uri").toString()
                ));
            }
            for (Object artist : (List<Object>) m.get("artists")) {
                Map<String, Object> am = (Map<String, Object>) artist;
                Collection<String> genres = new ArrayList<>();
                for (Object genre : (List<Object>) am.get("genres")) {
                    genres.add(genre.toString());
                }
                a.add(new Wrapped.Artist(
                        am.get("name").toString(),
                        genres,
                        am.get("uri").toString()
                ));
            }

            return wr;
        }
    }
}
