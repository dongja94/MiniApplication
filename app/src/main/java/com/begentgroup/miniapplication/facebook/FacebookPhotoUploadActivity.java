package com.begentgroup.miniapplication.facebook;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.begentgroup.miniapplication.R;
import com.begentgroup.miniapplication.data.FacebookUploadResult;
import com.begentgroup.miniapplication.manager.NetworkManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import okhttp3.Request;

public class FacebookPhotoUploadActivity extends AppCompatActivity {

    EditText captionView;
    ImageView imageView;

    CallbackManager callbackManager;
    LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_photo_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();

        captionView = (EditText)findViewById(R.id.edit_caption);
        imageView = (ImageView)findViewById(R.id.image_picture);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });

        Button btn = (Button)findViewById(R.id.btn_gallery);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getImageFromGallery();
            }
        });

        btn = (Button)findViewById(R.id.btn_camera);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromCamera();
            }
        });
        if (savedInstanceState != null) {
            String path = savedInstanceState.getString("uploadFile");
            if (!TextUtils.isEmpty(path)) {
                mUploadFile = new File(path);
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inSampleSize = 2;
                Bitmap bm = BitmapFactory.decodeFile(path, opts);
            }

            path = savedInstanceState.getString("cameraFile");
            if (!TextUtils.isEmpty(path)) {
                mCameraCaptureFile = new File(path);
            }
        }
    }

    private static final int RC_GALLERY = 1;
    private static final int RC_CAMERA = 2;

    private void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getCameraCaptureFile());
        startActivityForResult(intent, RC_CAMERA);
    }


    File mCameraCaptureFile;

    private Uri getCameraCaptureFile() {
        File dir = getExternalFilesDir("myphoto");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        mCameraCaptureFile = new File(dir, "my_photo_"+System.currentTimeMillis()+".jpg");
        return Uri.fromFile(mCameraCaptureFile);
    }

    private void getImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/jpeg");
        startActivityForResult(intent, RC_GALLERY);
    }


    private void upload() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token != null) {
            if (token.getPermissions().contains("publish_actions")) {
                String caption = captionView.getText().toString();
                if (!TextUtils.isEmpty(caption) && mUploadFile != null) {
                    NetworkManager.getInstance().getFacebookUpload(this, token.getToken(), caption, mUploadFile, new NetworkManager.OnResultListener<FacebookUploadResult>() {
                        @Override
                        public void onSuccess(Request request, FacebookUploadResult result) {
                            Toast.makeText(FacebookPhotoUploadActivity.this, "post : " + result.post_id, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFail(Request request, IOException exception) {
                            Toast.makeText(FacebookPhotoUploadActivity.this, "error ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return;
            }
        }
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                upload();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        loginManager.logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
    }

    File mUploadFile = null;

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (mUploadFile != null) {
            outState.putString("uploadfile", mUploadFile.getAbsolutePath());
        }
        if (mCameraCaptureFile != null) {
            outState.putString("cameraFile", mCameraCaptureFile.getAbsolutePath());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(uri, projection, null, null, null);
                if (c.moveToNext()) {
                    String path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
                    mUploadFile = new File(path);
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inSampleSize = 2;
                    Bitmap bm = BitmapFactory.decodeFile(path, opts);
                    imageView.setImageBitmap(bm);
                }
            }
            return;
        }

        if (requestCode == RC_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                File file = mCameraCaptureFile;
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inSampleSize = 2;
                Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
                mUploadFile = file;
            }
            return;
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
