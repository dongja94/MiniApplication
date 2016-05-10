package com.begentgroup.miniapplication.tstore;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.begentgroup.miniapplication.R;
import com.begentgroup.miniapplication.data.TStoreCategoryProduct;
import com.begentgroup.miniapplication.data.TStoreProduct;
import com.begentgroup.miniapplication.manager.NetworkManager;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 */
public class TStoreSearchFragment extends Fragment {


    public TStoreSearchFragment() {
        // Required empty public constructor
    }

    RecyclerView listView;
    ProductAdapter mAdapter;
    EditText keywordView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ProductAdapter();
        mAdapter.setOnItemClickListener(new ProductViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View view, TStoreProduct product) {
                Intent intent = new Intent(getContext(), TStoreDetailActivity.class);
//                intent.setData(Uri.parse(product.getWebUrl()));
                intent.putExtra(TStoreDetailActivity.EXTRA_PRODUCT_ID, product.getProductId());
                startActivity(intent);
            }
        });
    }

    LinearLayoutManager mLayoutManager;

    boolean isLast = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tstore_search, container, false);
        listView = (RecyclerView)view.findViewById(R.id.rv_list);
        listView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());
        listView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(mLayoutManager);
        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (isLast && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    getMoreData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalCount = mAdapter.getItemCount();
                int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                if (totalCount > 0 && lastVisibleItem >= totalCount - 1) {
                    isLast = true;
                } else {
                    isLast = false;
                }
            }
        });
        keywordView = (EditText)view.findViewById(R.id.edit_keyword);
        Button btn = (Button)view.findViewById(R.id.btn_search);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String keyword = keywordView.getText().toString();
                if (!TextUtils.isEmpty(keyword)) {
                    try {
                        NetworkManager.getInstance().getTStoreSearchProductList(getContext(), keyword, 1, 10, NetworkManager.SEARCH_PRODUCT_ORDER_R, new NetworkManager.OnResultListener<TStoreCategoryProduct>() {
                            @Override
                            public void onSuccess(Request request, TStoreCategoryProduct result) {
                                mAdapter.setKeyword(keyword);
                                mAdapter.setLastPage(1);
                                mAdapter.setTotalCount(result.totalCount);
                                mAdapter.clear();
                                mAdapter.addAll(result.products.productList);
                            }

                            @Override
                            public void onFail(Request request, IOException exception) {
                                Toast.makeText(getContext(), "fail : " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return view;
    }

    boolean isMoreData = false;
    private void getMoreData() {
        if (!isMoreData && mAdapter.isMore()) {
            isMoreData = true;
            final int page = mAdapter.getLastPage() + 1;
            try {
                NetworkManager.getInstance().getTStoreSearchProductList(getContext(), mAdapter.getKeyword(), page, 10, NetworkManager.SEARCH_PRODUCT_ORDER_R, new NetworkManager.OnResultListener<TStoreCategoryProduct>() {
                    @Override
                    public void onSuccess(Request request, TStoreCategoryProduct result) {
                        mAdapter.addAll(result.products.productList);
                        mAdapter.setLastPage(page);
                        isMoreData = false;
                    }

                    @Override
                    public void onFail(Request request, IOException exception) {
                        isMoreData = false;
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                isMoreData = false;
            }
        }
    }

}
