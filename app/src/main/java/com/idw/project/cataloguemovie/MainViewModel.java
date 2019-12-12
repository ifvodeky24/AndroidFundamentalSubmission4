package com.idw.project.cataloguemovie;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.idw.project.cataloguemovie.config.Config;
import com.idw.project.cataloguemovie.model.Movie;
import com.idw.project.cataloguemovie.model.MovieVideos;
import com.idw.project.cataloguemovie.model.TvShow;
import com.idw.project.cataloguemovie.model.TvShowVideos;
import com.idw.project.cataloguemovie.response.ResponseMovie;
import com.idw.project.cataloguemovie.response.ResponseMovieDetail;
import com.idw.project.cataloguemovie.response.ResponseMovieVideos;
import com.idw.project.cataloguemovie.response.ResponseTvShow;
import com.idw.project.cataloguemovie.response.ResponseTvShowDetail;
import com.idw.project.cataloguemovie.response.ResponseTvShowVideos;
import com.idw.project.cataloguemovie.rest.ApiClient;
import com.idw.project.cataloguemovie.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    private ArrayList<Movie> MovieArrayList = new ArrayList<>();
    private ArrayList<TvShow> TvArrayList = new ArrayList<>();


    ApiInterface apiService;

    private static final String API_KEY= Config.API_KEY;
    public static final String URL_VIDEO = Config.VIDEO_URL;

    //bagian movie
    private MutableLiveData<ArrayList<Movie>> listMovie = new MutableLiveData<>();
    public void setListMovie(){
        apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService.getMovie(API_KEY, Config.EN_LANGUAGE).enqueue(new Callback<ResponseMovie>() {
            @Override
            public void onResponse(Call<ResponseMovie> call, Response<ResponseMovie> response) {
                System.out.println("responseIn"+response);
                if (response.isSuccessful()){
                    System.out.println("responseSize"+response.body().getResults());

                    if (response.body().getTotalResults() > 0){
                        MovieArrayList.addAll(response.body().getResults());
                        System.out.println(response.body().getResults().get(0).getId());
                        listMovie.postValue(MovieArrayList);

                    }else {
                        Log.d("response", String.valueOf(response));
                    }

                }else{
                    Log.d("response", String.valueOf(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseMovie> call, Throwable t) {
                t.printStackTrace();
                Log.e("error", t.getMessage());
            }
        });
    }

    public LiveData<ArrayList<Movie>> getListMovies() {
        setListMovie();
        return listMovie;
    }

    //bagian tv
    private MutableLiveData<ArrayList<TvShow>> listTv = new MutableLiveData<>();
    public void setListTv(){
        apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService.getTv(API_KEY, Config.EN_LANGUAGE).enqueue(new Callback<ResponseTvShow>() {
            @Override
            public void onResponse(Call<ResponseTvShow> call, Response<ResponseTvShow> response) {
                System.out.println("responseIn"+response);
                if (response.isSuccessful()){
                    System.out.println("responseSize"+response.body().getResults());

                    if (response.body().getTotalResults() > 0){
                        TvArrayList.addAll(response.body().getResults());
                        System.out.println(response.body().getResults().get(0).getId());
                        listTv.postValue(TvArrayList);

                    }else {
                        Log.d("response", String.valueOf(response));
                    }

                }else{
                    Log.d("response", String.valueOf(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseTvShow> call, Throwable t) {
                t.printStackTrace();
                Log.e("error", t.getMessage());
            }
        });
    }

    public LiveData<ArrayList<TvShow>> getListTv() {
        setListTv();
        return listTv;
    }

    //bagian movie video

    private MutableLiveData<String> StringMovieVideo = new MutableLiveData<>();
    public void setListMovieVideo(final String id){
        System.out.println("cekId"+ id);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService.getMovieVideos(Integer.parseInt(id), API_KEY).enqueue(new Callback<ResponseMovieVideos>() {
            @Override
            public void onResponse(Call<ResponseMovieVideos> call, Response<ResponseMovieVideos> response) {
                System.out.println("responseIn"+response);
                if (response.isSuccessful()){
                    System.out.println("responseSize"+response.body().getResults());

                    if (response.body().getResults().size()>0){
                        System.out.println("testing"+ id);
                        List<MovieVideos> movieVideos = response.body().getResults();
                        String key = movieVideos.get(0).getKey();
                        System.out.println("cek_key"+key);

                        String path = URL_VIDEO+key;
                        System.out.println("cekPath"+ path);

                        StringMovieVideo.postValue(path);

                    }else {
                        Log.d("response", String.valueOf(response));
                        System.out.println("video tidak ada");

                        String path = null;

                        StringMovieVideo.postValue(path);
                    }

                }else{
                    Log.d("response", String.valueOf(response));
                }
            }
            @Override
            public void onFailure(Call<ResponseMovieVideos> call, Throwable t) {
                t.printStackTrace();
                Log.e("error", t.getMessage());
            }
        });
    }

    public LiveData<String> getListMovieVideos(String id) {
        setListMovieVideo(id);
        return StringMovieVideo;
    }

    //bagian tv video

    private MutableLiveData<String> StringTvVideo = new MutableLiveData<>();
    public void setListTvVideo(final String id){
        System.out.println("cekId"+ id);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService.getTvVideos(Integer.parseInt(id), API_KEY).enqueue(new Callback<ResponseTvShowVideos>() {
            @Override
            public void onResponse(Call<ResponseTvShowVideos> call, Response<ResponseTvShowVideos> response) {
                System.out.println("responseIn"+response);
                if (response.isSuccessful()){
                    System.out.println("responseSize"+response.body().getResults());

                    if (response.body().getResults().size()>0){
                        System.out.println("testing"+ id);
                        List<TvShowVideos> tvVideos = response.body().getResults();
                        String key = tvVideos.get(0).getKey();
                        System.out.println("cek_key"+key);

                        String path = URL_VIDEO+key;
                        System.out.println("cekPath"+ path);

                        StringTvVideo.postValue(path);

                    }else {
                        Log.d("response", String.valueOf(response));
                        System.out.println("video tidak ada");

                        String path = null;

                        StringTvVideo.postValue(path);
                    }

                }else{
                    Log.d("response", String.valueOf(response));
                }
            }
            @Override
            public void onFailure(Call<ResponseTvShowVideos> call, Throwable t) {
                t.printStackTrace();
                Log.e("error", t.getMessage());
            }
        });
    }

    public LiveData<String> getListTvVideos(String id) {
        setListTvVideo(id);
        return StringTvVideo;
    }

    //bagian detail movie
    private MutableLiveData<ResponseMovieDetail> detailMovie = new MutableLiveData<>();

    public void setDetailMovie(final String id){

        apiService = ApiClient.getClient().create(ApiInterface.class);

        apiService.getMovieDetails(Integer.parseInt(id), API_KEY, Config.EN_LANGUAGE).enqueue(new Callback<ResponseMovieDetail>() {
            @Override
            public void onResponse(Call<ResponseMovieDetail> call, Response<ResponseMovieDetail> response) {
                System.out.println("responseIn"+response);
                if (response.isSuccessful()){

                    detailMovie.postValue(response.body());
                    System.out.printf("id nya merupakan"+ response.body().getId());

                }else{
                    Log.d("response", String.valueOf(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseMovieDetail> call, Throwable t) {
                t.printStackTrace();
                Log.e("error", t.getMessage());
            }
        });
    }

    public LiveData<ResponseMovieDetail> getDetailMovie(String id) {
        setDetailMovie(id);
        return detailMovie;
    }

    //bagian detail tvshow
    private MutableLiveData<ResponseTvShowDetail> detailTv = new MutableLiveData<>();

    public void setDetailTv(final String id){

        apiService = ApiClient.getClient().create(ApiInterface.class);

        apiService.getTvDetails(Integer.parseInt(id), API_KEY, Config.EN_LANGUAGE).enqueue(new Callback<ResponseTvShowDetail>() {
            @Override
            public void onResponse(Call<ResponseTvShowDetail> call, Response<ResponseTvShowDetail> response) {
                System.out.println("responseIn"+response);
                if (response.isSuccessful()){

                    detailTv.postValue(response.body());
                    System.out.printf("id nya merupakan"+ response.body().getId());

                }else{
                    Log.d("response", String.valueOf(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseTvShowDetail> call, Throwable t) {
                t.printStackTrace();
                Log.e("error", t.getMessage());
            }
        });
    }

    public LiveData<ResponseTvShowDetail> getDetailTv(String id) {
        setDetailTv(id);
        return detailTv;
    }


}
