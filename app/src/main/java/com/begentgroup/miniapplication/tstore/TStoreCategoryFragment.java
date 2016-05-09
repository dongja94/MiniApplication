package com.begentgroup.miniapplication.tstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.begentgroup.miniapplication.R;
import com.begentgroup.miniapplication.data.TStoreCategory;
import com.begentgroup.miniapplication.manager.NetworkManager;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 */
public class TStoreCategoryFragment extends Fragment {

    RecyclerView listView;

    CategoryAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new CategoryAdapter();
        mAdapter.setOnItemClickListener(new CategoryViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View view, TStoreCategory category) {
                Toast.makeText(getContext(), "code : " + category.getCategoryCode(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), TStoreAppListActivity.class);
                intent.putExtra(TStoreAppListActivity.EXTRA_CATEGORY_CODE, category.getCategoryCode());
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tstore_category, container, false);
        listView = (RecyclerView)view.findViewById(R.id.rv_list);
        listView.setAdapter(mAdapter);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {
        NetworkManager.getInstance().getTStoreCategory(getContext(), new NetworkManager.OnResultListener<List<TStoreCategory>>() {
            @Override
            public void onSuccess(Request request, List<TStoreCategory> result) {
                mAdapter.clear();
                mAdapter.addAll(result);
            }

            @Override
            public void onFail(Request request, IOException exception) {
                Toast.makeText(getContext(), "exception : " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
//        mAdapter.clear();
//        for (int i = 0; i < 10; i++) {
//            TStoreCategory category = new TStoreCategory();
//            category.setCategoryName("Category " + i);
//            category.setCategoryCode("Code : " + i);
//            mAdapter.add(category);
//        }
    }
}
