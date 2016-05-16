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
 */
public class SignUpFragment extends Fragment {


    public SignUpFragment() {
        // Required empty public constructor
    }

    EditText nameView, emailView, passwordView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        nameView = (EditText)view.findViewById(R.id.edit_name);
        emailView = (EditText)view.findViewById(R.id.edit_email);
        passwordView = (EditText)view.findViewById(R.id.edit_password);
        Button btn = (Button)view.findViewById(R.id.btn_sign_up);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameView.getText().toString();
                final String email = emailView.getText().toString();
                final String password = passwordView.getText().toString();
                NetworkManager.getInstance().signup(getContext(), name, email, password, "", new NetworkManager.OnResultListener<MyResultUser>() {
                    @Override
                    public void onSuccess(Request request, MyResultUser result) {
                        if (result.code == 1) {
                            PropertyManager.getInstance().setLogin(true);
                            PropertyManager.getInstance().setUser(result.result);
                            PropertyManager.getInstance().setEmail(email);
                            PropertyManager.getInstance().setPassword(password);
                            startActivity(new Intent(getContext(), MainActivity.class));
                            getActivity().finish();
                        }
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
