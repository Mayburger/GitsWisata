package com.nacoda.wisata.utilities;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Mayburger on 8/10/2017.
 */

public class UIHelper {

    public static void RobotoLight(Context context, TextView tvData) {
        Typeface Roboto = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_light.ttf");
        tvData.setTypeface(Roboto);
    }

    public static void Montez(Context context, TextView tvData) {
        Typeface Roboto = Typeface.createFromAsset(context.getAssets(), "fonts/montez.ttf");
        tvData.setTypeface(Roboto);
    }

    public static void RobotoRegularButton(Context context, Button btnData) {
        Typeface RobotoRegularButton = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_regular.ttf");
        btnData.setTypeface(RobotoRegularButton);
    }

    public static void RobotoRegular(Context context, TextView tvData) {
        Typeface RobotoRegular = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_regular.ttf");
        tvData.setTypeface(RobotoRegular);
    }

    public static void ProgressBarWhite(ProgressBar progressBar){
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
            progressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
        }
    }

}
