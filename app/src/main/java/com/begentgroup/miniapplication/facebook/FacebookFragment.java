package com.begentgroup.miniapplication.facebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.begentgroup.miniapplication.R;
import com.begentgroup.miniapplication.data.FacebookFeed;
import com.begentgroup.miniapplication.manager.NetworkManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 */
public class FacebookFragment extends Fragment {
    public FacebookFragment() {
        // Required empty public constructor
    }



    RecyclerView listView;
    FeedAdapter mAdapter;
    CallbackManager callbackManager;
    LoginManager loginManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new FeedAdapter();
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_facebook, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_facebook_post) {
            startActivity(new Intent(getContext(), FacebookWriteActivity.class));
            return true;
        } else if (id == R.id.menu_facebook_upload) {
            startActivity(new Intent(getContext(), FacebookPhotoUploadActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_facebook, container, false);
        listView = (RecyclerView)view.findViewById(R.id.rv_list);
        listView.setAdapter(mAdapter);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        readFeed();
    }

    private void readFeed() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token != null) {
            if (token.getPermissions().contains("user_posts")) {
                NetworkManager.getInstance().getFacebookFeeds(getContext(), token.getToken(), new NetworkManager.OnResultListener<List<FacebookFeed>>() {
                    @Override
                    public void onSuccess(Request request, List<FacebookFeed> result) {
                        mAdapter.clear();
                        mAdapter.addAll(result);
                    }

                    @Override
                    public void onFail(Request request, IOException exception) {

                    }
                });
                return;
            }
        }

        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                readFeed();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        loginManager.logInWithReadPermissions(this, Arrays.asList("user_posts"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
