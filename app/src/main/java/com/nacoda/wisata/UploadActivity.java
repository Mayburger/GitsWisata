package com.nacoda.wisata;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {


    @InjectView(R.id.ivUpload)
    ImageView ivUpload;
    @InjectView(R.id.btnUpload)
    Button btnUpload;
    @InjectView(R.id.etJudul)
    EditText etJudul;
    @InjectView(R.id.etLokasi)
    EditText etLokasi;
    @InjectView(R.id.etKategori)
    EditText etKategori;
    @InjectView(R.id.etDeskripsi)
    EditText etDeskripsi;
    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;
    //Uri to store the image uri
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ButterKnife.inject(this);
        //Requesting storage permission
        requestStoragePermission();
        setTitle("Upload");
        //Initializing views

        //Setting clicklistener
        ivUpload.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
    }


    /*
    * This is the method responsible for image Upload
    * We need the full image path and the name for the image in this method
    * */
    public void uploadMultipart() {
        //getting name for the image
        //getting the actual path of the image
        String path = getPath(filePath);
        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, "http://entry.sandbox.gits.id/api/alamku/index.php/api/post/data/upload")
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("judul", etJudul.getText().toString())
                    .addParameter("location", etLokasi.getText().toString())
                    .addParameter("kategori", etKategori.getText().toString())
                    .addParameter("deskripsi", etDeskripsi.getText().toString())
                    .addParameter("id_user", getIntent().getStringExtra("id_user"))
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the UploadActivity
//            Toast.makeText(this, "berhasil menambah dan id == "+id, Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(getApplicationContext(), UploadActivity.class));
            ;
        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);

                ivUpload.setBackgroundDrawable(ob);
                ivUpload.setImageResource(0);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //Thi
    // s method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v == ivUpload) {
            showFileChooser();
            btnUpload.setEnabled(true);
        }
        if (v == btnUpload) {
            uploadMultipart();
            finish();
        }


    }


    @OnClick(R.id.etKategori)
    public void onClick() {
        PopupMenu popup = new PopupMenu(UploadActivity.this, etKategori);
        popup.getMenuInflater().inflate(R.menu.kategori_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.datTinggi:
                        etKategori.setText("Dataran Tinggi");
                        return true;
                    case R.id.datRendah:
                        etKategori.setText("Dataran Rendah");
                        return true;
                    case R.id.pantai:
                        etKategori.setText("Pantai");
                        return true;
                }
                return true;
            }
        });

        popup.show();
    }
}