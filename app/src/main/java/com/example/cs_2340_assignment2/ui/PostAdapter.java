package com.example.cs_2340_assignment2.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cs_2340_assignment2.R;
import com.example.cs_2340_assignment2.data.Message;
import com.example.cs_2340_assignment2.data.UserScope;
import com.example.cs_2340_assignment2.data.spotify.Wrapped;
import com.example.cs_2340_assignment2.state.State;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;

/**
 * When a user posts a Wrapped (sets the message scope to public) this class enables it to be
 * displayed in the feed.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Collection<Wrapped> wraps;
    private Collection<Post> posts;
    public PostAdapter() {
        if (posts == null) {
            posts = new ArrayList<>();
        }
        wraps = new ArrayList<>();
        posts.addAll(State.getInstance().getPostsAsList());
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_fragment, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        State.getInstance().readFromDB();
        Post p = (Post) posts.toArray()[position];
        holder.author.setText(p.author);
        holder.postArtist1.setText(p.artist1);
        holder.postArtist2.setText(p.artist2);
        holder.postArtist3.setText(p.artist3);
        holder.postArtist4.setText(p.artist4);
        holder.postArtist5.setText(p.artist5);
        holder.postTrack1.setText(p.track1);
        holder.postTrack2.setText(p.track2);
        holder.postTrack3.setText(p.track3);
        holder.postTrack4.setText(p.track4);
        holder.postTrack5.setText(p.track5);
        holder.postGenre.setText(p.genre);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView postArtist1;
        TextView postArtist2;
        TextView postArtist3;
        TextView postArtist4;
        TextView postArtist5;
        TextView postTrack1;
        TextView postTrack2;
        TextView postTrack3;
        TextView postTrack4;
        TextView postTrack5;
        TextView postGenre;


        public PostViewHolder(View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.post_author);
            postArtist1 = itemView.findViewById(R.id.post_textViewArtist1);
            postArtist2 = itemView.findViewById(R.id.post_textViewArtist2);
            postArtist3 = itemView.findViewById(R.id.post_textViewArtist3);
            postArtist4 = itemView.findViewById(R.id.post_textViewArtist4);
            postArtist5 = itemView.findViewById(R.id.post_textViewArtist5);
            postTrack1 = itemView.findViewById(R.id.post_textViewSong1);
            postTrack2 = itemView.findViewById(R.id.post_textViewSong2);
            postTrack3 = itemView.findViewById(R.id.post_textViewSong3);
            postTrack4 = itemView.findViewById(R.id.post_textViewSong4);
            postTrack5 = itemView.findViewById(R.id.post_textViewSong5);
            postGenre = itemView.findViewById(R.id.post_textViewGenre);
        }
    }

    public static class Post {
        private String id;
        private Long time;
        private String author;
        private String artist1;
        private String artist2;
        private String artist3;
        private String artist4;
        private String artist5;
        private String track1;
        private String track2;
        private String track3;
        private String track4;
        private String track5;
        private String genre;

        public Post() {

        }

        public Post(String author, String artist1, String artist2, String artist3, String artist4, String artist5, String track1, String track2, String track3, String track4, String track5, String genre) {
            this.author = author;
            this.artist1 = artist1;
            this.artist2 = artist2;
            this.artist3 = artist3;
            this.artist4 = artist4;
            this.artist5 = artist5;
            this.track1 = track1;
            this.track2 = track2;
            this.track3 = track3;
            this.track4 = track4;
            this.track5 = track5;
            this.genre = genre;
        }

        public String getAuthor() {
            return author;
        }

        public String getArtist1() {
            return artist1;
        }

        public String getArtist2() {
            return artist2;
        }

        public String getArtist3() {
            return artist3;
        }

        public String getArtist4() {
            return artist4;
        }

        public String getArtist5() {
            return artist5;
        }

        public String getTrack1() {
            return track1;
        }

        public String getTrack2() {
            return track2;
        }

        public String getTrack3() {
            return track3;
        }

        public String getTrack4() {
            return track4;
        }

        public String getTrack5() {
            return track5;
        }

        public String getGenre() {
            return genre;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setArtist1(String artist1) {
            this.artist1 = artist1;
        }

        public void setArtist2(String artist2) {
            this.artist2 = artist2;
        }

        public void setArtist3(String artist3) {
            this.artist3 = artist3;
        }

        public void setArtist4(String artist4) {
            this.artist4 = artist4;
        }

        public void setArtist5(String artist5) {
            this.artist5 = artist5;
        }

        public void setTrack1(String track1) {
            this.track1 = track1;
        }

        public void setTrack2(String track2) {
            this.track2 = track2;
        }

        public void setTrack3(String track3) {
            this.track3 = track3;
        }

        public void setTrack4(String track4) {
            this.track4 = track4;
        }

        public void setTrack5(String track5) {
            this.track5 = track5;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }
    }
}
