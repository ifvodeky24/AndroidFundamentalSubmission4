package com.idw.project.cataloguemovie.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.idw.project.cataloguemovie.model.Movie;
import com.idw.project.cataloguemovie.model.TvShow;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.idw.project.cataloguemovie.db.DatabaseContract.FavoriteColumn.AVERAGE;
import static com.idw.project.cataloguemovie.db.DatabaseContract.FavoriteColumn.OVERVIEW;
import static com.idw.project.cataloguemovie.db.DatabaseContract.FavoriteColumn.POSTER_PATH;
import static com.idw.project.cataloguemovie.db.DatabaseContract.FavoriteColumn.RELEASE_DATE;
import static com.idw.project.cataloguemovie.db.DatabaseContract.FavoriteColumn.TABLE_FAVORITE;
import static com.idw.project.cataloguemovie.db.DatabaseContract.FavoriteColumn.TABLE_FAVORITE_TVSHOW;
import static com.idw.project.cataloguemovie.db.DatabaseContract.FavoriteColumn.TITLE;

public class FavoriteHelper {
    private static final String DATABASE_TABLE = TABLE_FAVORITE;
    private static final String DATABASE_TABLE_TVSHOW = TABLE_FAVORITE_TVSHOW;
    private static DatabaseHelper dataBaseHelper;
    private static FavoriteHelper INSTANCE;

    private static SQLiteDatabase database;

    public FavoriteHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static FavoriteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavoriteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();

        if (database.isOpen())
            database.close();
    }

    public boolean Check(int id){
        Cursor cursor = null;
        String sql = "SELECT "+_ID+" FROM "+TABLE_FAVORITE+" WHERE "+_ID +"="+id;
        cursor = database.rawQuery(sql, null);
        System.out.println("cek sini"+cursor.getCount());

        if (cursor.getCount()>0){
            return true;
        }else {

        }
        cursor.close();
        return false;
    }

    public boolean CheckTvShow(int id){
        Cursor cursor = null;
        String sql = "SELECT "+_ID+" FROM "+TABLE_FAVORITE_TVSHOW+" WHERE "+_ID +"="+id;
        cursor = database.rawQuery(sql, null);
        System.out.println("cek sini"+cursor.getCount());

        if (cursor.getCount()>0){
            return true;
        }else {

        }
        cursor.close();
        return false;
    }


    /**
     * Gunakan method ini untuk ambil semua note yang ada
     * Otomatis di parsing ke dalam model Note
     *
     * @return hasil getAllNotes berbentuk array model note
     */
    public ArrayList<Movie> getAllMovies() {
        ArrayList<Movie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        Movie movie;
        if (cursor.getCount() > 0) {
            do {
                movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(AVERAGE)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));

                arrayList.add(movie);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    /**
     * Gunakan method ini untuk ambil semua note yang ada
     * Otomatis di parsing ke dalam model Note
     *
     * @return hasil getAllNotes berbentuk array model note
     */
    public ArrayList<TvShow> getAllTvShow() {
        ArrayList<TvShow> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE_TVSHOW, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        TvShow tvShow;
        if (cursor.getCount() > 0) {
            do {
                tvShow = new TvShow();
                tvShow.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                tvShow.setOriginalName(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                tvShow.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                tvShow.setFirstAirDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                tvShow.setVoteAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(AVERAGE)));
                tvShow.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));

                arrayList.add(tvShow);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    /**
     * Gunakan method ini untuk getAllNotes insertNote
     *
     * @param movie model note yang akan dimasukkan
     * @return id dari data yang baru saja dimasukkan
     */
    public long insertMovie(Movie movie) {
        ContentValues args = new ContentValues();
        args.put(_ID, movie.getId());
        args.put(TITLE, movie.getTitle());
        args.put(OVERVIEW, movie.getOverview());
        args.put(RELEASE_DATE, movie.getReleaseDate());
        args.put(AVERAGE, movie.getVoteAverage());
        args.put(POSTER_PATH, movie.getPosterPath());
        return database.insert(DATABASE_TABLE, null, args);
    }

    /**
     * Gunakan method ini untuk getAllNotes insertNote
     *
     * @param tvShow model note yang akan dimasukkan
     * @return id dari data yang baru saja dimasukkan
     */
    public long insertTvShow(TvShow tvShow) {
        ContentValues args = new ContentValues();
        args.put(_ID, tvShow.getId());
        args.put(TITLE, tvShow.getOriginalName());
        args.put(OVERVIEW, tvShow.getOverview());
        args.put(RELEASE_DATE, tvShow.getFirstAirDate());
        args.put(AVERAGE, tvShow.getVoteAverage());
        args.put(POSTER_PATH, tvShow.getPosterPath());
        return database.insert(DATABASE_TABLE_TVSHOW, null, args);
    }

    /**
     * Gunakan method ini untuk getAllNotes updateNote
     *
     * @param movie model note yang akan diubah
     * @return int jumlah dari row yang ter-updateNote, jika tidak ada yang diupdate maka nilainya 0
     */
    public int updateMovie(Movie movie) {
        ContentValues args = new ContentValues();
        args.put(_ID, movie.getId());
        args.put(TITLE, movie.getTitle());
        args.put(OVERVIEW, movie.getOverview());
        args.put(RELEASE_DATE, movie.getReleaseDate());
        args.put(AVERAGE, movie.getVoteAverage());
        args.put(POSTER_PATH, movie.getPosterPath());
        return database.update(DATABASE_TABLE, args, _ID + "= '" + movie.getId() + "'", null);
    }

    /**
     * Gunakan method ini untuk getAllNotes updateNote
     *
     * @param tvShow model note yang akan diubah
     * @return int jumlah dari row yang ter-updateNote, jika tidak ada yang diupdate maka nilainya 0
     */
    public int updateTvShow(TvShow tvShow) {
        ContentValues args = new ContentValues();
        args.put(_ID, tvShow.getId());
        args.put(TITLE, tvShow.getOriginalName());
        args.put(OVERVIEW, tvShow.getOverview());
        args.put(RELEASE_DATE, tvShow.getFirstAirDate());
        args.put(AVERAGE, tvShow.getVoteAverage());
        args.put(POSTER_PATH, tvShow.getPosterPath());
        return database.update(DATABASE_TABLE_TVSHOW, args, _ID + "= '" + tvShow.getId() + "'", null);
    }

    /**
     * Gunakan method ini untuk getAllNotes deleteNote
     *
     * @param id id yang akan di deleteNote
     * @return int jumlah row yang di deleteNote
     */
    public int deleteMovie(int id) {
        return database.delete(TABLE_FAVORITE, _ID + " = '" + id + "'", null);
    }

    /**
     * Gunakan method ini untuk getAllNotes deleteNote
     *
     * @param id id yang akan di deleteNote
     * @return int jumlah row yang di deleteNote
     */
    public int deleteTvShow(int id) {
        return database.delete(TABLE_FAVORITE_TVSHOW, _ID + " = '" + id + "'", null);
    }



}
