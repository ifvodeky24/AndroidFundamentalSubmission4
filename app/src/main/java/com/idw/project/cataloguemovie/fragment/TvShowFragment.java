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
import com.idw.project.cataloguemovie.activity.DetailTvShowActivity;
import com.idw.project.cataloguemovie.adapter.TvShowAdapter;
import com.idw.project.cataloguemovie.model.TvShow;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment {
    private RecyclerView recyclerView;
    private TvShowAdapter adapter;
    private ArrayList<TvShow> TvArrayList = new ArrayList<>();
    private ShimmerFrameLayout mShimmerViewContainer;
    private MainViewModel TvViewModel;

    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tvshow, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("Katalog TV Show");

        TvViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        TvViewModel.getListTv().observe((LifecycleOwner) getActivity(), getListTv);

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
                        TvArrayList.clear();
                        TvViewModel.setListTv();
                    }
                }, 1000);
            }
        });
    }

    private Observer<ArrayList<TvShow>> getListTv = new Observer<ArrayList<TvShow>>() {
        @Override
        public void onChanged(@Nullable ArrayList<TvShow> tvs) {
            if (tvs != null){
                TvArrayList = tvs;

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(new TvShowAdapter(getContext(), TvArrayList, new TvShowAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(TvShow item) {
                        Intent intent = new Intent(getContext(), DetailTvShowActivity.class);
                        intent.putExtra(DetailTvShowActivity.TAG, item);
                        System.out.println("id nya adalah"+ item.getId());
                        startActivity(intent);
//                        Toast.makeText(getContext(), "Klik Item"+item.getId(), Toast.LENGTH_SHORT).show();
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
