package com.begentgroup.miniapplication.facebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.begentgroup.miniapplication.R;
import com.begentgroup.miniapplication.manager.NetworkManager;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Request;

public class FacebookWriteActivity extends AppCompatActivity {

    EditText messageView, captionView, nameView, descriptionView;
    ImageView photoView;

    private static final String PHOTO_URL = "http://www.nasa.gov/sites/default/files/styles/image_card_4x3_ratio/public/thumbnails/image/idcs1426.jpg?itok=Gc_-Q58L";
    private static final String LINK_URL = "http://www.nasa.gov/mission_pages/chandra/images/index.html";
    CallbackManager callbackManager;
    LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_write);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        messageView = (EditText)findViewById(R.id.edit_message);
        captionView = (EditText)findViewById(R.id.edit_caption);
        nameView = (EditText)findViewById(R.id.edit_name);
        descriptionView = (EditText)findViewById(R.id.edit_description);
        photoView = (ImageView)findViewById(R.id.image_photo);

        Glide.with(this).load(PHOTO_URL).into(photoView);

        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });
    }

    private void post() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            if (accessToken.getPermissions().contains("publish_actions")) {
                String message = messageView.getText().toString();
                String name = nameView.getText().toString();
                String caption = captionView.getText().toString();
                String description = descriptionView.getText().toString();
                NetworkManager.getInstance().getFacebookPost(this, accessToken.getToken(),
                        message, caption, LINK_URL, PHOTO_URL, name, description, new NetworkManager.OnResultListener<String>() {
                            @Override
                            public void onSuccess(Request request, String result) {
                                Toast.makeText(FacebookWriteActivity.this, "id : " + result , Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFail(Request request, IOException exception) {
                                Toast.makeText(FacebookWriteActivity.this, "fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                return;
            }
        }

        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                post();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
