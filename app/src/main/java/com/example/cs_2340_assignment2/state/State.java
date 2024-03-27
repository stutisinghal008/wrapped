package com.example.cs_2340_assignment2.state;

import com.example.cs_2340_assignment2.data.Message;
import com.example.cs_2340_assignment2.data.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * State class for managing the state of the app.
 * Frontend data bridge entry point for the app.
 */
public class State {
    private static State instance;
    private FirebaseFirestore db;
    private Map<Long, User> users;
    private Map<Long, Message> messages;

    /**
     * Default constructor.
     */
    private State() {
        db = FirebaseFirestore.getInstance();
        users = new HashMap<>();
        messages = new HashMap<>();
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
        if (instance.db == null) {
            instance.db = FirebaseFirestore.getInstance();
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
    public List<Message> getMessages() {
        List<Message> messageList = new ArrayList<>();
        for (Message message : messages.values()) {
            messageList.add(message);
        }
        return messageList;
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
        messages.put(message.getId(), message);
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
        users.remove(user);
        for (Long m : messages.keySet()) {
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
        for (Long m : messages.keySet()) {
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

        Task<Void> updateUsers = users.set(getUsers());
        Task<Void> updateMessages = messages.set(getMessages());
    }

    /**
     * Reads the state from the database for persistence.
     */
    public void readFromDB() {
        CollectionReference collection = db.collection("temp");
        DocumentReference users = collection.document("users");
        DocumentReference messages = collection.document("messages");

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

    }
}
