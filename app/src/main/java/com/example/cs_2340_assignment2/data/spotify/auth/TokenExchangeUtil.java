package com.example.cs_2340_assignment2.data.spotify.auth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import se.michaelthelin.spotify.SpotifyApi;

public class TokenExchangeUtil {
    private static final String CLIENT_ID = "3f6e08b6307b478398c6811e6d85a012";
    private static final String REDIRECT_URI = "myappspotifyauth://callback";
    private static final String TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static final String CLIENT_SECRET = "085867c765a6474c8d8e3e2c55411d48";
    private static final SpotifyApi spotifyApi;


    static {
        try {
            spotifyApi = new SpotifyApi.Builder()
                    .setClientId(CLIENT_ID)
                    .setClientSecret(CLIENT_SECRET)
                    .setRedirectUri(new URI(REDIRECT_URI))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
//    private static final AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode()
//            .build();

    public static TokenResponse exchangeCodeForToken(String authorizationCode, String codeVerifier) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(TOKEN_URL).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String data = "client_id=" + CLIENT_ID +
                    "&grant_type=authorization_code" +
                    "&code=" + authorizationCode +
                    "&redirect_uri=" + REDIRECT_URI +
                    "&code_verifier=" + codeVerifier;

            try (OutputStream output = conn.getOutputStream()) {
                output.write(data.getBytes(StandardCharsets.UTF_8));
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            JSONObject jsonResponse = new JSONObject(response.toString());
            String accessToken = jsonResponse.getString("access_token");
            String refreshToken = jsonResponse.getString("refresh_token");  // Extract the refresh token

            return new TokenResponse(accessToken, refreshToken);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle errors appropriately
        }

    }

    public static String refreshAccessToken(String refreshToken) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(TOKEN_URL).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String data = "client_id=" + CLIENT_ID +
                    "&grant_type=refresh_token" +
                    "&refresh_token=" + refreshToken +
                    "&client_secret=" + CLIENT_SECRET;

            try (OutputStream output = conn.getOutputStream()) {
                output.write(data.getBytes(StandardCharsets.UTF_8));
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            JSONObject jsonResponse = new JSONObject(response.toString());
            return jsonResponse.getString("access_token");
        } catch (IOException e) {
            System.err.println("Error connecting to Spotify: " + e.getMessage());
            return null;
        } catch (JSONException e) {
            System.err.println("Error parsing JSON from Spotify: " + e.getMessage());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Consider more sophisticated error handling
        }
    }
    public static class TokenResponse {
        public final String accessToken;
        public final String refreshToken;

        public TokenResponse(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }


    public static SpotifyApi getSpotifyApi() {
        return spotifyApi;
    }
}

