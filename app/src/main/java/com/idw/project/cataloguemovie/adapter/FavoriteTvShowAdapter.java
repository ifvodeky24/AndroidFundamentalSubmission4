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
import com.idw.project.cataloguemovie.activity.DetailTvShowActivity;
import com.idw.project.cataloguemovie.config.Config;
import com.idw.project.cataloguemovie.model.TvShow;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoriteTvShowAdapter extends RecyclerView.Adapter<FavoriteTvShowAdapter.FavoriteTvShowViewholder> {
    private Activity activity;

    private ArrayList<TvShow> dataList = new ArrayList<>();

    public FavoriteTvShowAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<TvShow> getListTvShow() {
        return dataList;
    }

    public void setListTvShow(ArrayList<TvShow> listTvShow) {

        if (listTvShow.size() > 0) {
            this.dataList.clear();
        }
        this.dataList.addAll(listTvShow);

        notifyDataSetChanged();
    }

    public void addItem(TvShow tvShow) {
        this.dataList.add(tvShow);
        notifyItemInserted(dataList.size() - 1);
    }

    public void updateItem(int position, TvShow tvShow) {
        this.dataList.set(position, tvShow);
        notifyItemChanged(position, tvShow);
    }

    public void removeItem(int position) {
        this.dataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataList.size());
    }

    @NonNull
    @Override
    public FavoriteTvShowViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tvshow_item, parent,
                false);

        return new FavoriteTvShowViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteTvShowViewholder favoriteTvShowViewholder, int position) {
        favoriteTvShowViewholder.tv_title_tv.setText(dataList.get(position).getOriginalName());
        favoriteTvShowViewholder.tv_release_date_tv.setText(dataList.get(position).getFirstAirDate());
        favoriteTvShowViewholder.tv_description_tv.setText(dataList.get(position).getOverview());
        favoriteTvShowViewholder.tv_rb_tv.setText(String.valueOf(dataList.get(position).getVoteAverage()));
        favoriteTvShowViewholder.rb_tv.setRating(Float.parseFloat(String.valueOf(dataList.get(position).getVoteAverage())));

        Picasso.with(activity)
                .load(Config.IMAGE_W342+dataList.get(position).getPosterPath())
                .fit()
                .centerCrop()
                .into(favoriteTvShowViewholder.iv_poster_tv);

        favoriteTvShowViewholder.cv_tv.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(activity, DetailTvShowActivity.class);
                intent.putExtra(DetailTvShowActivity.TAG, dataList.get(position));
                activity.startActivity(intent);

//                Toast.makeText(activity, "Klik Item "+dataList.get(position).getId(), Toast.LENGTH_SHORT).show();
            }
        }));

    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() :0;
    }

    public class FavoriteTvShowViewholder extends RecyclerView.ViewHolder{
        ImageView iv_poster_tv;
        TextView tv_title_tv, tv_release_date_tv, tv_description_tv, tv_rb_tv;
        CardView cv_tv;
        RatingBar rb_tv;

        public FavoriteTvShowViewholder(@NonNull View itemView) {
            super(itemView);
            iv_poster_tv = itemView.findViewById(R.id.iv_poster_tv);
            tv_title_tv = itemView.findViewById(R.id.tv_title_tv);
            tv_release_date_tv = itemView.findViewById(R.id.tv_release_date_tv);
            rb_tv = itemView.findViewById(R.id.rb_tv);
            tv_rb_tv = itemView.findViewById(R.id.tv_rb_tv);
            tv_description_tv = itemView.findViewById(R.id.tv_description_tv);
            cv_tv = itemView.findViewById(R.id.cv_tv);
        }

    }

}
