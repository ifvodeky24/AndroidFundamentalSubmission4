package com.idw.project.cataloguemovie;

import com.idw.project.cataloguemovie.model.TvShow;

import java.util.ArrayList;

public interface LoadFavoriteTvShowCallback {
    void preExecute();

    void postExecute(ArrayList<TvShow> tvShows);
}
