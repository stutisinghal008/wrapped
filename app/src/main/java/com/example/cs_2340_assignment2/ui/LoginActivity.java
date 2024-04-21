package com.example.cs_2340_assignment2.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs_2340_assignment2.R;
import com.example.cs_2340_assignment2.data.User;
import com.example.cs_2340_assignment2.data.spotify.Wrapped;
import com.example.cs_2340_assignment2.state.State;
import com.example.cs_2340_assignment2.ui.SignupActivity;
import com.example.cs_2340_assignment2.data.spotify.auth.PkceUtil;
import com.example.cs_2340_assignment2.ui.MainActivity;

import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final TextView signUpOption = findViewById(R.id.signup);


        State.getInstance().readFromDB();
        loginButton.setOnClickListener(v -> {
            Log.d("LoginActivity", "Login button clicked");

            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                return;
            }
            Collection<User> users = State.getInstance().getUsers();
            if (users.isEmpty()) {
                usernameEditText.setText("No users in database!".toCharArray(), 0, 21);
                passwordEditText.setText("");
                return;
            }
            for (User user : users) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    Wrapped w = user.getSpotifyData();
                    user.setSpotifyData(w);
                    State.getInstance().setCurrentUser(user.getUsername());
                    Log.d("LoginActivity", "Login successful");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // This flag is typically used to clear the activity stack up to MainActivity, but we'll interpret it as "coming from login".
                    startActivity(intent);
                    finish();
                    return;
                }
            }

            usernameEditText.setText("Username or Password Incorrect!".toCharArray(), 0, 27);
            passwordEditText.setText("");
        });


        signUpOption.setOnClickListener(v -> {
            Log.e("LoginActivity", "generating code challenge");
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
            finish();
        });
        // You need to override onActivityResult to handle the redirect from Spotify login
    }
}

