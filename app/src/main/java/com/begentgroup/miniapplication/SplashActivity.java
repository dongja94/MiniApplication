package com.begentgroup.miniapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.begentgroup.miniapplication.gcm.RegistrationIntentService;
import com.begentgroup.miniapplication.login.LoginActivity;
import com.begentgroup.miniapplication.login.MyResult;
import com.begentgroup.miniapplication.login.User;
import com.begentgroup.miniapplication.manager.NetworkManager;
import com.begentgroup.miniapplication.manager.PropertyManager;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.IOException;

import okhttp3.Request;

public class SplashActivity extends AppCompatActivity {

    Handler mHandler = new Handler(Looper.getMainLooper());

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                doRealStart();
            }
        };
        setUpIfNeeded();


    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(RegistrationIntentService.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLAY_SERVICES_RESOLUTION_REQUEST &&
                resultCode == Activity.RESULT_OK) {
            setUpIfNeeded();
        }
    }

    private void setUpIfNeeded() {
        if (checkPlayServices()) {
            String regId = PropertyManager.getInstance().getRegistrationToken();
            if (!regId.equals("")) {
                doRealStart();
            } else {
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
        }
    }

    private void doRealStart() {
        startSplash();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                Dialog dialog = apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                });
                dialog.show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    private void startSplash() {
        String email = PropertyManager.getInstance().getEmail();
        if (!TextUtils.isEmpty(email)) {
            String password = PropertyManager.getInstance().getPassword();
            NetworkManager.getInstance().signin(this, email, password, PropertyManager.getInstance().getRegistrationToken(), new NetworkManager.OnResultListener<MyResult<User>>() {
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
                        NetworkManager.getInstance().facebookSignIn(this, token.getToken(), PropertyManager.getInstance().getRegistrationToken(), new NetworkManager.OnResultListener<MyResult>() {
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
