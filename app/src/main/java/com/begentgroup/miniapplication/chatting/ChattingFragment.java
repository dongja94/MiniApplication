package com.begentgroup.miniapplication.chatting;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.begentgroup.miniapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChattingFragment extends Fragment {


    public ChattingFragment() {
        // Required empty public constructor
    }

    FragmentTabHost tabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatting, container, false);
        tabHost = (FragmentTabHost)view.findViewById(R.id.tabhost);
        tabHost.setup(getContext(), getChildFragmentManager(), android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec("friend").setIndicator("Friend"), ChattingFriendFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("chatlist").setIndicator("Chat List"), ChattingListFragment.class, null);
        return view;
    }

}
