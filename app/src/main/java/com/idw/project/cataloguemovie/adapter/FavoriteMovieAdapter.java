package com.idw.project.cataloguemovie.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.idw.project.cataloguemovie.CustomOnItemClickListener;
import com.idw.project.cataloguemovie.R;
import com.idw.project.cataloguemovie.activity.DetailMovieActivity;
import com.idw.project.cataloguemovie.config.Config;
import com.idw.project.cataloguemovie.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieViewholder> {
    private Activity activity;

    private ArrayList<Movie> dataList = new ArrayList<>();

    public FavoriteMovieAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Movie> getListMovies() {
        return dataList;
    }

    public void setListMovies(ArrayList<Movie> listMovies) {

        if (listMovies.size() > 0) {
            this.dataList.clear();
        }
        this.dataList.addAll(listMovies);

        notifyDataSetChanged();
    }

    public void addItem(Movie movie) {
        this.dataList.add(movie);
        notifyItemInserted(dataList.size() - 1);
    }

    public void updateItem(int position, Movie movie) {
        this.dataList.set(position, movie);
        notifyItemChanged(position, movie);
    }

    public void removeItem(int position) {
        this.dataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataList.size());
    }

    @NonNull
    @Override
    public FavoriteMovieViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent,
                false);

        return new FavoriteMovieViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteMovieViewholder favoriteMovieViewholder, int position) {
        favoriteMovieViewholder.tv_title_movie.setText(dataList.get(position).getTitle());
        favoriteMovieViewholder.tv_release_date_movie.setText(dataList.get(position).getReleaseDate());
        favoriteMovieViewholder.tv_description_movie.setText(dataList.get(position).getOverview());
        favoriteMovieViewholder.tv_rb_movie.setText(String.valueOf(dataList.get(position).getVoteAverage()));
        favoriteMovieViewholder.rb_movie.setRating(Float.parseFloat(String.valueOf(dataList.get(position).getVoteAverage())));

        Picasso.with(activity)
                .load(Config.IMAGE_W342+dataList.get(position).getPosterPath())
                .fit()
                .centerCrop()
                .into(favoriteMovieViewholder.iv_poster_movie);

        favoriteMovieViewholder.cv_movie.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(activity, DetailMovieActivity.class);
                intent.putExtra(DetailMovieActivity.TAG, dataList.get(position));
                activity.startActivity(intent);

//                Toast.makeText(activity, "Klik Item "+dataList.get(position).getId(), Toast.LENGTH_SHORT).show();
            }
        }));

    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() :0;
    }

    public class FavoriteMovieViewholder extends RecyclerView.ViewHolder{
        ImageView iv_poster_movie;
        TextView tv_title_movie, tv_release_date_movie, tv_description_movie, tv_rb_movie;
        CardView cv_movie;
        RatingBar rb_movie;

        public FavoriteMovieViewholder(@NonNull View itemView) {
            super(itemView);
            iv_poster_movie = itemView.findViewById(R.id.iv_poster_movie);
            tv_title_movie = itemView.findViewById(R.id.tv_title_movie);
            tv_release_date_movie = itemView.findViewById(R.id.tv_release_date_movie);
            rb_movie = itemView.findViewById(R.id.rb_movie);
            tv_rb_movie = itemView.findViewById(R.id.tv_rb_movie);
            tv_description_movie = itemView.findViewById(R.id.tv_description_movie);
            cv_movie = itemView.findViewById(R.id.cv_movie);
        }

    }

}
