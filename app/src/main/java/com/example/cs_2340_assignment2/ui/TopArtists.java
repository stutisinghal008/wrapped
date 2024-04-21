package com.example.cs_2340_assignment2.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.cs_2340_assignment2.data.spotify.Wrapped;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cs_2340_assignment2.R;
import com.example.cs_2340_assignment2.state.State;

import java.util.List;

public class TopArtists extends Fragment {
    private ProgressBar storyProgressBar;
    private Handler progressHandler = new Handler(Looper.getMainLooper());
    private int progressStatus = 0;

    private TextView artistNameTextView;
    private TextView genresTextView;

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
        View view = inflater.inflate(R.layout.top_artist, container, false);
        storyProgressBar = view.findViewById(R.id.storyProgressBar);
        username = view.findViewById(R.id.userName);
        username.setText(State.getInstance().getCurrentUser());
        artistNameTextView = view.findViewById(R.id.artistName1);
        genresTextView = view.findViewById(R.id.genre1);
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
            ((MainActivity) getActivity()).setFragmentReady3(true);
        }
    }

    private void closeFragmentSmoothly() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).removeArtistWithAnimation();
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
    public void onResume() {
        super.onResume();
        progressHandler.post(progressRunnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        progressHandler.removeCallbacks(progressRunnable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        showBottomActionBar();
        // Remove callbacks to avoid memory leaks.
        progressHandler.removeCallbacks(progressRunnable);
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setFragmentReady3(false);
        }
    }

    public void updateArtistData(List<Wrapped.Artist> artists) {
        if (getView() != null) {
            if (artists.size() >= 3) {
                ((TextView) getView().findViewById(R.id.artistName1)).setText(artists.get(0).getName());
                TextView genreTextView = getView().findViewById(R.id.genre1);
                if (artists != null && !artists.isEmpty() && artists.get(0).getGenres() != null) {
                    String genresText = String.join(", ", artists.get(0).getGenres());
                    genreTextView.setText(genresText);
                } else {
                    genreTextView.setText("No genres available");
                }
                ((TextView) getView().findViewById(R.id.artist2)).setText(artists.get(1).getName());
                TextView genreTextView2 = getView().findViewById(R.id.genre2);
                if (artists != null && !artists.isEmpty() && artists.get(1).getGenres() != null) {
                    String genresText2 = String.join(", ", artists.get(1).getGenres());
                    genreTextView2.setText(genresText2);
                } else {
                    genreTextView2.setText("No genres available");
                }
                ((TextView) getView().findViewById(R.id.artist3)).setText(artists.get(2).getName());
                TextView genreTextView3 = getView().findViewById(R.id.genre3);
                if (artists != null && !artists.isEmpty() && artists.get(2).getGenres() != null) {
                    String genresText3 = String.join(", ", artists.get(2).getGenres());
                    genreTextView3.setText(genresText3);
                } else {
                    genreTextView3.setText("No genres available");
                }
            }
        }
    }
}
