package com.idw.project.cataloguemovie.db;

import android.provider.BaseColumns;

public class DatabaseContract {

    public static final class FavoriteColumn implements BaseColumns {
        public static String TABLE_FAVORITE = "favorite";
        public static String TABLE_FAVORITE_TVSHOW = "favorite_tvshow";
        public static String TITLE = "title";
        public static String RELEASE_DATE = "release_date";
        public static String OVERVIEW = "overview";
        public static String AVERAGE = "average";
        public static String POSTER_PATH = "poster_path";
    }
}
