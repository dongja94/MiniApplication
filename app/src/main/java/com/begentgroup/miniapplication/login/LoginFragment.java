package com.begentgroup.miniapplication.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.begentgroup.miniapplication.MainActivity;
import com.begentgroup.miniapplication.R;
import com.begentgroup.miniapplication.manager.NetworkManager;
import com.begentgroup.miniapplication.manager.PropertyManager;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }

    Button facebookLoginButton;

    EditText emailView, passwordView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        facebookLoginButton = (Button)view.findViewById(R.id.btn_login_facebook);
        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
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

        emailView = (EditText)view.findViewById(R.id.edit_email);
        passwordView = (EditText)view.findViewById(R.id.edit_password);

        btn = (Button)view.findViewById(R.id.btn_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailView.getText().toString();
                final String password = passwordView.getText().toString();
                NetworkManager.getInstance().signin(getContext(), email, password, "",
                        new NetworkManager.OnResultListener<MyResult<User>>(){
                            @Override
                            public void onSuccess(Request request, MyResult<User> result) {
                                if (result.code == 1) {
                                    PropertyManager.getInstance().setLogin(true);
                                    PropertyManager.getInstance().setUser(result.result);
                                    PropertyManager.getInstance().setEmail(email);
                                    PropertyManager.getInstance().setPassword(password);
                                    goMainActivity();
                                }
                            }

                            @Override
                            public void onFail(Request request, IOException exception) {
                                Toast.makeText(getContext(), "error : " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
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
            facebookLoginButton.setText("facebook logout");
        } else {
            facebookLoginButton.setText("facebook login");
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
                    AccessToken token = AccessToken.getCurrentAccessToken();
                    NetworkManager.getInstance().facebookSignIn(getContext(), token.getToken(), "",
                            new NetworkManager.OnResultListener<MyResult>() {
                                @Override
                                public void onSuccess(Request request, MyResult result) {
                                    if (result.code == 1) {
                                        User user = (User)result.result;
                                        // login success
                                        PropertyManager.getInstance().setLogin(true);
                                        PropertyManager.getInstance().setUser(user);
                                        PropertyManager.getInstance().setFacebookId(user.facebookId);
                                        goMainActivity();
                                    } else if (result.code == 3) {
                                        FacebookInfo info = (FacebookInfo)result.result;
                                        ((LoginActivity)getActivity()).changeFacebookSignUp(info);
                                    }
                                }

                                @Override
                                public void onFail(Request request, IOException exception) {
                                }
                            });
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });

            loginManager.logInWithReadPermissions(this, Arrays.asList("email"));
        } else {
            loginManager.logOut();
        }
    }

    private void goMainActivity() {
        startActivity(new Intent(getContext(), MainActivity.class));
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void signup() {
        ((LoginActivity)getActivity()).changeSignUp();
    }

}
