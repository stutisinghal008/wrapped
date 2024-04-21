package com.example.cs_2340_assignment2.state;

import android.util.Log;
import java.util.stream.Collectors;

import com.example.cs_2340_assignment2.data.Message;
import com.example.cs_2340_assignment2.data.User;
import com.example.cs_2340_assignment2.data.spotify.Wrapped;
import com.example.cs_2340_assignment2.ui.PostAdapter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * State class for managing the state of the app.
 * Frontend data bridge entry point for the app.
 */
public class State {
    private static State instance = getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Map<String, User> users;
    private Map<String, Message> messages;
    private Map<String, PostAdapter.Post> posts;
    private Wrapped wrapped;
    private String currentUser;

    /**
     * Default constructor.
     */
    private State() {
        db = FirebaseFirestore.getInstance();
        users = new HashMap<>();
        messages = new HashMap<>();
        posts = new HashMap<>();
        wrapped = new Wrapped();
    }

    /**
     * Get the instance of the state.
     *
     * @return the instance of the state
     */
    public static State getInstance() {
        if (instance == null) {
            return new State();
        }
        return instance;
    }

    /**
     * Get the database instance.
     *
     * @return the database instance
     */
    protected static FirebaseFirestore getDB() {
        return getInstance().db;
    }

    /**
     * Get the users in the app.
     *
     * @return users in the app
     */
    public List<User> getUsers() {
        List<User> userList = new ArrayList<>();
        for (User user : users.values()) {
            userList.add(user);
        }
        return userList;
    }

    /**
     * Get the messages in the app.
     *
     * @return messages in the app
     */
    public Map<String, Message> getMessages() {
        return messages;
    }

    /**
     * Get the messages in the app.
     *
     * @return messages in the app
     */
    public List<Message> getMessagesAsList() {
        List<Message> messageList = new ArrayList<>();
        for (Message message : messages.values()) {
            messageList.add(message);
        }
        return messageList;
    }

    /**
     * Get the posts in the app.
     * @return posts in the app
     */
    public Map<String, PostAdapter.Post> getPosts() {
        return posts;
    }

    /**
     * Get the posts in the app.
     * @return posts in the app
     */

    public List<PostAdapter.Post> getPostsAsList() {
        List<PostAdapter.Post> postList = new ArrayList<>();
        postList.addAll(posts.values());
        postList.sort((p1, p2) -> p2.getTime().compareTo(p1.getTime()));
        return postList;
    }

    public Wrapped getWrapped() {
        return wrapped;
    }
    /**
     * Adds a post to the system.
     * @param post the post to add
     */
    public void addPost(PostAdapter.Post post) {
        posts.put(post.getId(), post);
    }

    /**
     * Adds a user to the system.
     *
     * @param user the user to add
     */
    public void addUser(User user) {
        if (user == null) {
            throw new NullPointerException("User cannot be null!");
        }
        users.put(user.getId(), user);
    }

    /**
     * Adds a message to the system.
     *
     * @param message the message to add
     */
    public void addMessage(Message message) {
        if (message == null) {
            throw new NullPointerException("Message cannot be null!");
        }
        if (message.getContent().getData().getArtists().size() != 0) {
            messages.put(message.getId(), message);
        }
    }

    /**
     * Removes a user from the system.
     *
     * @param user the user to remove
     */
    public void removeUser(User user) {
        if (user == null) {
            throw new NullPointerException("User cannot be null!");
        }
        Log.d("RemoveUser", "Attempting to remove user: " + user.getUsername());
        users.remove(user.getId());
        for (String key : users.keySet()) {
            Log.d("MapKey", "User key in map: '" + key + "'");
        }
        for (String m : messages.keySet()) {
            if (messages.get(m).getAuthors().contains(user.getId())) {
                var authors = messages.get(m).getAuthors();
                authors.remove(user.getId());
                messages.get(m).setAuthors(authors);

                if (authors.size() == 0) {
                    messages.remove(m);
                }
            }
        }
    }

    /**
     * Removes a message from the system.
     *
     * @param message the message to remove
     */
    public void deleteMessage(Message message) {
        if (message == null) {
            throw new NullPointerException("Message cannot be null!");
        }
        for (String m : messages.keySet()) {
            if (messages.get(m).equals(message)) {
                messages.get(m).delete();
                messages.remove(m);
                break;
            }
        }
    }

    /**
     * Writes the state to the database.
     */
    public void writeToDB() {
        CollectionReference collection = db.collection("temp");
        DocumentReference users = collection.document("users");
        DocumentReference messages = collection.document("messages");
        DocumentReference posts = collection.document("posts");

        Map<String, User> writeUsers = this.users;
        Map<String, Message> writeMessages = this.messages;
        Map<String, PostAdapter.Post> writePosts = this.posts;

        users.set(writeUsers);
        messages.set(writeMessages);
        posts.set(writePosts);
    }

    public void setWrapped(Wrapped w) {
        wrapped=w;
    }

    private Map<String, Object> serializeMessages(Map<String, Message> messages) {
        // Implement message serialization logic here
        return new HashMap<>();
    }

    private Map<String, Object> serializePosts(Map<String, PostAdapter.Post> posts) {
        // Implement post serialization logic here
        return new HashMap<>();
    }

    /**
     * Reads the state from the database for persistence.
     */
    public void readFromDB() {
        CollectionReference collection = db.collection("temp");
        DocumentReference users = collection.document("users");

        users.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                return;
            }
            if (snapshot != null && snapshot.exists()) {
                Map<String, Object> u = snapshot.getData();
                assert u != null;
                for (String key : u.keySet()) {
                    User user = Factory.createUser(u.get(key));
                    addUser(user);
                }
            }
        });

        DocumentReference messages = collection.document("messages");
        messages.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                return;
            }
            if (snapshot != null && snapshot.exists()) {
                Map<String, Object> m = snapshot.getData();
                assert m != null;
                for (String key : m.keySet()) {
                    Message message = Factory.createMessage(m.get(key));
                    addMessage(message);
                }
            }
        });

        DocumentReference posts = collection.document("posts");
        posts.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                return;
            }
            if (snapshot != null && snapshot.exists()) {
                Map<String, Object> p = snapshot.getData();
                assert p != null;
                for (String key : p.keySet()) {
                    PostAdapter.Post post = Factory.createPost(p.get(key));
                    addPost(post);
                }
            }
        });
    }

    /**
     * Get the current user.
     *
     * @return the current user
     */
    public String getCurrentUser() {
        return currentUser;
    }

    /**
     * Set the current user.
     *
     * @param currentUser the current user
     */
    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
}

