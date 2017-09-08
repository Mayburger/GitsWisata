package com.nacoda.wisata;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nacoda.wisata.presenter.Presenter;
import com.nacoda.wisata.presenter.PresenterInterface;
import com.nacoda.wisata.utilities.Fonts;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RegisterActivity extends AppCompatActivity implements PresenterInterface {

    @InjectView(R.id.tvTitleRegister)
    TextView tvTitleRegister;
    @InjectView(R.id.etFirstName)
    EditText etFirstName;
    @InjectView(R.id.etLastName)
    EditText etLastName;
    @InjectView(R.id.etUsername)
    EditText etUsername;
    @InjectView(R.id.etPassword)
    EditText etPassword;
    @InjectView(R.id.btnCreateAccount)
    Button btnCreateAccount;
    @InjectView(R.id.etBirthdate)
    EditText etBirthdate;
    @InjectView(R.id.etGender)
    EditText etGender;
    @InjectView(R.id.etPhone)
    EditText etPhone;

    DatePickerDialog.OnDateSetListener date;
    Calendar calendar = Calendar.getInstance();
    Presenter presenter;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);

        setFonts();
        setDatePicker();

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.progress_dialog);
        presenter = new Presenter(this);

        /** Set the progressbar color to white **/
        ProgressBar progressBar = dialog.findViewById(R.id.pbLoad);
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
            progressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        /** Set the progressbar color to white **/


        /** Request focus for dialogLoad input **/
        etPassword.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    birthdateOnClick(etPassword);
                    return true;
                }
                return false;
            }
        });

        etGender.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    genderOnClick(etGender);
                    return true;
                }
                return false;
            }
        });

        etPhone.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    register_onclick(etPhone);
                    return true;
                }
                return false;
            }
        });
        /** Request focus for dialogLoad input **/

    }

    /**
     * Setting Fonts
     **/
    private void setFonts() {
        Fonts.Montez(this, tvTitleRegister);
        Fonts.RobotoLight(this, etFirstName);
        Fonts.RobotoLight(this, etLastName);
        Fonts.RobotoLight(this, etUsername);
        Fonts.RobotoLight(this, etPassword);
        Fonts.RobotoLight(this, etBirthdate);
        Fonts.RobotoRegularButton(this, btnCreateAccount);
    }
    /** Setting Fonts **/

    /**
     * Register onClick
     **/
    public void register_onclick(View view) {
        String first_name = etFirstName.getText().toString().trim();
        String last_name = etLastName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String bday = etBirthdate.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String gender = etGender.getText().toString().trim();

        if (!username.isEmpty() && !password.isEmpty() && !first_name.isEmpty() && !last_name.isEmpty() && !bday.isEmpty() && !phone.isEmpty()) {


            presenter.request_register(first_name, last_name, username, password, bday, gender, phone, dialog, getApplicationContext());
        } else {
            Toast.makeText(getApplicationContext(),
                    "Please enter the credentials!",
                    Toast.LENGTH_LONG).show();
        }
    }
    /** Register onClick **/

    /**
     * Date picker
     **/
    private void setDatePicker() {

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                etBirthdate.setText(sdf.format(calendar.getTime()));
            }

        };
    }
    /** Date picker **/


    /**
     * Show DatePicker when clicked
     **/
    public void birthdateOnClick(View view) {
        new DatePickerDialog(RegisterActivity.this, date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    /** Show DatePicker when clicked **/

    /**
     * Show gender selection when clicked
     **/
    public void genderOnClick(View view) {
        PopupMenu popup = new PopupMenu(RegisterActivity.this, etGender);
        popup.getMenuInflater().inflate(R.menu.gender_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.male:
                        etGender.setText("Male");
                        return true;
                    case R.id.female:
                        etGender.setText("Female");
                        return true;
                }
                return true;
            }
        });

        popup.show();
    }

    /**
     * Show gender selection when clicked
     **/

    @Override
    public void Response(String response) {
        if (response != null){
            Toast.makeText(this, "Account succesfully created!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void ResponseBanner(String response) {

    }
}
