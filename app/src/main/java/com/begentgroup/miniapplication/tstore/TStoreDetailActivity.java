package com.begentgroup.miniapplication.tstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.begentgroup.miniapplication.R;
import com.begentgroup.miniapplication.data.TStoreProduct;
import com.begentgroup.miniapplication.manager.NetworkManager;

import java.io.IOException;

import okhttp3.Request;

public class TStoreDetailActivity extends AppCompatActivity {


    RecyclerView listView;

    ProductDetailAdapter mAdapter;

    public static final String EXTRA_PRODUCT_ID = "productId";

    String productId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tstore_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (RecyclerView)findViewById(R.id.rv_list);

        mAdapter = new ProductDetailAdapter();

        listView.setAdapter(mAdapter);
        listView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        productId = intent.getStringExtra(EXTRA_PRODUCT_ID);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setData();
    }

    private void setData() {
        NetworkManager.getInstance().getTStoreDetailProduct(this, productId, new NetworkManager.OnResultListener<TStoreProduct>() {
            @Override
            public void onSuccess(Request request, TStoreProduct result) {
                mAdapter.setProduct(result);
            }

            @Override
            public void onFail(Request request, IOException exception) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
