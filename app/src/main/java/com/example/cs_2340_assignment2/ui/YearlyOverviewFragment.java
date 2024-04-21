package com.example.cs_2340_assignment2.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cs_2340_assignment2.data.Message;
import com.example.cs_2340_assignment2.data.MessageData;
import com.example.cs_2340_assignment2.data.UserScope;
import com.example.cs_2340_assignment2.data.spotify.Wrapped;
import com.example.cs_2340_assignment2.state.State;
import com.example.cs_2340_assignment2.ui.MainActivity;
import com.example.cs_2340_assignment2.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class YearlyOverviewFragment extends Fragment {
    private ProgressBar storyProgressBar;
    private Handler progressHandler = new Handler(Looper.getMainLooper());
    private int progressStatus = 0;
    private ImageButton postButton;

    private TextView username;

    private final Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            if (progressStatus < 100) {
                progressStatus += 3.33; // Increment the progress
                storyProgressBar.setProgress((int) Math.round(progressStatus)); // Set the progress on the progress bar, rounded to nearest whole number
                progressHandler.postDelayed(this, 100); // Continue updating every 100ms
            } else {
                storyProgressBar.setProgress(100); // Ensure it sets to 100 at the end
                closeFragmentSmoothly(); // Close the fragment once the progress completes
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yearly_overview, container, false);
        postButton = view.findViewById(R.id.addPostButton);
        storyProgressBar = view.findViewById(R.id.storyProgressBar);
        username = view.findViewById(R.id.userName);
        username.setText(State.getInstance().getCurrentUser());
        State.getInstance().readFromDB();

        postButton.setOnClickListener(v -> {
            // Close the fragment when the button is clicked.
            MessageData m = new MessageData();
            m.setText("");
            List<Wrapped.Artist> artists = new ArrayList<Wrapped.Artist>();
            List<Wrapped.Track> tracks = new ArrayList<Wrapped.Track>();

            PostAdapter.Post p = new PostAdapter.Post();

            String genre = ((TextView) view.findViewById(R.id.textViewGenre)).getText().toString();

            if (genre.equals("")) {
                return;
            }

            String a1 = ((TextView) view.findViewById(R.id.textViewArtist1)).getText().toString();
            String t1 = ((TextView) view.findViewById(R.id.textViewSong1)).getText().toString();
            String a2 = ((TextView) view.findViewById(R.id.textViewArtist2)).getText().toString();
            String t2 = ((TextView) view.findViewById(R.id.textViewSong2)).getText().toString();
            String a3 = ((TextView) view.findViewById(R.id.textViewArtist3)).getText().toString();
            String t3 = ((TextView) view.findViewById(R.id.textViewSong3)).getText().toString();
            String a4 = ((TextView) view.findViewById(R.id.textViewArtist4)).getText().toString();
            String t4 = ((TextView) view.findViewById(R.id.textViewSong4)).getText().toString();
            String a5 = ((TextView) view.findViewById(R.id.textViewArtist5)).getText().toString();
            String t5 = ((TextView) view.findViewById(R.id.textViewSong5)).getText().toString();

            Wrapped w = new Wrapped(tracks, artists);
            Collection<String> authors = new ArrayList<>();
            authors.add(State.getInstance().getCurrentUser());
            m.setData(w);
            Message message = new Message(String.valueOf(m.hashCode()), m, authors);
            message.setScope(UserScope.PUBLIC);

            message.setDeleted(false);
            message.setDate(new Date());
            message.setViews(new ArrayList<>());
            message.setLikes(new ArrayList<>());
            message.setReplies(new ArrayList<>());
            message.setRootId(message.getId());
            message.setParentId(message.getId());

            var users = State.getInstance().getUsers();
            for (var user : users) {
                if (user.getUsername().equals(State.getInstance().getCurrentUser())) {
                    user.setSpotifyData(w);
                }
            }

            p.setId(message.getId());
            p.setAuthor(State.getInstance().getCurrentUser());
            p.setArtist1(a1);
            p.setArtist2(a2);
            p.setArtist3(a3);
            p.setArtist4(a4);
            p.setArtist5(a5);
            p.setTrack1(t1);
            p.setTrack2(t2);
            p.setTrack3(t3);
            p.setTrack4(t4);
            p.setTrack5(t5);
            p.setGenre(genre);
            p.setTime(System.currentTimeMillis());

            State.getInstance().addPost(p);
            State.getInstance().addMessage(message);
            State.getInstance().writeToDB();
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Hide bottom action bar.
        hideBottomActionBar();
        // Start the progress.
        progressHandler.post(progressRunnable);
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setFragmentReady(true);
        }
    }

    private void closeFragmentSmoothly() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).removeYearlyOverviewFragmentWithAnimation();
        }
    }

    private void hideBottomActionBar() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideBottomActionBar();
        }
    }

    private void showBottomActionBar() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showBottomActionBar();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Show bottom action bar as the fragment is destroyed.
        showBottomActionBar();
        // Remove callbacks to avoid memory leaks.
        progressHandler.removeCallbacks(progressRunnable);
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setFragmentReady(false);
        }
    }

    public void updateData(List<Wrapped.Artist> artists, List<Wrapped.Track> tracks, String mostCommonGenre) {
        if (getView() != null) {
            if (artists.size() >= 5) {
                ((TextView) getView().findViewById(R.id.textViewArtist1)).setText(artists.get(0).getName());
                ((TextView) getView().findViewById(R.id.textViewArtist2)).setText(artists.get(1).getName());
                ((TextView) getView().findViewById(R.id.textViewArtist3)).setText(artists.get(2).getName());
                ((TextView) getView().findViewById(R.id.textViewArtist4)).setText(artists.get(3).getName());
                ((TextView) getView().findViewById(R.id.textViewArtist5)).setText(artists.get(4).getName());
            }

            if (tracks.size() >= 5) {
                ((TextView) getView().findViewById(R.id.textViewSong1)).setText(tracks.get(0).getTitle());
                ((TextView) getView().findViewById(R.id.textViewSong2)).setText(tracks.get(1).getTitle());
                ((TextView) getView().findViewById(R.id.textViewSong3)).setText(tracks.get(2).getTitle());
                ((TextView) getView().findViewById(R.id.textViewSong4)).setText(tracks.get(3).getTitle());
                ((TextView) getView().findViewById(R.id.textViewSong5)).setText(tracks.get(4).getTitle());
            }
            ((TextView) getView().findViewById(R.id.textViewGenre)).setText(mostCommonGenre);
        }
    }
}
