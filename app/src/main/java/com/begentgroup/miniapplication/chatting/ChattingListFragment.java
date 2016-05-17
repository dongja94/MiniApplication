package com.begentgroup.miniapplication.chatting;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.begentgroup.miniapplication.R;
import com.begentgroup.miniapplication.manager.DataConstant;
import com.begentgroup.miniapplication.manager.DataManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChattingListFragment extends Fragment {


    public ChattingListFragment() {
    }

    ListView listView;
    SimpleCursorAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] from = {DataConstant.ChatUserTable.COLUMN_NAME, DataConstant.ChatUserTable.COLUMN_EMAIL};
        int[] to = {R.id.text_name, R.id.text_email};
        mAdapter = new SimpleCursorAdapter(getContext(), R.layout.view_chat_list, null, from, to, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatting_list, container, false);
        listView = (ListView)view.findViewById(R.id.listView);
        listView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Cursor c = DataManager.getInstance().getChatUserList();
        mAdapter.changeCursor(c);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.changeCursor(null);
    }
}
