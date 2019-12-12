package com.idw.project.cataloguemovie.fragment;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.idw.project.cataloguemovie.LoadFavoriteMovieCallback;
import com.idw.project.cataloguemovie.R;
import com.idw.project.cataloguemovie.adapter.FavoriteMovieAdapter;
import com.idw.project.cataloguemovie.db.FavoriteHelper;
import com.idw.project.cataloguemovie.model.Movie;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteMovieFragment extends Fragment implements LoadFavoriteMovieCallback {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FavoriteHelper favoriteHelper;
    private FavoriteMovieAdapter adapter;
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private LinearLayout ll_empty_data;

    public FavoriteMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_favorite_movie, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("Favorite");

        recyclerView = view.findViewById(R.id.rvMovies);
        progressBar = view.findViewById(R.id.progressbar);
        ll_empty_data = view.findViewById(R.id.ll_empty_data);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new FavoriteMovieAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        favoriteHelper = FavoriteHelper.getInstance(getContext());

        favoriteHelper.open();

        /*
        Cek jika savedInstaceState null makan akan melakukan proses asynctask nya
        jika tidak,akan mengambil arraylist nya dari yang sudah di simpan
         */
        if (savedInstanceState == null) {
            new LoadFavMovieAsync(favoriteHelper,  this).execute();
        } else {
            ArrayList<Movie> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null ) {
                adapter.setListMovies(list);
            }


        }

        // Inflate the layout for this fragment

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListMovies());
    }

    @Override
    public void preExecute() {
        /*
        Callback yang akan dipanggil di onPreExecute Asyntask
        Memunculkan progressbar
        */
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<Movie> movies) {
        progressBar.setVisibility(View.INVISIBLE);
        if (movies.size()==0){
            Toast.makeText(getActivity(), "Data Favorite Movie Kosong", Toast.LENGTH_SHORT).show();
            ll_empty_data.setVisibility(View.VISIBLE);
        }
        adapter.setListMovies(movies);
    }


    private class LoadFavMovieAsync extends AsyncTask<Void, Void, ArrayList<Movie>> {
        private final WeakReference<FavoriteHelper> weakFavoriteHelper;
        private final WeakReference<LoadFavoriteMovieCallback> weakCallback;

        private LoadFavMovieAsync(FavoriteHelper favoriteHelper, LoadFavoriteMovieCallback callback) {
            weakFavoriteHelper = new WeakReference<>(favoriteHelper);
            weakCallback = new WeakReference<>(callback);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {
            return weakFavoriteHelper.get().getAllMovies();
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            progressBar.setVisibility(View.GONE);

            weakCallback.get().postExecute(movies);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        favoriteHelper.close();
    }
}
