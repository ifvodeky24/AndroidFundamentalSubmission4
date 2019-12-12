package com.idw.project.cataloguemovie.config;

import com.idw.project.cataloguemovie.BuildConfig;

public class Config {
    public static final String API_KEY = BuildConfig.API_KEY;
    public static final String ENDPOINT = "https://api.themoviedb.org/3/";
    public static final String EN_LANGUAGE = "en-US";
    private static final String IMAGE_ENDPOINT = "https://image.tmdb.org/t/p/";
    public static final String IMAGE_W185 = IMAGE_ENDPOINT+"w185/";
    public static final String IMAGE_W92 = IMAGE_ENDPOINT+"w92/";
    public static final String IMAGE_W154 = IMAGE_ENDPOINT+"w154/";
    public static final String IMAGE_W342 = IMAGE_ENDPOINT+"w342/";
    public static final String IMAGE_W500 = IMAGE_ENDPOINT+"w500/";
    public static final String IMAGE_W780 = IMAGE_ENDPOINT+"w780/";
    public static final String IMAGE_ORIGINAL = IMAGE_ENDPOINT+"original/";
    public static final String VIDEO_URL = "https://www.youtube.com/embed/";

}
