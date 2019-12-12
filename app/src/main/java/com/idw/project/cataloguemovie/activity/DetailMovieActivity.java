package com.idw.project.cataloguemovie.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.material.snackbar.Snackbar;
import com.idw.project.cataloguemovie.MainViewModel;
import com.idw.project.cataloguemovie.R;
import com.idw.project.cataloguemovie.config.Config;
import com.idw.project.cataloguemovie.db.FavoriteHelper;
import com.idw.project.cataloguemovie.model.Movie;
import com.idw.project.cataloguemovie.response.ResponseMovieDetail;
import com.idw.project.cataloguemovie.rest.ApiClient;
import com.idw.project.cataloguemovie.rest.ApiInterface;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.BlurTransformation;


public class DetailMovieActivity extends AppCompatActivity {

    public static final String TAG = "movie";

    Integer id;

    ApiInterface apiService;

    ImageView iv_poster_movie, iv_poster_movie_front;
    TextView tv_title_movie_detail, tv_rb_movie, tv_genre, tv_release_date, tv_description;
    RatingBar rb_movie;

    private ProgressBar progressBar;
    private ConstraintLayout cl_1;

    private MainViewModel mainViewModel, detailViewModel;

    WebView webView;

    Movie movie;

    MaterialFavoriteButton materialFavoriteButton;

    private FavoriteHelper favoriteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        setTitle("Detail Movie");

        apiService = ApiClient.getClient().create(ApiInterface.class);

        iv_poster_movie = findViewById(R.id.iv_poster_movie);
        iv_poster_movie_front = findViewById(R.id.iv_poster_movie_front);
        tv_title_movie_detail = findViewById(R.id.tv_title_movie_detail);
        tv_genre = findViewById(R.id.tv_genre);
        tv_release_date = findViewById(R.id.tv_release_date);
        tv_description = findViewById(R.id.tv_description);
        tv_rb_movie = findViewById(R.id.tv_rb_movie);
        rb_movie = findViewById(R.id.rb_movie);
        webView = findViewById(R.id.mWebView);
        materialFavoriteButton = findViewById(R.id.fab);
        progressBar = findViewById(R.id.progressbar);
        cl_1 =  findViewById(R.id.cl_1);

        progressBar.setVisibility(View.VISIBLE);
        cl_1.setVisibility(View.GONE);

        movie = getIntent().getExtras().getParcelable(TAG);
        //get Id
        id = movie.getId();

        favoriteHelper = new FavoriteHelper(this);
        favoriteHelper.open();
        if (favoriteHelper.Check(id)){
            materialFavoriteButton.setFavorite(true);
            materialFavoriteButton.setAnimateFavorite(false);
        }else {
            materialFavoriteButton.setFavorite(false);
            materialFavoriteButton.setAnimateFavorite(true);
        }
        favoriteHelper.close();

        materialFavoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                if (favorite){
                    saveFavorite();

                    Snackbar.make(buttonView, "Ditambahkan ke Favorit", Snackbar.LENGTH_LONG).show();
                }else {
                    favoriteHelper = new FavoriteHelper(DetailMovieActivity.this);

                    favoriteHelper.open();
                    favoriteHelper.deleteMovie(id);
                    favoriteHelper.close();

                    Snackbar.make(buttonView, "Dihapus dari Favorit", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getListMovieVideos(String.valueOf(id)).observe(this, getListMovieVideo);

        detailViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        detailViewModel.getDetailMovie(String.valueOf(id)).observe(this, getDetailMovie);

    }

    private final Observer<ResponseMovieDetail> getDetailMovie = new Observer<ResponseMovieDetail>() {
        @Override
        public void onChanged(ResponseMovieDetail responseMovieDetail) {
            if (responseMovieDetail != null) {
                tv_title_movie_detail.setText(responseMovieDetail.getTitle());
                rb_movie.setRating(Float.parseFloat(String.valueOf(responseMovieDetail.getVoteAverage())));
                tv_rb_movie.setText(String.valueOf(responseMovieDetail.getVoteAverage()));
                tv_release_date.setText(responseMovieDetail.getReleaseDate());
                tv_description.setText(responseMovieDetail.getOverview());
                tv_genre.setText(String.valueOf(responseMovieDetail.getGenres().get(0).getName()));

                Picasso.with(DetailMovieActivity.this)
                        .load(Config.IMAGE_W500+responseMovieDetail.getBackdropPath())
                        .transform(new BlurTransformation(getApplicationContext(), 5,1))
                        .fit()
                        .centerCrop()
                        .into(iv_poster_movie);

                Picasso.with(DetailMovieActivity.this)
                        .load(Config.IMAGE_W500+responseMovieDetail.getPosterPath())
                        .fit()
                        .centerCrop()
                        .into(iv_poster_movie_front);

                cl_1.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }
    };

    private void saveFavorite() {
        favoriteHelper.open();
        favoriteHelper.insertMovie(movie);
        favoriteHelper.close();
    }

    private Observer<String> getListMovieVideo = new Observer<String>() {
        @Override
        public void onChanged(String movies) {
            if (movies != null){
                String frameVideo = "<html><body>Video From YouTube<br><iframe width=\"320\" height=\"250\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
                System.out.println("cekPath11"+movies);
                webView.setVisibility(View.VISIBLE);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        return false;
                    }
                });

                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadData(frameVideo, "text/html", "utf-8");
                webView.loadUrl(movies);
            }else {
                webView.setVisibility(View.INVISIBLE);
                webView.setVisibility(View.GONE);
            }
        }
    };

    private void goToMainActivity() {
        Intent intent = new Intent(DetailMovieActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMainActivity();
    }

}
