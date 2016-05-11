package com.begentgroup.miniapplication;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.begentgroup.miniapplication.chatting.ChattingFragment;
import com.begentgroup.miniapplication.data.MyInfo;
import com.begentgroup.miniapplication.facebook.FacebookFragment;
import com.begentgroup.miniapplication.manager.NetworkManager;
import com.begentgroup.miniapplication.tstore.TStoreFragment;
import com.begentgroup.miniapplication.youtube.YoutubeFragment;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;

import java.io.IOException;

import okhttp3.Request;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView profileView;
    TextView nameView, emailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        profileView = (ImageView)headerView.findViewById(R.id.image_profile);
        nameView = (TextView)headerView.findViewById(R.id.text_name);
        emailView = (TextView)headerView.findViewById(R.id.text_email);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new TStoreFragment())
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setMyInfo();
    }

    public void setMyInfo() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token != null) {
            NetworkManager.getInstance().getFacebookMyInfo(this, token.getToken(), new NetworkManager.OnResultListener<MyInfo>() {
                @Override
                public void onSuccess(Request request, MyInfo result) {
                    nameView.setText(result.name);
                    emailView.setText(result.email);
                }

                @Override
                public void onFail(Request request, IOException exception) {

                }
            });
            String url = "https://graph.facebook.com/v2.6/me/picture?type=large&access_token="+token.getToken();
            Glide.with(MainActivity.this).load(url).into(profileView);
//            NetworkManager.getInstance().getFacebookMyPicture(this, token.getToken(), new NetworkManager.OnResultListener<String>() {
//                @Override
//                public void onSuccess(Request request, String result) {
//                    Glide.with(MainActivity.this).load(result).into(profileView);
//                }
//
//                @Override
//                public void onFail(Request request, IOException exception) {
//
//                }
//            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tstore) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new TStoreFragment())
                    .commit();
        } else if (id == R.id.nav_facebook) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new FacebookFragment())
                    .commit();
        } else if (id == R.id.nav_youtube) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new YoutubeFragment())
                    .commit();
        } else if (id == R.id.nav_chatting) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new ChattingFragment())
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
