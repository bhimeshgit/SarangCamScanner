package com.loksuvidha.loksuvidhacamscanner;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.loksuvidha.loksuvidhacamscanner.helpers.MyConstants;
import com.loksuvidha.loksuvidhacamscanner.libraries.NativeClass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ImageEnhanceActivity extends AppCompatActivity {

    ImageView imageView;
    Bitmap selectedImageBitmap;

    Button btnImageToBW;
    Button next_btn;
    Button btnImageToGray;

    NativeClass nativeClass;
    String loksuvidha_pack_name="com.loksuvidha.loksuvidhacampro";
    String loksuvidha_activity_name="com.loksuvidha.loksuvidhacampro.MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_enhance);

        initializeElement();
        initializeImage();
    }

    private void initializeElement() {

        nativeClass = new NativeClass();

        imageView = findViewById(R.id.imageView);
        next_btn= findViewById(R.id.next_btn);

        next_btn.setOnClickListener(nextBtnClick);

    }

    private void initializeImage() {

        selectedImageBitmap = MyConstants.selectedImageBitmap;
        MyConstants.selectedImageBitmap = null;

        imageView.setImageBitmap(selectedImageBitmap);


    }


    private View.OnClickListener nextBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addImageToGallery(ImageEnhanceActivity.this,selectedImageBitmap);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setComponent(new ComponentName(loksuvidha_pack_name,loksuvidha_activity_name));
            intent.putExtra("scanner_call","yes");
            startActivity(intent);
            finishAffinity();
        }
    };

    //Saving the  Image
    public void addImageToGallery(final Context context, Bitmap bitmap) {

        Bitmap bmp = bitmap;
        File storageLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM); //context.getExternalFilesDir(null);
        File file = new File(storageLoc, "Demo" + ".jpg");
        try{
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            fos.close();
            Toast.makeText(getApplicationContext(),"Image Saved successfully",Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
