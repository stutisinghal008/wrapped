package com.example.cs_2340_assignment2.data;


import androidx.annotation.Nullable;

import com.example.cs_2340_assignment2.data.spotify.Wrapped;
import com.example.cs_2340_assignment2.state.State;

import java.io.Serializable;
import java.util.Collection;

/**
 * Represents a user in the app.
 */
public class User implements Serializable {
    private long id;
    private String username;
    private String password;
    private final Collection<Long> messages;  // message ids
    private final Collection<Long> likes;  // message ids
    private final Collection<Long> views;  // message ids
    private final Collection<Long> followers;  // user ids
    private final Collection<Long> following;  // user ids
    private UserScope scope;
    private Wrapped spotifyData;

    /**
     * Creates a new user.
     *
     * @param userid      the user's id
     * @param username    the user's username
     * @param password    the user's password
     * @param messages    the user's messages
     * @param likes       the user's likes
     * @param views       the user's views
     * @param followers   the user's followers
     * @param following   the user's following
     * @param scope       the user's scope
     * @param spotifyData the user's spotify data
     */
    public User(Long userid,
                String username,
                String password,
                Collection<Long> messages,
                Collection<Long> likes,
                Collection<Long> views,
                Collection<Long> followers,
                Collection<Long> following,
                UserScope scope,
                Wrapped spotifyData) {
        this.id = userid;
        this.username = username;
        this.password = password;
        this.messages = messages;
        this.likes = likes;
        this.views = views;
        this.followers = followers;
        this.following = following;
        this.scope = scope;
        this.spotifyData = spotifyData;
    }

    /**
     * Creates a new user.
     *
     * @param userid    the user's id
     * @param username  the user's username
     * @param password  the user's password
     * @param messages  the user's messages
     * @param likes     the user's likes
     * @param views     the user's views
     * @param followers the user's followers
     * @param following the user's following
     * @param scope     the user's scope
     */
    private User(Long userid,
                 String username,
                 String password,
                 Collection<Long> messages,
                 Collection<Long> likes,
                 Collection<Long> views,
                 Collection<Long> followers,
                 Collection<Long> following,
                 UserScope scope) {
        this(userid, username, password, messages, likes, views, followers, following, scope, null);
    }

    /**
     * Posts a message.
     *
     * @param message message to post
     */
    public void postMessage(Message message) {
        State.getInstance().addMessage(message);
        messages.add(message.getId());
    }

    /**
     * Deletes a message.
     *
     * @param message message to delete
     */
    public void deleteMessage(Message message) {
        message.delete();
    }

    /**
     * Likes a message.
     *
     * @param message message to like
     */
    public void likeMessage(Message message) {
        if (message == null) {
            throw new NullPointerException("Message cannot be null!");
        }
        if (!likes.contains(message.getId())) {
            message.like(this);
            likes.add(message.getId());
        }
    }

    /**
     * Unlikes a message.
     *
     * @param message message to unlike
     */
    public void unlikeMessage(Message message) {
        if (message == null) {
            throw new NullPointerException("Message cannot be null!");
        }
        message.unlike(this);
        likes.remove(message);
    }

    /**
     * Views a message.
     *
     * @param message message to view
     */
    public void viewMessage(Message message) {
        if (message == null) {
            throw new NullPointerException("Message cannot be null!");
        }
        if (!views.contains(message.getId())) {
            message.view(this);
            views.add(message.getId());
        }
    }

    /**
     * Replies to a message.
     *
     * @param message message to reply to
     * @param reply   reply message
     */
    public void replyToMessage(Message message, Message reply) {
        if (message == null || reply == null) {
            throw new NullPointerException("Message cannot be null!");
        }
        reply.setRootId(message.getRootId());
        reply.setParentId(message.getId());

        message.reply(reply);
        State.getInstance().addMessage(reply);
        messages.add(reply.getId());
    }

    /**
     * Follows a user.
     *
     * @param user user to follow
     */
    public void follow(User user) {
        user.followers.add(this.id);
        following.add(user.id);
    }

    /**
     * Unfollows a user.
     *
     * @param user user to unfollow
     */
    public void unfollow(User user) {
        user.followers.remove(this.id);
        following.remove(user.id);
    }

    /**
     * Gets the user's id.
     *
     * @return the user's id
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the user's username.
     *
     * @return the user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the user's password.
     *
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the user's messages.
     *
     * @return the user's messages
     */
    public Collection<Long> getMessages() {
        return messages;
    }

    /**
     * Gets the user's likes.
     *
     * @return the user's likes
     */
    public Collection<Long> getLikes() {
        return likes;
    }

    /**
     * Gets the user's views.
     *
     * @return the user's views
     */
    public Collection<Long> getViews() {
        return views;
    }

    /**
     * Gets the user's followers.
     *
     * @return the user's followers
     */
    public Collection<Long> getFollowers() {
        return followers;
    }

    /**
     * Gets the user's following.
     *
     * @return the user's following
     */
    public Collection<Long> getFollowing() {
        return following;
    }

    /**
     * Gets the user's scope.
     *
     * @return the user's scope
     */
    public UserScope getScope() {
        return scope;
    }

    /**
     * Gets the user's spotify data.
     *
     * @return the user's spotify data
     */
    public Wrapped getSpotifyData() {
        return spotifyData;
    }

    /**
     * Sets the user's id.
     *
     * @param id the user's id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the user's username.
     *
     * @param username the user's username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the user's password.
     *
     * @param password the user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the user's messages.
     *
     * @param messages the user's messages
     */
    public void setMessages(Collection<Long> messages) {
        this.messages.clear();
        this.messages.addAll(messages);
    }

    /**
     * Sets the user's likes.
     *
     * @param likes the user's likes
     */
    public void setLikes(Collection<Long> likes) {
        this.likes.clear();
        this.likes.addAll(likes);
    }

    /**
     * Sets the user's views.
     *
     * @param views the user's views
     */
    public void setViews(Collection<Long> views) {
        this.views.clear();
        this.views.addAll(views);
    }

    /**
     * Sets the user's followers.
     *
     * @param followers the user's followers
     */
    public void setFollowers(Collection<Long> followers) {
        this.followers.clear();
        this.followers.addAll(followers);
    }

    /**
     * Sets the user's following.
     *
     * @param following the user's following
     */
    public void setFollowing(Collection<Long> following) {
        this.following.clear();
        this.following.addAll(following);
    }

    /**
     * Sets the user's spotify data.
     *
     * @param spotifyData the user's spotify data
     */
    public void setSpotifyData(Wrapped spotifyData) {
        this.spotifyData = spotifyData;
    }

    /**
     * Sets the user's scope.
     *
     * @param scope the user's scope
     */
    public void setScope(UserScope scope) {
        this.scope = scope;
    }

    /**
     * Checks if the user is equal to another object.
     *
     * @param o the object to compare
     * @return true if the user is equal to the object, false otherwise
     */
    @Override
    public boolean equals(@Nullable Object o) {
        if (o instanceof User other) {
            return username.equals(other.username)
                    && messages.equals(other.messages)
                    && likes.equals(other.likes)
                    && views.equals(other.views)
                    && followers.equals(other.followers)
                    && following.equals(other.following)
                    && scope.equals(other.scope);
        }
        return false;
    }
}

