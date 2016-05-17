package com.begentgroup.miniapplication.chatting;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.begentgroup.miniapplication.login.User;

/**
 * Created by dongja94 on 2016-05-17.
 */
public class FriendViewHolder extends RecyclerView.ViewHolder {
    public interface OnItemClickListener {
        public void onItemClick(User user);
    }
    OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    TextView textView;
    public FriendViewHolder(View itemView) {
        super(itemView);
        textView = (TextView)itemView;
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(user);
                }
            }
        });
    }

    User user;
    public void setUser(User user) {
        this.user = user;
        textView.setText(user.userName + "(" + user.email + ")");
    }
}
