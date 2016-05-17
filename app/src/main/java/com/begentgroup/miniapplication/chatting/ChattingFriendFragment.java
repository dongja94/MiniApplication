package com.begentgroup.miniapplication.chatting;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.begentgroup.miniapplication.R;
import com.begentgroup.miniapplication.login.MyResult;
import com.begentgroup.miniapplication.login.User;
import com.begentgroup.miniapplication.manager.NetworkManager;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChattingFriendFragment extends Fragment {


    public ChattingFriendFragment() {
        // Required empty public constructor
    }

    RecyclerView listView;
    FriendAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new FriendAdapter();
        mAdapter.setOnItemClickListener(new FriendViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(User user) {
                Intent intent = new Intent(getContext(), ChattingActivity.class);
                intent.putExtra(ChattingActivity.EXTRA_USER, user);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatting_friend, container, false);
        listView = (RecyclerView)view.findViewById(R.id.rv_list);
        listView.setAdapter(mAdapter);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        NetworkManager.getInstance().getFriendList(getContext(), new NetworkManager.OnResultListener<MyResult<List<User>>>() {
            @Override
            public void onSuccess(Request request, MyResult<List<User>> result) {
                mAdapter.clear();
                mAdapter.addAll(result.result);
            }

            @Override
            public void onFail(Request request, IOException exception) {

            }
        });
    }

}
