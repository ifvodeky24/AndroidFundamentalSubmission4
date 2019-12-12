package com.idw.project.cataloguemovie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.idw.project.cataloguemovie.MainViewModel;
import com.idw.project.cataloguemovie.R;
import com.idw.project.cataloguemovie.activity.DetailMovieActivity;
import com.idw.project.cataloguemovie.adapter.MovieAdapter;
import com.idw.project.cataloguemovie.config.Config;
import com.idw.project.cataloguemovie.model.Movie;
import com.idw.project.cataloguemovie.response.ResponseMovie;
import com.idw.project.cataloguemovie.rest.ApiClient;
import com.idw.project.cataloguemovie.rest.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private ArrayList<Movie> MovieArrayList = new ArrayList<>();
    private ShimmerFrameLayout mShimmerViewContainer;
    private MainViewModel mainViewModel;

    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("Katalog Movie");


        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);

        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        mainViewModel.getListMovies().observe(getActivity(), getListMovies);

        initView(view);
        return view;
    }

    private void initView(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorAccent);
        refresh();
    }

    private void refresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        MovieArrayList.clear();
                        mainViewModel.setListMovie();
                    }
                }, 1000);
            }
        });
    }

    private Observer<ArrayList<Movie>> getListMovies = new Observer<ArrayList<Movie>>() {
        @Override
        public void onChanged(@Nullable ArrayList<Movie> movies) {
            if (movies != null){
                MovieArrayList = movies;

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(new MovieAdapter(getContext(), MovieArrayList, new MovieAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Movie item) {
                        Intent intent = new Intent(getContext(), DetailMovieActivity.class);
                        intent.putExtra(DetailMovieActivity.TAG, item);
                        System.out.println("id nya adalah"+ item.getId());
                        startActivity(intent);
//                        Toast.makeText(getContext(), "Klik Item"+item.getTitle(), Toast.LENGTH_SHORT).show();
                    }
                }));

                mShimmerViewContainer.stopShimmerAnimation();

                mShimmerViewContainer.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mShimmerViewContainer.stopShimmerAnimation();

    }
}
