package com.example.cs_2340_assignment2.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cs_2340_assignment2.R;
import com.example.cs_2340_assignment2.data.User;
import com.example.cs_2340_assignment2.data.UserScope;
import com.example.cs_2340_assignment2.data.spotify.Wrapped;
import com.example.cs_2340_assignment2.data.spotify.auth.PkceUtil;
import com.example.cs_2340_assignment2.data.spotify.auth.TokenExchangeUtil;
import com.example.cs_2340_assignment2.state.State;

import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

public class SignupActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "3f6e08b6307b478398c6811e6d85a012";
    private static final String REDIRECT_URI = "myappspotifyauth://callback";
    private static final String SCOPES = "user-read-private playlist-read-private user-top-read";
    private static final int SPOTIFY_LOGIN_REQUEST = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final EditText usernameInput = findViewById(R.id.nameInput);
        final EditText passwordInput = findViewById(R.id.usernameInput);
        final EditText confirmPasswordInput = findViewById(R.id.passwordInput);
        final Button signUpButton = findViewById(R.id.signUpButton);



        TextView loginLink = findViewById(R.id.login_link);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        signUpButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();
            String confirmPassword = confirmPasswordInput.getText().toString();

            boolean fail = false;

            if (password.isBlank() || password.isEmpty() || username.isBlank() || username.isEmpty()) {
                Log.e("SignupActivity", "Username or password is empty");
                fail = true;
            }

            Collection<User> users = State.getInstance().getUsers();
            if (!fail) {
                for (User user : users) {
                    if (!fail && user.getUsername().equals(username)) {
                        loginLink.setText("Username already exists!");
                        usernameInput.setText("");
                        passwordInput.setText("");
                        Log.e("SignupActivity", "Username already exists");
                        fail = true;
                    }
                }
            }

            if (!fail && !password.equals(confirmPassword)) {
                usernameInput.setText("");
                confirmPasswordInput.setText("Passwords do not match!".toCharArray(), 0, 24);
                fail = true;
            }


            if (!fail) {
                User u = new User(
                        username,
                        username,
                        password,
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        UserScope.PRIVATE,
                        new Wrapped()
                );
                State.getInstance().setCurrentUser(u.getUsername());
                State.getInstance().addUser(u);
                State.getInstance().writeToDB();
                finish();
            } else {
                Looper.loop();
            }

            try {
                initiateSpotifyLogin(username, password);
            } catch (NoSuchAlgorithmException e) {
                Log.e("SignupActivity", "Error generating code challenge", e);
            }
        });
    }

    private void initiateSpotifyLogin(String username, String password) throws NoSuchAlgorithmException {
        String codeVerifier = PkceUtil.generateCodeVerifier();
        String codeChallenge = PkceUtil.generateCodeChallenge(codeVerifier);

        String authUrl = "https://accounts.spotify.com/authorize" +
                "?client_id=" + CLIENT_ID +
                "&response_type=code" +
                "&redirect_uri=" + Uri.encode(REDIRECT_URI) +
                "&code_challenge_method=S256" +
                "&code_challenge=" + codeChallenge +
                "&scope=" + Uri.encode(SCOPES);

        SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
        editor.putString("code_verifier", codeVerifier);
        editor.apply();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl));
        intent.putExtra("code_verifier", codeVerifier); // Pass the code verifier as an extra
        Log.d("SignupActivity", "Navigating to Spotify login");
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != SPOTIFY_LOGIN_REQUEST || resultCode != RESULT_OK) {
            return;
        }
        // Handle the redirect with the authorization code
        // Create user and save to state here, after successful Spotify login
        String username = data.getStringExtra("username");
        String password = data.getStringExtra("password");
    }
}
