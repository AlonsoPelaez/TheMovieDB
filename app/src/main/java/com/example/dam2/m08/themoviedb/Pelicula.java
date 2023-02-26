package com.example.dam2.m08.themoviedb;

import android.os.Parcel;
import android.os.Parcelable;


public class Pelicula  implements Parcelable{


    private int id;
    private String title;
    private String overview;
    private int vote_average;
    private String poster_path;
    private String release_date;

    public Pelicula(int id, String title, String overview, int vote_average, String poster_path, String release_date) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.vote_average = vote_average;
        this.poster_path = poster_path;
        this.release_date = release_date;
    }

    public Pelicula() {
    }

    protected Pelicula(Parcel in) {
        id = in.readInt();
        title = in.readString();
        overview = in.readString();
        vote_average = in.readInt();
        poster_path = in.readString();
        release_date = in.readString();
    }

    public static final Creator<Pelicula> CREATOR = new Creator<Pelicula>() {
        @Override
        public Pelicula createFromParcel(Parcel in) {
            return new Pelicula(in);
        }

        @Override
        public Pelicula[] newArray(int size) {
            return new Pelicula[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public int getVote_average() {
        return vote_average;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    @Override
    public String toString() {
        return "Pelicula{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", vote_average=" + vote_average +
                ", poster_path='" + poster_path + '\'' +
                ", release_date='" + release_date + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeInt(vote_average);
        parcel.writeString(poster_path);
        parcel.writeString(release_date);
    }
}