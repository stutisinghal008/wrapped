package com.example.cs_2340_assignment2.data.spotify;

import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;
import com.example.cs_2340_assignment2.data.spotify.auth.TokenExchangeUtil;

import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;

public class Wrapped {
    private Collection<Track> tracks = new ArrayList<>();
    private Collection<Artist> artists = new ArrayList<>();
    public Wrapped() {
        this.artists = new ArrayList<>();
        this.tracks = new ArrayList<>();
    }

    public Wrapped(Collection<Track> tracks, Collection<Artist> artists) {
        this.tracks = tracks;
        this.artists = artists;
    }

    public Collection<Track> getTracks() {
        return tracks;
    }

    public void setTracks(Collection<Track> tracks) {
        this.tracks = tracks;
    }

    public Collection<Artist> getArtists() {
        return artists;
    }

    public void setArtists(Collection<Artist> artists) {
        this.artists = artists;
    }

    public List<Artist> getTopArtists(int limit) {
        return artists.stream().limit(limit).collect(Collectors.toList());
    }

    public List<Track> getTopTracks(int limit) {
        return tracks.stream().limit(limit).collect(Collectors.toList());
    }

    public String findMostCommonGenre() {
        List<Artist> topArtists = getTopArtists(5);
        Map<String, Integer> genreCount = new HashMap<>();

        for (Artist artist : topArtists) {
            for (String genre : artist.getGenres()) {
                genreCount.put(genre, genreCount.getOrDefault(genre, 0) + 1);
            }
        }

        String mostCommonGenre = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : genreCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostCommonGenre = entry.getKey();
            }
        }

        return mostCommonGenre;
    }


    public void convertPagingToArtist(String selectedTerm1) throws IOException, ParseException, SpotifyWebApiException {
        final CompletableFuture<Paging<se.michaelthelin.spotify.model_objects.specification.Artist>> pagingFuture = TokenExchangeUtil.getSpotifyApi().getUsersTopArtists().time_range(selectedTerm1).build().executeAsync();
        Paging<se.michaelthelin.spotify.model_objects.specification.Artist> artistPaging = pagingFuture.join();
        se.michaelthelin.spotify.model_objects.specification.Artist[] itemArray = artistPaging.getItems();
        artists.clear();
        for (var item : itemArray) {
            Artist artist = new Artist(item.getName(), Arrays.stream(item.getGenres()).toList(), item.getUri());
            artists.add(artist);
        }
    }

    public void convertPagingToTrack(String selectedTerm1) throws IOException, ParseException, SpotifyWebApiException {
        final CompletableFuture<Paging<se.michaelthelin.spotify.model_objects.specification.Track>> pagingFuture = TokenExchangeUtil.getSpotifyApi().getUsersTopTracks().time_range(selectedTerm1).build().executeAsync();
        Paging<se.michaelthelin.spotify.model_objects.specification.Track> trackPaging = pagingFuture.join();
        se.michaelthelin.spotify.model_objects.specification.Track[] itemArray = trackPaging.getItems();
        tracks.clear();
        for (var item : itemArray) {
            var artists = Arrays.stream(item.getArtists()).toList();
            List<String> artistName = new ArrayList<>();
            for (var a : artists) {
                artistName.add(a.getName());
            }
            Track track = new Track(item.getName(), artistName, item.getAlbum().getName(), item.getPopularity(), item.getUri());
            tracks.add(track);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Artists: {");
        for (Artist artist : artists) {
            sb.append("{");
            sb.append(artist.getName());
            sb.append(", ");
            sb.append(artist.getGenres());
            sb.append("}");
            sb.append(", ");
        }
        sb.append("}\nTracks: {");
        for (Track track : tracks) {
            sb.append(track.getTitle());
            sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }


    public static class Track {
        public Track() {
        }
        public Track(String title, Collection<String> artists, String album_name, long popularity, String uri) {
            this.title = title;
            this.artists = artists;
            this.album_name = album_name;
            this.popularity = popularity;
            this.uri = uri;
        }

        private String title;
        private Collection<String> artists;
        private String album_name;
        private long popularity;
        private String uri;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Collection<String> getArtists() {
            return artists;
        }

        public void setArtists(Collection<String> artists) {
            this.artists = artists;
        }

        public String getAlbum_name() {
            return album_name;
        }

        public void setAlbum_name(String album_name) {
            this.album_name = album_name;
        }

        public long getPopularity() {
            return popularity;
        }

        public void setPopularity(long popularity) {
            this.popularity = popularity;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }

    public static class Artist {
        private String name;
        private Collection<String> genres;
        private String uri;

        public Artist() {

        }

        public Artist(String name, Collection<String> genres, String uri) {
            this.name = name;
            this.genres = genres;
            this.uri = uri;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Collection<String> getGenres() {
            return genres;
        }

        public void setGenres(Collection<String> genres) {
            this.genres = genres;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }
}