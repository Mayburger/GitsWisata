package com.nacoda.wisata;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nacoda.wisata.gson.GsonDetail;
import com.nacoda.wisata.presenter.Presenter;
import com.nacoda.wisata.presenter.PresenterInterface;
import com.nacoda.wisata.utilities.UIHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class DetailActivity extends AppCompatActivity implements PresenterInterface {

    Presenter presenter;
    Dialog dialogLoad;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @InjectView(R.id.tvJudulDetail)
    TextView tvJudulDetail;
    @InjectView(R.id.tvLokasi)
    TextView tvLokasi;
    @InjectView(R.id.tvDeskripsi)
    TextView tvDeskripsi;
    @InjectView(R.id.tvCreated)
    TextView tvCreated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);

        dialogLoad = new Dialog(this);
        dialogLoad.setContentView(R.layout.loading_dialog);
        setTitle("");

        ProgressBar progressBar = dialogLoad.findViewById(R.id.pbLoad);
        UIHelper.ProgressBarWhite(progressBar);


        presenter = new Presenter(this);
        presenter.request_detail(getApplicationContext(), dialogLoad, getIntent().getStringExtra("id_data"));

    }

    @Override
    public void Response(String response) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        final GsonDetail gsonDetail = gson.fromJson(response, GsonDetail.class);

        if (gsonDetail.isStatus()) {

            tvCreated.setText(gsonDetail.getData().get(0).getCreated_at());
            tvDeskripsi.setText(gsonDetail.getData().get(0).getDescription());
            tvJudulDetail.setText(gsonDetail.getData().get(0).getTitle());
            tvLokasi.setText(gsonDetail.getData().get(0).getLocation());

            String url = "http://entry.sandbox.gits.id/api/alamku/uploads/images/" + gsonDetail.getData().get(0).getUrl_image();

            Picasso.with(getApplicationContext()).load(url).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    toolbarLayout.setBackground(new BitmapDrawable(bitmap));

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });

        } else {
            Toast.makeText(this, "Wrong username or password!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void ResponseBanner(String response) {

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
