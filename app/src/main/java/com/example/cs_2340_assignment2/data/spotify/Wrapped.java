package com.example.cs_2340_assignment2.data.spotify;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Giant wrapper class for Spotify wrapped objects based on the Spotify API.
 */
public class Wrapped implements Serializable {
    public Collection<Song> wrapped;

    /**
     * Default constructor.
     */
    public Wrapped() {
        this(new ArrayList<>());
    }

    /**
     * Constructor with a varargs of songs.
     *
     * @param song varargs of songs
     */
    public Wrapped(Song... song) {
        this.wrapped = new ArrayList<>();
        Collections.addAll(wrapped, song);
    }

    /**
     * Constructor with a collection of songs.
     *
     * @param wrapped collection of songs
     */
    public Wrapped(Collection<Song> wrapped) {
        this.wrapped = wrapped;
    }

    /**
     * Get the collection of songs.
     *
     * @return collection of songs
     */
    public Collection<Song> getWrapped() {
        return wrapped;
    }

    /**
     * Set the collection of songs.
     *
     * @param wrapped collection of songs
     */
    public void setWrapped(Collection<Song> wrapped) {
        this.wrapped = wrapped;
    }

    /**
     * Base Song class.
     */
    public static class Song implements Serializable {
        private String title;
        private List<String> artists;
        private Long duration;
        private String genre;
        private GeoPoint mostPopularLocation;
        private Long numberOfSecondsListened;
        private Map<String, Long> monthlyFrequencyOfListens;

        /**
         * Default constructor.
         *
         * @param title                    title of the song
         * @param artists                  list of artists
         * @param duration                 duration of the song
         * @param genre                    genre of the song
         * @param mostPopularLocation      most popular location of the song
         * @param numberOfMinutesListened  number of minutes listened
         * @param monthlyFrequencyOfListen monthly frequency of listens
         */
        public Song(String title, List<String> artists, Long duration, String genre, GeoPoint mostPopularLocation, Long numberOfMinutesListened, Map<String, Long> monthlyFrequencyOfListen) {
            this.title = title;
            this.artists = artists;
            this.duration = duration;
            this.genre = genre;
            this.mostPopularLocation = mostPopularLocation;
            this.numberOfSecondsListened = numberOfMinutesListened;
            this.monthlyFrequencyOfListens = monthlyFrequencyOfListen;
        }

        /**
         * Get the title of the song.
         *
         * @return title of the song
         */
        public String getTitle() {
            return title;
        }

        /**
         * Set the title of the song.
         *
         * @param title title of the song
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * Get the list of artists.
         *
         * @return list of artists
         */
        public List<String> getArtists() {
            return artists;
        }

        /**
         * Set the list of artists.
         *
         * @param artists list of artists
         */
        public void setArtists(List<String> artists) {
            this.artists = artists;
        }

        /**
         * Get the duration of the song.
         *
         * @return duration of the song
         */
        public double getDuration() {
            return duration;
        }

        /**
         * Set the duration of the song.
         *
         * @param duration duration of the song
         */
        public void setDuration(Long duration) {
            this.duration = duration;
        }

        /**
         * Get the genre of the song.
         *
         * @return genre of the song
         */
        public String getGenre() {
            return genre;
        }

        /**
         * Set the genre of the song.
         *
         * @param genre genre of the song
         */
        public void setGenre(String genre) {
            this.genre = genre;
        }

        /**
         * Get the most popular location of the song.
         *
         * @return most popular location of the song
         */
        public GeoPoint getMostPopularLocation() {
            return mostPopularLocation;
        }

        /**
         * Set the most popular location of the song.
         *
         * @param mostPopularLocation most popular location of the song
         */
        public void setMostPopularLocation(GeoPoint mostPopularLocation) {
            this.mostPopularLocation = mostPopularLocation;
        }

        /**
         * Get the number of seconds listened.
         *
         * @return number of seconds listened
         */
        public double getNumberOfSecondsListened() {
            return numberOfSecondsListened;
        }

        /**
         * Set the number of seconds listened.
         *
         * @param numberOfSecondsListened number of seconds listened
         */
        public void setNumberOfSecondsListened(Long numberOfSecondsListened) {
            this.numberOfSecondsListened = numberOfSecondsListened;
        }

        /**
         * Get the monthly frequency of listens.
         *
         * @return monthly frequency of listens
         */
        public Map<String, Long> getMonthlyFrequencyOfListens() {
            return monthlyFrequencyOfListens;
        }

        /**
         * Set the monthly frequency of listens.
         *
         * @param monthlyFrequencyOfListens monthly frequency of listens
         */
        public void setMonthlyFrequencyOfListens(Map<String, Long> monthlyFrequencyOfListens) {
            this.monthlyFrequencyOfListens = monthlyFrequencyOfListens;
        }

        /**
         * Equals method for Song.
         *
         * @param o object to compare
         * @return true if equal, false otherwise
         */
        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Song song = (Song) o;
            return title.equals(song.title) &&
                    artists.equals(song.artists) &&
                    duration.equals(song.duration) &&
                    genre.equals(song.genre) &&
                    mostPopularLocation.equals(song.mostPopularLocation) &&
                    numberOfSecondsListened.equals(song.numberOfSecondsListened) &&
                    monthlyFrequencyOfListens.equals(song.monthlyFrequencyOfListens);
        }
    }

    /**
     * Equals method for Wrapped.
     *
     * @param obj object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Wrapped wrapped = (Wrapped) obj;
        return this.wrapped.equals(wrapped.wrapped);
    }
}
