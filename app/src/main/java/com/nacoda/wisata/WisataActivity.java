package com.nacoda.wisata;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nacoda.wisata.adapters.WisataAdapter;
import com.nacoda.wisata.gson.GsonDetail;
import com.nacoda.wisata.gson.GsonWisata;
import com.nacoda.wisata.presenter.Presenter;
import com.nacoda.wisata.presenter.PresenterInterface;
import com.nacoda.wisata.utilities.BannerData;
import com.nacoda.wisata.utilities.UIHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.events.OnBannerClickListener;
import ss.com.bannerslider.views.BannerSlider;

public class WisataActivity extends AppCompatActivity implements PresenterInterface {

    Presenter presenter;
    @InjectView(R.id.bsWisata)
    BannerSlider bsWisata;
    @InjectView(R.id.rvWisata)
    RecyclerView rvWisata;
    Dialog dialogLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wisata);
        ButterKnife.inject(this);

        dialogLoad = new Dialog(this);
        dialogLoad.setContentView(R.layout.loading_dialog);

        ProgressBar progressBar = dialogLoad.findViewById(R.id.pbLoad);
        UIHelper.ProgressBarWhite(progressBar);

        presenter = new Presenter(this);
        presenter.request_data(getApplicationContext(), 1, dialogLoad);
        presenter.request_banner(getApplicationContext(), 1);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.wisata_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.kategori:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.category_dialog);

                final RadioButton rbTinggi = dialog.findViewById(R.id.rbTinggi);
                final RadioButton rbRendah = dialog.findViewById(R.id.rbRendah);
                final RadioButton rbPantai = dialog.findViewById(R.id.rbPantai);
                TextView tvCancel = dialog.findViewById(R.id.tvCancel);
                TextView tvOk = dialog.findViewById(R.id.tvOk);

                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                tvOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (rbTinggi.isChecked()) {
                            presenter.request_data(getApplicationContext(), 1, dialogLoad);
                            presenter.request_banner(getApplicationContext(), 1);
                        } else if (rbRendah.isChecked()) {
                            presenter.request_data(getApplicationContext(), 2, dialogLoad);
                            presenter.request_banner(getApplicationContext(), 2);
                        } else if (rbPantai.isChecked()) {
                            presenter.request_data(getApplicationContext(), 3, dialogLoad);
                            presenter.request_banner(getApplicationContext(), 3);
                        }
                        dialog.dismiss();
                    }
                });


                dialog.show();

                return true;
            case R.id.logout:
                startActivity(new Intent(WisataActivity.this, LoginActivity.class));
                finish();
                return true;
            case R.id.add:
                Intent add = new Intent(WisataActivity.this, UploadActivity.class);
                add.putExtra("id_user", getIntent().getStringExtra("id_user"));
                startActivity(add);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void recyclerOnClickMain(RecyclerView rvRecipe, final Context mContext, final GsonWisata gsonWisata, final String id_user) {
        rvRecipe.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {

                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);

                    Intent detail = new Intent(mContext, DetailActivity.class);


                    detail.putExtra("id_user", id_user);
                    detail.putExtra("id_data", "" + gsonWisata.getData().get(position).getId_data());

                    mContext.startActivity(detail);


                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }

    @Override
    public void Response(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        final GsonWisata gsonWisata = gson.fromJson(response, GsonWisata.class);

        WisataAdapter adapter = new WisataAdapter(getApplicationContext(), gsonWisata.getData());
        GridLayoutManager glm = new GridLayoutManager(this, 2);

        rvWisata.setLayoutManager(glm);
        rvWisata.setAdapter(adapter);

        recyclerOnClickMain(rvWisata, WisataActivity.this, gsonWisata, getIntent().getStringExtra("id_user"));


    }

    @Override
    public void ResponseBanner(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        final GsonWisata gsonWisata = gson.fromJson(response, GsonWisata.class);

        bsWisata = (BannerSlider) findViewById(R.id.bsWisata);
        List<Banner> banners = new ArrayList<>();
        final BannerData bannerData = new BannerData();

        // Clear data for the new ones
        banners.clear();
        bannerData.clear();
        bsWisata.removeAllBanners();

        for (int i = 0; i < 5; i++) {
            bannerData.addData(
                    gsonWisata.getData().get(i).getId_data(),
                    gsonWisata.getData().get(i).getTitle(),
                    gsonWisata.getData().get(i).getDescription(),
                    gsonWisata.getData().get(i).getUrl_image()
            );
            banners.add(new RemoteBanner("http://entry.sandbox.gits.id/api/alamku/uploads/images/" + gsonWisata.getData().get(i).getUrl_image()));
        }

        bsWisata.setBanners(banners);
        bsWisata.setMustAnimateIndicators(true);
        bsWisata.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void onClick(int position) {

                Intent detail = new Intent(WisataActivity.this, DetailActivity.class);


                detail.putExtra("id_user", getIntent().getStringExtra("id_user"));
                detail.putExtra("id_data", "" + gsonWisata.getData().get(position).getId_data());

                startActivity(detail);


            }
        });


    }
}
