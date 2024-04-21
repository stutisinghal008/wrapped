package com.example.cs_2340_assignment2.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cs_2340_assignment2.R;

public class MyDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // Set custom animation
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.settings_modal, container, false);

        // Set up the "My Profile" button
        Button myProfileButton = view.findViewById(R.id.tvMyProfile);
        myProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMyProfileFragment();
            }

        });
        Button logoutButton = view.findViewById(R.id.tvLogOut);
        logoutButton.setOnClickListener(v -> navigateToLoginActivity());

        return view;
    }

    private void navigateToMyProfileFragment() {
        // Navigate to MyProfile fragment using the Navigation component
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_myDialogFragment_to_myProfileFragment);
    }
    private void navigateToLoginActivity() {
        // Create an Intent to start LoginActivity
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        // Add flags to clear the back stack and start fresh on the login screen
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        // Optional: if you want to close the current activity
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}
