package com.example.cs_2340_assignment2.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cs_2340_assignment2.R;
import com.example.cs_2340_assignment2.data.User;
import com.example.cs_2340_assignment2.state.State;

public class ProfileSettings extends Fragment {

    private EditText editUsername;
    private EditText editPassword;

    private Button goBackButton;
    private Button deleteOption;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.profile_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI elements
        editUsername = view.findViewById(R.id.editUsername);
        editPassword = view.findViewById(R.id.editPassword);
        deleteOption = view.findViewById(R.id.button);

        // Retrieve current user data from State
        User currentUser = State.getInstance().getUsers().stream()
                .filter(u -> u.getUsername().equals(State.getInstance().getCurrentUser()))
                .findFirst()
                .orElse(null);

        if (currentUser != null) {
            editUsername.setText(currentUser.getUsername());
            editPassword.setText(currentUser.getPassword());
        } else {
            Toast.makeText(getContext(), "Error: No current user data found.", Toast.LENGTH_SHORT).show();
        }

        deleteOption.setOnClickListener(v -> showDeleteConfirmationDialog());

        Button goBackButton = view.findViewById(R.id.goback);
        // Setup go back button functionality
        goBackButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(ProfileSettings.this)
                    .navigate(R.id.action_profileSettingsFragment_to_thirdFragment);
        });

        // Save username button functionality
        ImageButton saveUsernameButton = view.findViewById(R.id.saveUsername);
        saveUsernameButton.setOnClickListener(v -> {
            String username = editUsername.getText().toString();
            if (!username.isEmpty() && currentUser != null) {
                currentUser.setUsername(username);
                State.getInstance().setCurrentUser(currentUser.getUsername());
                State.getInstance().addUser(currentUser);
                State.getInstance().writeToDB();
                Toast.makeText(getContext(), "Username updated successfully!", Toast.LENGTH_SHORT).show();
                Log.d("UpdateUsername", currentUser.getUsername());
            }
        });

        // Save password button functionality
        ImageButton savePasswordButton = view.findViewById(R.id.savePassword);
        savePasswordButton.setOnClickListener(v -> {
            String password = editPassword.getText().toString();
            if (!password.isEmpty() && currentUser != null) {
                currentUser.setPassword(password);
                State.getInstance().setCurrentUser(currentUser.getUsername());
                State.getInstance().addUser(currentUser);
                State.getInstance().writeToDB();
                Toast.makeText(getContext(), "Password updated successfully!", Toast.LENGTH_SHORT).show();
                Log.d("UpdatePassword", currentUser.getPassword());
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Profile")
                .setMessage("Are you sure you want to delete your profile? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> deleteProfile())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteProfile() {
        // Implementation for deleting the profile
        // This could be a database operation or an API call
        User currentUser = State.getInstance().getUsers().stream()
                .filter(u -> u.getUsername().equals(State.getInstance().getCurrentUser()))
                .findFirst()
                .orElse(null);

        if (currentUser != null) {
            State.getInstance().removeUser(currentUser);
            State.getInstance().writeToDB();
            Toast.makeText(getActivity(), "Your profile has been deleted", Toast.LENGTH_LONG).show();

            // Create an Intent to start LoginActivity
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            // Finish the current activity
            getActivity().finish();
        } else {
            Toast.makeText(getActivity(), "Error deleting profile", Toast.LENGTH_LONG).show();
        }
    }
}