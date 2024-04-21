package com.example.cs_2340_assignment2.data;

import com.example.cs_2340_assignment2.state.State;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Represents a message in the app.
 */
public class Message implements Serializable {

    private String id;
    private String rootId;
    private String parentId;

    private Collection<String> likes; // userids
    private Collection<String> views; // userids
    private Collection<String> replies; // messageids
    private MessageData content; // message data
    private Collection<String> authors; // userids
    private Date date; // date sent
    private boolean isDeleted;
    private UserScope scope;

    /**
     * Default constructor.
     */
    public Message() {
        this("", "0", new MessageData(), new ArrayList<>());
    }

    /**
     * Constructor with a message id, message data, and a collection of authors.
     *
     * @param id      message id
     * @param content message data
     * @param authors collection of authors
     */
    public Message(String id, MessageData content, Collection<String> authors) {
        this("", id, content, authors);
    }

    /**
     * Constructor with a parent message id, message id, message data, and a collection of authors.
     * (id "" is root).
     *
     * @param parent  parent message id
     * @param id      message id
     * @param content message data
     * @param authors collection of authors
     */
    public Message(String parent, String id, MessageData content, Collection<String> authors) {
        if (parent.equals("")) {
            this.rootId = "";
            this.parentId = "";
        } else {
            try {
                this.rootId = Objects.requireNonNull(State.getInstance().getMessages().get(parent)).rootId;
                this.parentId = parent;
            } catch (NullPointerException e) {
                this.rootId = "";
                this.parentId = "";
            }
        }
        this.id = id;
        this.content = content;
        this.authors = authors;
        this.likes = new ArrayList<>();
        this.views = new ArrayList<>();
        this.replies = new ArrayList<>();
        isDeleted = false;
    }

    /**
     * Constructor with a root message id, parent message id, message id, message data, a collection of authors, a collection of likes, a collection of views, a collection of replies, a date, and a boolean.
     *
     * @param root      root message id
     * @param parent    parent message id
     * @param id        message id
     * @param data      message data
     * @param authors   collection of authors
     * @param likes     collection of likes
     * @param views     collection of views
     * @param replies   collection of replies
     * @param date      date
     * @param isDeleted boolean
     */
    public Message(String root,
                   String parent,
                   String id,
                   MessageData data,
                   List<String> authors,
                   List<String> likes,
                   List<String> views,
                   List<String> replies,
                   Date date,
                   boolean isDeleted) {
        this.rootId = root;
        this.parentId = parent;
        this.id = id;
        this.content = data;
        this.authors = authors;
        this.likes = likes;
        this.views = views;
        this.replies = replies;
        this.date = date;
        this.isDeleted = isDeleted;
    }

    /**
     * Get the message id.
     *
     * @return message id
     */
    public String getId() {
        return id;
    }

    /**
     * Get the root message id.
     *
     * @return root message id
     */
    public String getRootId() {
        return rootId;
    }

    /**
     * Get the parent message id.
     *
     * @return parent message id
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * Get the collection of likes.
     *
     * @return collection of likes
     */
    public Collection<String> getLikes() {
        return likes;
    }

    /**
     * Get the collection of views.
     *
     * @return collection of views
     */
    public Collection<String> getViews() {
        return views;
    }

    /**
     * Get the collection of replies.
     *
     * @return collection of replies
     */
    public Collection<String> getReplies() {
        return replies;
    }

    /**
     * Get the message data.
     *
     * @return message data
     */
    public MessageData getContent() {
        return content;
    }

    /**
     * Get the collection of authors.
     *
     * @return collection of authors
     */
    public Collection<String> getAuthors() {
        return authors;
    }

    /**
     * Get the date.
     *
     * @return date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Get the boolean isDeleted.
     *
     * @return isDeleted
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * Set the message id.
     *
     * @param id message id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Set the root message id.
     *
     * @param rootId root message id
     */
    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    /**
     * Set the parent message id.
     *
     * @param parentId parent message id
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * Sets the collection of likes.
     *
     * @param likes collection of likes
     */
    public void setLikes(Collection<String> likes) {
        this.likes = likes;
    }

    /**
     * Sets the collection of views.
     *
     * @param views collection of views
     */
    public void setViews(Collection<String> views) {
        this.views = views;
    }

    /**
     * Sets the collection of replies.
     *
     * @param replies collection of replies
     */
    public void setReplies(Collection<String> replies) {
        this.replies = replies;
    }

    /**
     * Sets the message data.
     *
     * @param content message data
     */
    public void setContent(MessageData content) {
        this.content = content;
    }

    /**
     * Sets the collection of authors.
     *
     * @param authors collection of authors
     */
    public void setAuthors(Collection<String> authors) {
        this.authors = authors;
    }

    /**
     * Sets the date.
     *
     * @param date date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Sets the boolean isDeleted.
     *
     * @param deleted boolean
     */
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    /**
     * Add an author to the collection of authors.
     *
     * @param user user
     */
    public void addAuthor(User user) {
        authors.add(user.getId());
    }

    /**
     * Remove an author from the collection of authors.
     *
     * @param user user
     */
    public void like(User user) {
        likes.add(user.getId());
    }

    /**
     * Remove a like from the collection of likes.
     *
     * @param user user
     */
    public void unlike(User user) {
        likes.remove(user.getId());
    }

    /**
     * Add a view to the collection of views.
     *
     * @param user user
     */
    public void view(User user) {
        views.add(user.getId());
    }

    /**
     * Add a reply to the collection of replies.
     *
     * @param reply reply
     */
    public void reply(Message reply) {
        replies.add(reply.id);
        reply.parentId = id;
        reply.rootId = rootId;
    }

    /**
     * Delete the message by marking it as deleted.
     */
    public void delete() {
        isDeleted = true;
    }

    /**
     * Delete a reply by removing it from the collection of replies.
     *
     * @param messageId message id
     */
    public void deleteReply(String messageId) {
        replies.remove(messageId);
    }

    /**
     * Get the scope of the message.
     * @return scope
     */
    public UserScope getScope() {
        return scope;
    }

    /**
     * Set the scope of the message.
     * @param scope scope
     */
    public void setScope(UserScope scope) {
        this.scope = scope;
    }

    /**
     * Equals method for Message.
     *
     * @param o object to compare
     * @return true if equal, false otherwise
     */
    public boolean equals(Object o) {
        if (o instanceof Message) {
            Message other = (Message) o;
            return id == other.id
                    && content.equals(other.content)
                    && authors.equals(other.authors)
                    && date.equals(other.date)
                    && isDeleted == other.isDeleted;
        }
        return false;
    }
}
