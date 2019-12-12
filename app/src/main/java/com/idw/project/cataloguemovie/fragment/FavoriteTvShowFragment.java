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

import com.idw.project.cataloguemovie.LoadFavoriteTvShowCallback;
import com.idw.project.cataloguemovie.R;
import com.idw.project.cataloguemovie.adapter.FavoriteTvShowAdapter;
import com.idw.project.cataloguemovie.db.FavoriteHelper;
import com.idw.project.cataloguemovie.model.TvShow;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class
FavoriteTvShowFragment extends Fragment implements LoadFavoriteTvShowCallback {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FavoriteHelper favoriteHelper;
    private FavoriteTvShowAdapter adapter;
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private LinearLayout ll_empty_data;

    public FavoriteTvShowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_favorite_tv_show, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("Favorite");

        recyclerView = view.findViewById(R.id.rvTvShow);
        progressBar = view.findViewById(R.id.progressbar);
        ll_empty_data = view.findViewById(R.id.ll_empty_data);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new FavoriteTvShowAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        favoriteHelper = FavoriteHelper.getInstance(getContext());

        favoriteHelper.open();

        /*
        Cek jika savedInstaceState null makan akan melakukan proses asynctask nya
        jika tidak,akan mengambil arraylist nya dari yang sudah di simpan
         */
        if (savedInstanceState == null) {
            new LoadFavTvShowAsync(favoriteHelper,  this).execute();
        } else {
            ArrayList<TvShow> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null ) {
                adapter.setListTvShow(list);
            }


        }

        // Inflate the layout for this fragment

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListTvShow());
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
    public void postExecute(ArrayList<TvShow> tvShows) {
        progressBar.setVisibility(View.INVISIBLE);
        if (tvShows.size()==0){
            Toast.makeText(getActivity(), "Data Favorite Tv Show Kosong", Toast.LENGTH_SHORT).show();
            ll_empty_data.setVisibility(View.VISIBLE);
        }
        adapter.setListTvShow(tvShows);
    }


    private class LoadFavTvShowAsync extends AsyncTask<Void, Void, ArrayList<TvShow>> {
        private final WeakReference<FavoriteHelper> weakFavoriteHelper;
        private final WeakReference<LoadFavoriteTvShowCallback> weakCallback;

        private LoadFavTvShowAsync(FavoriteHelper favoriteHelper, LoadFavoriteTvShowCallback callback) {
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
        protected ArrayList<TvShow> doInBackground(Void... voids) {
            return weakFavoriteHelper.get().getAllTvShow();
        }

        @Override
        protected void onPostExecute(ArrayList<TvShow> tvShows) {
            super.onPostExecute(tvShows);
            progressBar.setVisibility(View.GONE);

            weakCallback.get().postExecute(tvShows);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        favoriteHelper.close();
    }
}
