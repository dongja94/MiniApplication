package com.begentgroup.miniapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.begentgroup.miniapplication.login.LoginActivity;
import com.begentgroup.miniapplication.login.MyResult;
import com.begentgroup.miniapplication.login.User;
import com.begentgroup.miniapplication.manager.NetworkManager;
import com.begentgroup.miniapplication.manager.PropertyManager;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import java.io.IOException;

import okhttp3.Request;

public class SplashActivity extends AppCompatActivity {

    Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String email = PropertyManager.getInstance().getEmail();
        if (!TextUtils.isEmpty(email)) {
            String password = PropertyManager.getInstance().getPassword();
            NetworkManager.getInstance().signin(this, email, password, "", new NetworkManager.OnResultListener<MyResult<User>>() {
                @Override
                public void onSuccess(Request request, MyResult<User> result) {
                    if (result.code == 1) {
                        PropertyManager.getInstance().setLogin(true);
                        PropertyManager.getInstance().setUser(result.result);
                        goMainActivity();
                    }
                }

                @Override
                public void onFail(Request request, IOException exception) {
                    Toast.makeText(SplashActivity.this, "error : " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    goLoginActivity();
                }
            });
        } else {
            String facebookId = PropertyManager.getInstance().getFacebookId();
            if (!TextUtils.isEmpty(facebookId)) {
                AccessToken token = AccessToken.getCurrentAccessToken();
                if (token == null) {
                    PropertyManager.getInstance().setFacebookId("");
                    goLoginActivity();
                } else {
                    if (facebookId.equals(token.getUserId())) {
                        NetworkManager.getInstance().facebookSignIn(this, token.getToken(), "", new NetworkManager.OnResultListener<MyResult>() {
                            @Override
                            public void onSuccess(Request request, MyResult result) {
                                if (result.code == 1) {
                                    User user = (User)result.result;
                                    PropertyManager.getInstance().setLogin(true);
                                    PropertyManager.getInstance().setUser(user);
                                    goMainActivity();
                                } else {
                                    PropertyManager.getInstance().setFacebookId("");
                                    LoginManager.getInstance().logOut();
                                    goLoginActivity();
                                }

                            }

                            @Override
                            public void onFail(Request request, IOException exception) {
                                PropertyManager.getInstance().setFacebookId("");
                                LoginManager.getInstance().logOut();
                                goLoginActivity();
                            }
                        });
                    } else {
                        PropertyManager.getInstance().setFacebookId("");
                        LoginManager.getInstance().logOut();
                        goLoginActivity();
                    }
                }
            } else {
                goLoginActivity();
            }
        }
    }

    private void goMainActivity() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    private void goLoginActivity() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 2000);
    }
}
