package com.loksuvidha.loksuvidhacamscanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.loksuvidha.loksuvidhacamscanner.helpers.MyConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    Button btnOpenGallery;
    Button btnImageProcess;
    ImageView imageView;
    Uri selectedImage;
    Bitmap selectedBitmap;
    static int REQUEST_WRITE_STORAGE_REQUEST_CODE=1;
    private final static String TAG= "loc_scanner";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestAppPermissions();
        initializeElement();
        initializeEvent();
        Intent intent=getIntent();
        if(intent!=null) {
    /*        String image_uri_string=null;
            if(!(intent.getExtras().getString("image_uri_string")==null)) {
                image_uri_string = intent.getExtras().getString("image_uri_string");
            }
            else{
                Toast.makeText(getApplicationContext(),"image_uri_string Null",Toast.LENGTH_SHORT).show();
            }
            if(image_uri_string!=null) {
                Log.d(TAG,"Uri="+image_uri_string);
                    selectedImage = Uri.parse(image_uri_string);

                try {
                    selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG,"File Not Found");

                }

    */
                if (intent.getExtras() != null) {
                    File path = new File(Environment.getExternalStorageDirectory(), "DCIM/Demo.jpg");

                    if (path.exists()) {
                        try {
                            selectedBitmap = BitmapFactory.decodeStream(new FileInputStream(path));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        imageView.setImageBitmap(selectedBitmap);
                        btnImageProcess.setVisibility(View.VISIBLE);
                        Log.d("lok_scanner", path.getAbsolutePath());
                        if (selectedBitmap != null) {
                            Log.d("lok_scanner", "Bitmnap not null");
                        } else {
                            Log.d("lok_scanner", "Bitmnap  null");
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Path not Exist", Toast.LENGTH_SHORT).show();
                    }
                }

        } else {
            Toast.makeText(getApplicationContext(), "Null Intent", Toast.LENGTH_SHORT).show();
        }


    }

    private void initializeElement() {
        this.imageView = (ImageView) findViewById(R.id.imageView);
        this.btnOpenGallery = (Button) findViewById(R.id.btnOpenGallery);
        this.btnImageProcess = (Button) findViewById(R.id.btnImageProcess);
    }

    private void initializeEvent() {
        this.btnOpenGallery.setOnClickListener(this.btnOpenGalleryClick);
        this.btnImageProcess.setOnClickListener(this.btnImageProcessClick);
    }

    private View.OnClickListener btnOpenGalleryClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, MyConstants.GALLERY_IMAGE_LOADED);
        }
    };

    private View.OnClickListener btnImageProcessClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //save selected bitmap to our constants
            //this method will save the image to our device memory
            //so set this variable to null after the image is no longer used
            MyConstants.selectedImageBitmap = selectedBitmap;

            //create new intent to start process image
            Intent intent = new Intent(getApplicationContext(), ImageCropActivity.class);
            startActivity(intent);

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyConstants.GALLERY_IMAGE_LOADED && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            this.loadImage();
        }
    }

    private void loadImage() {
        try {
            InputStream inputStream = getContentResolver().openInputStream(this.selectedImage);
            selectedBitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(selectedBitmap);
            btnImageProcess.setVisibility(View.VISIBLE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    //permission
    void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_WRITE_STORAGE_REQUEST_CODE); // your request code
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }


}
