package com.begentgroup.miniapplication.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.begentgroup.miniapplication.MainActivity;
import com.begentgroup.miniapplication.R;
import com.begentgroup.miniapplication.manager.NetworkManager;
import com.begentgroup.miniapplication.manager.PropertyManager;

import java.io.IOException;

import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FacebookSignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FacebookSignUpFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FACEBOOK_INFO = "facebookInfo";

    // TODO: Rename and change types of parameters
    private FacebookInfo mfacebookInfo;


    public FacebookSignUpFragment() {
        // Required empty public constructor
    }

    public static FacebookSignUpFragment newInstance(FacebookInfo info) {
        FacebookSignUpFragment fragment = new FacebookSignUpFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FACEBOOK_INFO, info);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mfacebookInfo = (FacebookInfo)getArguments().getSerializable(ARG_FACEBOOK_INFO);
        }
    }

    EditText nameView, emailView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_facebook_sign_up, container, false);
        nameView = (EditText)view.findViewById(R.id.edit_name);
        emailView = (EditText)view.findViewById(R.id.edit_email);
        nameView.setText(mfacebookInfo.name);
        emailView.setText(mfacebookInfo.email);
        Button btn = (Button)view.findViewById(R.id.btn_sign_up);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameView.getText().toString();
                String email = emailView.getText().toString();
                NetworkManager.getInstance().facebookSignUp(getContext(), name, email,
                        new NetworkManager.OnResultListener<MyResult<User>>() {
                            @Override
                            public void onSuccess(Request request, MyResult<User> result) {
                                User user = (User)result.result;
                                // login success
                                PropertyManager.getInstance().setLogin(true);
                                PropertyManager.getInstance().setUser(user);
                                PropertyManager.getInstance().setFacebookId(user.facebookId);
                                //
                                startActivity(new Intent(getContext(), MainActivity.class));
                                getActivity().finish();
                            }

                            @Override
                            public void onFail(Request request, IOException exception) {

                            }
                        });
            }
        });
        return view;
    }

}
