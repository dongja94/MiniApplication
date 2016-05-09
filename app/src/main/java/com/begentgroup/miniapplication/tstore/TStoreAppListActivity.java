package com.begentgroup.miniapplication.tstore;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.begentgroup.miniapplication.R;
import com.begentgroup.miniapplication.data.TStoreProduct;

public class TStoreAppListActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY_CODE = "category_code";
    String code;
    RecyclerView listView;
    ProductAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tstore_app_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        code = getIntent().getStringExtra(EXTRA_CATEGORY_CODE);

        listView = (RecyclerView)findViewById(R.id.rv_list);
        mAdapter = new ProductAdapter();
        listView.setAdapter(mAdapter);

        listView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter.setOnItemClickListener(new ProductViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View view, TStoreProduct product) {
                Intent intent = new Intent(TStoreAppListActivity.this, TStoreDetailActivity.class);
                intent.setData(Uri.parse(product.getWebUrl()));
                startActivity(intent);
            }
        });

        setData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setData() {
        for (int i = 0 ; i < 10 ; i++) {
            TStoreProduct p = new TStoreProduct();
            p.setCategoryPath(code);
            p.setCharge(0);
            p.setDescription("description " + i);
            p.setDetailDescription("detail " + i);
            p.setDownloadCount(i);
            p.setName("name " + i);
            p.setProductId("productid" + i);
            p.setScore(0.0f);
            p.setThumbnailUrl("url");
            p.setTinyUrl("url");
            p.setWebUrl("http://www.tstore.co.kr/userpoc/game/view?pid=0000699882");
            mAdapter.add(p);
        }
    }

}
