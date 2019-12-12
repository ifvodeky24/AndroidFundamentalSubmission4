package com.idw.project.cataloguemovie.rest;

import com.idw.project.cataloguemovie.response.ResponseMovie;
import com.idw.project.cataloguemovie.response.ResponseMovieDetail;
import com.idw.project.cataloguemovie.response.ResponseMovieVideos;
import com.idw.project.cataloguemovie.response.ResponseTvShowDetail;
import com.idw.project.cataloguemovie.response.ResponseTvShow;
import com.idw.project.cataloguemovie.response.ResponseTvShowVideos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("movie/now_playing")
    Call<ResponseMovie> getMovie(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("tv/on_the_air")
    Call<ResponseTvShow> getTv(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{movie_id}/videos")
    Call<ResponseMovieVideos> getMovieVideos(
            @Path("movie_id") int id,
            @Query("api_key") String apiKey
    );

    @GET("tv/{tv_id}/videos")
    Call<ResponseTvShowVideos> getTvVideos(
            @Path("tv_id") int id,
            @Query("api_key") String apiKey
    );

    @GET("movie/{movie_id}?append_to_response=credits,reviews")
    Call<ResponseMovieDetail> getMovieDetails(
            @Path("movie_id") int id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("tv/{tv_id}?append_to_response=credits,reviews")
    Call<ResponseTvShowDetail> getTvDetails(
            @Path("tv_id") int id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );
}
