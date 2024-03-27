package com.example.cs_2340_assignment2.data;

import com.example.cs_2340_assignment2.state.State;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Represents a message in the app.
 */
public class Message implements Serializable {
    private long id;
    private long rootId;
    private long parentId;

    private Collection<Long> likes; // userids
    private Collection<Long> views; // userids
    private Collection<Long> replies; // messageids

    private MessageData content; // message data
    private Collection<Long> authors; // userids
    private Date date; // date sent
    private boolean isDeleted;

    /**
     * Default constructor.
     */
    public Message() {
        this(0, 0, new MessageData(), new ArrayList<>());
    }

    /**
     * Constructor with a message id, message data, and a collection of authors.
     *
     * @param id      message id
     * @param content message data
     * @param authors collection of authors
     */
    public Message(long id, MessageData content, Collection<Long> authors) {
        this(0, id, content, authors);
    }

    /**
     * Constructor with a parent message id, message id, message data, and a collection of authors.
     * (id 0 is root).
     *
     * @param parent  parent message id
     * @param id      message id
     * @param content message data
     * @param authors collection of authors
     */
    public Message(long parent, long id, MessageData content, Collection<Long> authors) {
        if (parent == 0) {
            this.rootId = 0;
            this.parentId = 0;
        } else {
            this.rootId = State.getInstance().getMessages().get((int) parent).rootId;
            this.parentId = parent;
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
    public Message(long root,
                   long parent,
                   long id,
                   MessageData data,
                   List<Long> authors,
                   List<Long> likes,
                   List<Long> views,
                   List<Long> replies,
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
    public long getId() {
        return id;
    }

    /**
     * Get the root message id.
     *
     * @return root message id
     */
    public long getRootId() {
        return rootId;
    }

    /**
     * Get the parent message id.
     *
     * @return parent message id
     */
    public long getParentId() {
        return parentId;
    }

    /**
     * Get the collection of likes.
     *
     * @return collection of likes
     */
    public Collection<Long> getLikes() {
        return likes;
    }

    /**
     * Get the collection of views.
     *
     * @return collection of views
     */
    public Collection<Long> getViews() {
        return views;
    }

    /**
     * Get the collection of replies.
     *
     * @return collection of replies
     */
    public Collection<Long> getReplies() {
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
    public Collection<Long> getAuthors() {
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
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Set the root message id.
     *
     * @param rootId root message id
     */
    public void setRootId(long rootId) {
        this.rootId = rootId;
    }

    /**
     * Set the parent message id.
     *
     * @param parentId parent message id
     */
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    /**
     * Sets the collection of likes.
     *
     * @param likes collection of likes
     */
    public void setLikes(Collection<Long> likes) {
        this.likes = likes;
    }

    /**
     * Sets the collection of views.
     *
     * @param views collection of views
     */
    public void setViews(Collection<Long> views) {
        this.views = views;
    }

    /**
     * Sets the collection of replies.
     *
     * @param replies collection of replies
     */
    public void setReplies(Collection<Long> replies) {
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
    public void setAuthors(Collection<Long> authors) {
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
    public void deleteReply(long messageId) {
        replies.remove(messageId);
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
