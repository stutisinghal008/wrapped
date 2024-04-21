package com.example.cs_2340_assignment2.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cs_2340_assignment2.data.spotify.Wrapped;
import com.example.cs_2340_assignment2.state.State;
import com.example.cs_2340_assignment2.ui.MainActivity;
import com.example.cs_2340_assignment2.R;

import java.util.List;

public class TopSongs extends Fragment {
    private ProgressBar storyProgressBar;
    private Handler progressHandler = new Handler(Looper.getMainLooper());
    private int progressStatus = 0;
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
        View view = inflater.inflate(R.layout.top_songs, container, false);
        storyProgressBar = view.findViewById(R.id.storyProgressBar);
        username = view.findViewById(R.id.userName);
        username.setText(State.getInstance().getCurrentUser());
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
            ((MainActivity) getActivity()).setFragmentReady2(true);
        }
    }

    private void closeFragmentSmoothly() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).removeSongsWithAnimation();
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
            ((MainActivity) getActivity()).setFragmentReady2(false);
        }
    }

    public void updateSongsData(List<Wrapped.Track> tracks) {
        if (getView() != null) {
            if (tracks.size() >= 5) {
                ((TextView) getView().findViewById(R.id.textViewSong1)).setText(tracks.get(0).getTitle());
                ((TextView) getView().findViewById(R.id.tvArtist1)).setText(tracks.get(0).getArtists().toString());
                ((TextView) getView().findViewById(R.id.textViewSong2)).setText(tracks.get(1).getTitle());
                ((TextView) getView().findViewById(R.id.tvArtist2)).setText(tracks.get(1).getArtists().toString());
                ((TextView) getView().findViewById(R.id.textViewSong3)).setText(tracks.get(2).getTitle());
                ((TextView) getView().findViewById(R.id.tvArtist3)).setText(tracks.get(2).getArtists().toString());
                ((TextView) getView().findViewById(R.id.textViewSong4)).setText(tracks.get(3).getTitle());
                ((TextView) getView().findViewById(R.id.tvArtist4)).setText(tracks.get(3).getArtists().toString());
                ((TextView) getView().findViewById(R.id.textViewSong5)).setText(tracks.get(4).getTitle());
                ((TextView) getView().findViewById(R.id.tvArtist5)).setText(tracks.get(4).getArtists().toString());
            }
        }
    }
}
