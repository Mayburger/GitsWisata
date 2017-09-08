package com.nacoda.wisata.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nacoda.wisata.DetailActivity;
import com.nacoda.wisata.R;
import com.nacoda.wisata.gson.GsonWisata;
import com.nacoda.wisata.utilities.Fonts;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Mayburger on 4/19/2017.
 */

public class WisataAdapter extends RecyclerView.Adapter<WisataAdapter.ViewHolder> {

    private Context context;
    private List<GsonWisata.Data> gsonWisata;

    public WisataAdapter(Context context, List<GsonWisata.Data> gsonWisata) {
        this.context = context;
        this.gsonWisata = gsonWisata;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_wisata, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        String url = "http://entry.sandbox.gits.id/api/alamku/uploads/images/" + gsonWisata.get(position).getUrl_image();

        Picasso.with(context).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.ivWisata.setBackground(new BitmapDrawable(bitmap));
                holder.ivWisata.setImageResource(0);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        Fonts.RobotoLight(context, holder.tvWisata);

        holder.tvWisata.setText(gsonWisata.get(position).getTitle());
        holder.rbWisata.setRating(gsonWisata.get(position).getRate());

    }

    @Override
    public int getItemCount() {
        return gsonWisata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.ivWisata)
        ImageView ivWisata;
        @InjectView(R.id.tvWisata)
        TextView tvWisata;
        @InjectView(R.id.rbWisata)
        RatingBar rbWisata;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);

        }
    }
}
