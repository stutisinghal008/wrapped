package com.example.cs_2340_assignment2.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cs_2340_assignment2.R;
import com.example.cs_2340_assignment2.data.User;
import com.example.cs_2340_assignment2.data.spotify.auth.PkceUtil;
import com.example.cs_2340_assignment2.data.spotify.auth.TokenExchangeUtil;
import com.example.cs_2340_assignment2.data.spotify.Wrapped;
import com.example.cs_2340_assignment2.databinding.ActivityMainBinding;
import com.example.cs_2340_assignment2.state.State;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ThirdFragment.OnMyButtonClickListener, ThirdFragment.OnMyButtonTwoClickListener, ThirdFragment.OnMyButtonThreeClickListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private String selectedTerm1;
    private boolean isLoginActivitySource;
    private String authCode;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());


    private String accessTokenFinal;
    private boolean isFragmentReady = false;
    public void setFragmentReady(boolean ready) {
        isFragmentReady = ready;
        if (isFragmentReady && accessTokenFinal != null) {
            Log.d("ready year", "ready year");
            updateYearlyOverviewFragment();
        }
    }
    private boolean isFragmentReady2 = false;
    public void setFragmentReady2(boolean ready) {
        isFragmentReady2 = ready;
        if (isFragmentReady2 && accessTokenFinal != null) {
            updateSongsFragment();
        }
    }
    private boolean isFragmentReady3 = false;
    public void setFragmentReady3(boolean ready) {
        isFragmentReady3 = ready;
        if (isFragmentReady3 && accessTokenFinal != null) {
            updateArtistsFragment();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity.java", "pp: "+String.valueOf((getIntent().getFlags() & Intent.FLAG_ACTIVITY_NEW_TASK)));
//
//        // In MainActivity's onCreate or a similar lifecycle method
//        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_NEW_TASK) != 0) {
//            Log.d("MainActivity", "Came from LoginActivity with FLAG_ACTIVITY_NEW_TASK");
//            isLoginActivitySource=true;
//            Log.d("MainActivity", "Came from LoginActivity with FLAG_ACTIVITY_NEW_TASK"+isLoginActivitySource);
//            String refreshToken = getRefreshToken();
//            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//            String codeVerifier1 = prefs.getString("code_verifier", null);
//            new TokenExchangeTask().execute(authCode, codeVerifier1);
//        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().hide();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        // Setting up BottomNavigationView with NavController
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(navView, navController);

        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        initializeAccessToken();
        checkSourceAndAuthenticate();
        Log.d("MainActivity.java", "onCreate()");

    }

    private void initializeAccessToken() {
        accessTokenFinal = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("accessToken", null);
        if (accessTokenFinal != null) {
            TokenExchangeUtil.getSpotifyApi().setAccessToken(accessTokenFinal);
        }
    }

    private void checkSourceAndAuthenticate() {
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_NEW_TASK) == 0) {
            isLoginActivitySource = true;
            String refreshToken = getRefreshToken(State.getInstance().getCurrentUser());
            if (refreshToken != null) {
                refreshAccessToken(refreshToken);
            } else {
                Log.d("MainActivity", "No refresh token available");
            }
        }
    }

    private String getRefreshToken(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getString(username + "_refreshToken", null);
    }
    private void refreshAccessToken(String refreshToken) {
        executorService.execute(() -> {
            try {
                final String newAccessToken = TokenExchangeUtil.refreshAccessToken(refreshToken);
                handler.post(() -> {
                    if (newAccessToken != null) {
                        handleNewAccessToken(newAccessToken);
                    } else {
                        promptUserReAuthentication();
                    }
                });
            } catch (Exception e) {
                Log.e("MainActivity", "Error refreshing token: " + e.getMessage());
                handler.post(this::promptUserReAuthentication);
            }
        });
    }

    private void handleNewAccessToken(String newAccessToken) {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("accessToken", newAccessToken);
        editor.apply();
        accessTokenFinal = newAccessToken;
        TokenExchangeUtil.getSpotifyApi().setAccessToken(newAccessToken);
        updateFragmentsAfterTokenRefresh();
    }

    private void handleTokenError(String error) {
        Log.e("MainActivity", "Token error: " + error);
        if ("invalid_grant".equals(error) || "invalid_token".equals(error) || "null_response".equals(error) || "json_parsing_error".equals(error)) {
            promptUserReAuthentication();
        }
    }

    private void promptUserReAuthentication() {
        Toast.makeText(this, "Your session has expired. Please sign in again.", Toast.LENGTH_LONG).show();
        try {
            initiateSpotifyLogin(); // Trigger Spotify login process directly
        } catch (NoSuchAlgorithmException e) {
            Log.e("MainActivity", "Error initializing Spotify login", e);
        }
    }


    private static final String CLIENT_ID = "3f6e08b6307b478398c6811e6d85a012";
    private static final String REDIRECT_URI = "myappspotifyauth://callback";
    private static final String SCOPES = "user-read-private playlist-read-private user-top-read";
    private static final int SPOTIFY_LOGIN_REQUEST = 1;
    private void initiateSpotifyLogin() throws NoSuchAlgorithmException {
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
    private void updateFragmentsAfterTokenRefresh() {
        if (isFragmentReady) updateYearlyOverviewFragment();
        if (isFragmentReady2) updateSongsFragment();
        if (isFragmentReady3) updateArtistsFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onMyButtonClick(String selectedTerm) {
        selectedTerm1=selectedTerm;
        YearlyOverviewFragment yearlyOverviewFragment = new YearlyOverviewFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(
                R.anim.story_fade_in, // enter
                R.anim.story_fade_out, // exit
                R.anim.story_fade_in, // popEnter
                R.anim.story_fade_out // popExit
        );
        transaction.add(R.id.container, yearlyOverviewFragment, "YearlyOverviewFragmentTag");
        transaction.addToBackStack(null);
        transaction.commit();

        Log.d("MainActivity.java", "onMyButtonClick()");
    }

    @Override
    public void onMyButtonThreeClick(String selectedTerm) {
        selectedTerm1=selectedTerm;
        TopSongs songs = new TopSongs();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(
                R.anim.story_fade_in, // enter
                R.anim.story_fade_out, // exit
                R.anim.story_fade_in, // popEnter
                R.anim.story_fade_out // popExit
        );
        transaction.add(R.id.container, songs, "TopSongsFragmentTag");
        transaction.addToBackStack(null);
        transaction.commit();

        Log.d("MainActivity.java", "onMyButtonThreeClick()");
    }

    @Override
    public void onMyButtonTwoClick(String selectedTerm) {
        selectedTerm1=selectedTerm;
        Collection<User> u = State.getInstance().getUsers();
        for (User user : u) {
            if (user.getUsername().equals(State.getInstance().getCurrentUser())) {
                Wrapped w = user.getSpotifyData();
                try {
                    w.convertPagingToArtist(selectedTerm);
                    w.convertPagingToTrack(selectedTerm);
                } catch (Exception e) {
                    w = new Wrapped();
                }
                user.setSpotifyData(w);
            }
        }
        TopArtists topArtists1 = new TopArtists();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(
                R.anim.story_fade_in, // enter
                R.anim.story_fade_out, // exit
                R.anim.story_fade_in, // popEnter
                R.anim.story_fade_out // popExit
        );
        transaction.add(R.id.container, topArtists1, "TopArtistsFragmentTag");
        transaction.addToBackStack(null);
        transaction.commit();

        Log.d("MainActivity.java", "onMyButtonTwoClick()");
    }

    public void hideBottomActionBar() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setVisibility(View.GONE);
    }

    public void showBottomActionBar() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setVisibility(View.VISIBLE);
    }

    public void updateYearlyOverviewFragment() {
        Log.d("MainActivity.java","it reaches this yearly fragment");
        if (!isFragmentReady || accessTokenFinal == null) {
            Log.d("MainActivity", "Fragment not ready or access token not available.");
            return;
        }
        // Retrieve the current user and their Wrapped data
        User currentUser = State.getInstance().getUsers().stream()
                .filter(u -> u.getUsername().equals(State.getInstance().getCurrentUser()))
                .findFirst()
                .orElse(null);

        if (currentUser == null || currentUser.getSpotifyData() == null) {
            Log.e("MainActivity", "No Wrapped data available for current user.");
            return;
        }

        Wrapped wrappedData = currentUser.getSpotifyData();
        try {
            wrappedData.convertPagingToArtist(selectedTerm1);
            wrappedData.convertPagingToTrack(selectedTerm1);
            Log.d("MainActivity", "Data fetched and converted.");
            String mostCommonGenre = wrappedData.findMostCommonGenre();

            YearlyOverviewFragment fragment = (YearlyOverviewFragment) getSupportFragmentManager().findFragmentByTag("YearlyOverviewFragmentTag");
            if (fragment != null) {
                fragment.updateData(wrappedData.getTopArtists(5), wrappedData.getTopTracks(5), mostCommonGenre);
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Failed to convert data", e);
        }
    }

    public void removeYearlyOverviewFragmentWithAnimation() {
        Fragment yearlyOverviewFragment = getSupportFragmentManager().findFragmentByTag("YearlyOverviewFragmentTag");
        if (yearlyOverviewFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.anim.story_fade_in, // exit
                    R.anim.story_fade_out // popEnter
            );
            transaction.remove(yearlyOverviewFragment);
            transaction.commit();
        }

        Log.d("MainActivity.java", "removeYearlyOverviewFragmentWithAnimation()");
    }

    public void updateSongsFragment() {
        if (!isFragmentReady2 || accessTokenFinal == null) {
            Log.d("MainActivity", "Fragment not ready or access token not available.");
            return;
        }

        // Retrieve the current user and their Wrapped data
        User currentUser = State.getInstance().getUsers().stream()
                .filter(u -> u.getUsername().equals(State.getInstance().getCurrentUser()))
                .findFirst()
                .orElse(null);

        if (currentUser == null || currentUser.getSpotifyData() == null) {
            Log.e("MainActivity", "No Wrapped data available for current user.");
            return;
        }

        Wrapped wrappedData = currentUser.getSpotifyData();
        try {
            wrappedData.convertPagingToTrack(selectedTerm1);
            Log.d("MainActivity", "Data fetched and converted.");
            TopSongs fragment = (TopSongs) getSupportFragmentManager().findFragmentByTag("TopSongsFragmentTag");
            if (fragment != null) {
                fragment.updateSongsData(wrappedData.getTopTracks(5));
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Failed to convert data", e);
        }
    }

    public void removeSongsWithAnimation() {
        Fragment topSongs = getSupportFragmentManager().findFragmentByTag("TopSongsFragmentTag");
        if (topSongs != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.anim.story_fade_in, // exit
                    R.anim.story_fade_out // popEnter
            );
            transaction.remove(topSongs);
            transaction.commit();
        }

        Log.d("MainActivity.java", "removeSongsWithAnimation()");
    }

    public void updateArtistsFragment() {
        if (!isFragmentReady3 || accessTokenFinal == null) {
            Log.d("MainActivity", "Fragment not ready or access token not available.");
            return;
        }

        // Retrieve the current user and their Wrapped data
        User currentUser = State.getInstance().getUsers().stream()
                .filter(u -> u.getUsername().equals(State.getInstance().getCurrentUser()))
                .findFirst()
                .orElse(null);

        if (currentUser == null || currentUser.getSpotifyData() == null) {
            Log.e("MainActivity", "No Wrapped data available for current user.");
            return;
        }

        Wrapped wrappedData = currentUser.getSpotifyData();
        try {
            wrappedData.convertPagingToArtist(selectedTerm1);
            Log.d("MainActivity", "Data fetched and converted.");
            TopArtists fragment = (TopArtists) getSupportFragmentManager().findFragmentByTag("TopArtistsFragmentTag");
            if (fragment != null) {
                fragment.updateArtistData(wrappedData.getTopArtists(3));
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Failed to convert data", e);
        }
    }

    public void removeArtistWithAnimation() {
        Fragment topArtist = getSupportFragmentManager().findFragmentByTag("TopArtistsFragmentTag");
        if (topArtist != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.anim.story_fade_in, // exit
                    R.anim.story_fade_out // popEnter
            );
            transaction.remove(topArtist);
            transaction.commit();
        }

        Log.d("MainActivity.java", "removeSongsWithAnimation()");
    }

    @Override
    protected void onResume() {
        Log.d("MainActivity.java","We are handling the response here");
        super.onResume();
        Uri data = getIntent().getData();
        if (data != null && data.getScheme().equals("myappspotifyauth")) {
            String authorizationCode = data.getQueryParameter("code");
            authCode = authorizationCode;
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String codeVerifier = prefs.getString("code_verifier", null);
            if (codeVerifier != null) {
                new TokenExchangeTask().execute(authorizationCode, codeVerifier);
            } else {
                Log.d("MainActivity.java", "Code verifier is null");
            }
        }

        Log.d("MainActivity.java", "onResume()");
    }


    private class TokenExchangeTask extends AsyncTask<String, Void, TokenExchangeUtil.TokenResponse> {

        public  void getAccessCodefromRefresh(String refreshTokenNew) {
            refreshAccessToken(refreshTokenNew);
        }
        @Override
        protected TokenExchangeUtil.TokenResponse doInBackground(String... params) {
            String authorizationCode = params[0];
            String codeVerifier = params[1];
            // This line is redundant and should be removed since you don't need to set the authorization code like this:
            // TokenExchangeUtil.getSpotifyApi().authorizationCode(authorizationCode);
            return TokenExchangeUtil.exchangeCodeForToken(authorizationCode, codeVerifier);
        }

        @Override
        protected void onPostExecute(TokenExchangeUtil.TokenResponse tokenResponse) {
            if (tokenResponse != null && tokenResponse.accessToken != null && tokenResponse.refreshToken != null) {
                // Log the tokens
                Log.d("MainActivity.java", "Access Token: " + tokenResponse.accessToken);
                Log.d("MainActivity.java", "Refresh Token: " + tokenResponse.refreshToken);
                useAccessToken(tokenResponse.accessToken);

                // Save the tokens
                saveTokens(tokenResponse.accessToken, tokenResponse.refreshToken,State.getInstance().getCurrentUser());
                Log.d("MainActivity.java","exited saving");
//                if (isLoginActivitySource) {
//                    Log.d("MainActivity.java","This is working :")
//
//                }
                // Decide which token to use based on the source activity
//                String sourceActivity = getIntent().getStringExtra("source");
//                if ("LoginActivity".equals(sourceActivity)) {
//                    Log.d("MainActivity.java","source activity ->login");
//                    // Refresh the access token using the refresh token if coming from LoginActivity
//                    refreshAccessToken(tokenResponse.refreshToken);
//                } else if ("SignUpActivity".equals(sourceActivity)) {
//                    Log.d("MainActivity.java","source activity ->signup");
//
//                    // Use the access token directly if coming from SignUpActivity
                useAccessToken(tokenResponse.accessToken);
//                }
            } else {
                Log.d("MainActivity.java", "Token Response is null or incomplete");
            }
        }

        private void saveTokens(String accessToken, String refreshToken, String userName) {
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("accessToken", accessToken);
            editor.putString(userName + "_refreshToken", refreshToken);
            editor.apply();
            Log.d("MainActivity.java", "Tokens saved to SharedPreferences.");
        }



        private void useAccessToken(String accessToken) {
            accessTokenFinal = accessToken;
            Log.d("MainActivity.java", "Using Access Token: " + accessToken);
            TokenExchangeUtil.getSpotifyApi().setAccessToken(accessTokenFinal);
            Log.d("MainActivity.java","Line");


            // Check if fragments are ready to be updated
            if (isFragmentReady) {
                Log.d("MainActivity","did we reach here?, yes");
                updateYearlyOverviewFragment();
            }
            if (isFragmentReady2) {
                updateSongsFragment();
            }
            if (isFragmentReady3) {
                updateArtistsFragment();
            }
        }
    }

}