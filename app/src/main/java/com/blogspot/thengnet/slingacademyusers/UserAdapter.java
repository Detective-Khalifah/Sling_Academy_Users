package com.blogspot.thengnet.slingacademyusers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.blogspot.thengnet.slingacademyusers.databinding.UserBinding;

import java.util.List;

import androidx.databinding.DataBindingUtil;

public class UserAdapter extends ArrayAdapter<User> {

    private LayoutInflater inflater;
    private Context mAppContext;

    protected UserBinding binder;

    public UserAdapter (Context context, List<User> objects) {
        super(context, 0, objects);
        mAppContext = context;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = ((Activity) mAppContext).getLayoutInflater();

        User currentUser = getItem(position);

        binder = DataBindingUtil.getBinding(convertView);
        if (binder == null)
            binder = DataBindingUtil.inflate(inflater, R.layout.user, parent, false);

        binder.setUser(currentUser);
        binder.executePendingBindings();

        return binder.getRoot();
    }
}
