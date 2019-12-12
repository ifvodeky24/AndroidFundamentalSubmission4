package com.idw.project.cataloguemovie;

import com.idw.project.cataloguemovie.model.Movie;

import java.util.ArrayList;

public interface LoadFavoriteMovieCallback {
    void preExecute();

    void postExecute(ArrayList<Movie> movies);
}
