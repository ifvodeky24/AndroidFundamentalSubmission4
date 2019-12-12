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
import com.idw.project.cataloguemovie.model.TvShow;
import com.idw.project.cataloguemovie.response.ResponseTvShowDetail;
import com.idw.project.cataloguemovie.rest.ApiClient;
import com.idw.project.cataloguemovie.rest.ApiInterface;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class DetailTvShowActivity extends AppCompatActivity {

    private static final String STATE_RESULT = "state_result";

    public static final String TAG = "tv";
    Integer id;

    ApiInterface apiService;

    ImageView iv_poster_tv, iv_poster_tv_front;
    TextView tv_title_tv_detail, tv_rb_tv, tv_genre, tv_release_date, tv_description;
    RatingBar rb_tv;

    private ProgressBar progressBar;
    private ConstraintLayout cl_2;

    private MainViewModel mainViewModel, detailViewModel;

    WebView webView;

    TvShow tvShow;

    MaterialFavoriteButton materialFavoriteButton;

    private FavoriteHelper favoriteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tv_show);
        setTitle("Detail Tv Show");

        apiService = ApiClient.getClient().create(ApiInterface.class);

        iv_poster_tv = findViewById(R.id.iv_poster_tv);
        iv_poster_tv_front = findViewById(R.id.iv_poster_tv_front);
        tv_title_tv_detail = findViewById(R.id.tv_title_tv_detail);
        tv_genre = findViewById(R.id.tv_genre);
        tv_release_date = findViewById(R.id.tv_release_date);
        tv_description = findViewById(R.id.tv_description);
        tv_rb_tv = findViewById(R.id.tv_rb_tv);
        rb_tv = findViewById(R.id.rb_tv);
        webView = findViewById(R.id.mWebView);
        materialFavoriteButton = findViewById(R.id.fab);
        progressBar = findViewById(R.id.progressbar);
        cl_2 =  findViewById(R.id.cl_2);

        progressBar.setVisibility(View.VISIBLE);
        cl_2.setVisibility(View.GONE);

        tvShow = getIntent().getExtras().getParcelable(TAG);

        //get Id
        id = tvShow.getId();

        favoriteHelper = new FavoriteHelper(this);
        favoriteHelper.open();
        if (favoriteHelper.CheckTvShow(id)){
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
                    favoriteHelper = new FavoriteHelper(DetailTvShowActivity.this);

                    favoriteHelper.open();
                    favoriteHelper.deleteTvShow(id);
                    favoriteHelper.close();

                    Snackbar.make(buttonView, "Dihapus dari Favorit", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getListTvVideos(String.valueOf(id)).observe(this, getListTvVideo);

        detailViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        detailViewModel.getDetailTv(String.valueOf(id)).observe(this, getDetailTv);

    }

    private final Observer<ResponseTvShowDetail> getDetailTv = new Observer<ResponseTvShowDetail>() {
        @Override
        public void onChanged(ResponseTvShowDetail responseTvShowDetail) {
            if (responseTvShowDetail != null) {
                tv_title_tv_detail.setText(responseTvShowDetail.getOriginalName());
                rb_tv.setRating(Float.parseFloat(String.valueOf(responseTvShowDetail.getVoteAverage())));
                tv_rb_tv.setText(String.valueOf(responseTvShowDetail.getVoteAverage()));
                tv_release_date.setText(responseTvShowDetail.getFirstAirDate());
                tv_description.setText(responseTvShowDetail.getOverview());
                tv_genre.setText(String.valueOf(responseTvShowDetail.getGenres().get(0).getName()));

                Picasso.with(DetailTvShowActivity.this)
                            .load(Config.IMAGE_W500+responseTvShowDetail.getBackdropPath())
                            .transform(new BlurTransformation(getApplicationContext(), 5,1))
                            .fit()
                            .centerCrop()
                            .into(iv_poster_tv);

                    Picasso.with(DetailTvShowActivity.this)
                            .load(Config.IMAGE_W500+responseTvShowDetail.getPosterPath())
                            .fit()
                            .centerCrop()
                            .into(iv_poster_tv_front);

                cl_2.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }
    };

    private void saveFavorite() {
        favoriteHelper.open();
        favoriteHelper.insertTvShow(tvShow);
        favoriteHelper.close();
    }

    private Observer<String> getListTvVideo = new Observer<String>() {
        @Override
        public void onChanged(String tvs) {
            if (tvs != null){

                String frameVideo = "<html><body>Video From YouTube<br><iframe width=\"320\" height=\"250\" frameborder=\"0\" allowfullscreen></iframe></body></html>";

                System.out.println("cekPath11"+tvs);

                webView.setVisibility(View.VISIBLE);

                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        return false;
                    }
                });

                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadData(frameVideo, "text/html", "utf-8");
                webView.loadUrl(tvs);
            }else {
                webView.setVisibility(View.INVISIBLE);
                webView.setVisibility(View.GONE);
            }
        }
    };

    private void goToMainActivity() {
        Intent intent = new Intent(DetailTvShowActivity.this, MainActivity.class);
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
