package com.begentgroup.miniapplication.tstore;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabWidget;
import android.widget.TextView;

import com.begentgroup.miniapplication.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TStoreFragment extends Fragment {


    public TStoreFragment() {
        // Required empty public constructor
    }


    FragmentTabHost tabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tstore, container, false);
        tabHost = (FragmentTabHost)view.findViewById(R.id.tabhost);
        tabHost.setup(getContext(), getChildFragmentManager(), android.R.id.tabcontent);

        TabWidget tabWidget = (TabWidget)tabHost.findViewById(android.R.id.tabs);

        View tabHeader = inflater.inflate(R.layout.tab_header, tabWidget, false);
        TextView titleView = (TextView)tabHeader.findViewById(R.id.text_title);
        titleView.setText(R.string.tstore_tab_category_title);
        tabHost.addTab(tabHost.newTabSpec("category").setIndicator(tabHeader), TStoreCategoryFragment.class, null);
//        tabHost.addTab(tabHost.newTabSpec("category").setIndicator(getString(R.string.tstore_tab_category_title)), TStoreCategoryFragment.class, null);

        tabHeader = inflater.inflate(R.layout.tab_header, tabWidget, false);
        titleView = (TextView)tabHeader.findViewById(R.id.text_title);
        titleView.setText(R.string.tstore_tab_search_title);
        tabHost.addTab(tabHost.newTabSpec("search").setIndicator(tabHeader), TStoreSearchFragment.class, null);
//        tabHost.addTab(tabHost.newTabSpec("search").setIndicator(getString(R.string.tstore_tab_search_title)), TStoreSearchFragment.class, null);
        return view;
    }

}
