package com.begentgroup.miniapplication.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.begentgroup.miniapplication.MainActivity;
import com.begentgroup.miniapplication.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }

    Button loginButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginButton = (Button)view.findViewById(R.id.btn_login_facebook);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        Button btn = (Button)view.findViewById(R.id.btn_sign_up);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        return view;
    }
    CallbackManager callbackManager;
    LoginManager loginManager;
    AccessTokenTracker tokenTracker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (tokenTracker == null) {
            tokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                    updateButtonText();
                }
            };
        } else {
            tokenTracker.startTracking();
        }
        updateButtonText();
    }

    @Override
    public void onStop() {
        super.onStop();
        tokenTracker.stopTracking();
    }

    private void updateButtonText() {
        if (isLogin()) {
            loginButton.setText("logout");
        } else {
            loginButton.setText("login");
        }
    }

    private boolean isLogin() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        return token!=null;
    }

    private void login() {
        if (!isLogin()) {
            loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    startActivity(new Intent(getContext(), MainActivity.class));
                    getActivity().finish();
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });

            loginManager.logInWithReadPermissions(this, Arrays.asList("emial"));
        } else {
            loginManager.logOut();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void signup() {

    }

}
