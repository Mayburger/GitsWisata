package com.nacoda.wisata;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nacoda.wisata.gson.GsonRegister;
import com.nacoda.wisata.presenter.Presenter;
import com.nacoda.wisata.presenter.PresenterInterface;
import com.nacoda.wisata.utilities.Fonts;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class LoginActivity extends AppCompatActivity implements PresenterInterface {


    @InjectView(R.id.tvTitleLogin)
    TextView tvTitleLogin;
    @InjectView(R.id.etUsername)
    EditText etUsername;
    @InjectView(R.id.etPassword)
    EditText etPassword;
    @InjectView(R.id.btnRegister)
    Button btnRegister;
    Presenter presenter;
    Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        Fonts.Montez(this, tvTitleLogin);
        Fonts.RobotoLight(this, etUsername);
        Fonts.RobotoLight(this, etPassword);
        Fonts.RobotoRegularButton(this, btnRegister);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.progress_dialog);

        /** Set the progressbar color to white **/
        ProgressBar progressBar = dialog.findViewById(R.id.pbLoad);
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
            progressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        /** Set the progressbar color to white **/

        presenter = new Presenter(this);

    }

    public void start_registerActivity(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    public void login_onclick(View view) {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (!username.isEmpty() && !password.isEmpty()) {
            presenter.request_login(username, password, getApplicationContext(), dialog);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Please enter the credentials!",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void Response(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        GsonRegister gsonRegister = gson.fromJson(response, GsonRegister.class);

        if (gsonRegister.isStatus()){

            Intent intent = new Intent(LoginActivity.this, WisataActivity.class);
            intent.putExtra("id_user", gsonRegister.getData().get(0).getId_user());
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(this, "Wrong username or password!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void ResponseBanner(String response) {

    }
}