package com.asif.followme;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by user on 1/6/2018.
 */

public class MyGroupMenuAdapter extends ArrayAdapter<MyGroupMenuActivity.MyGroupMenu> {

    private final List<MyGroupMenuActivity.MyGroupMenu> list;
    private final Activity context;

    static class ViewHolder {
        protected TextView name;
        protected ImageView flag;
    }

    public MyGroupMenuAdapter(Activity context, List<MyGroupMenuActivity.MyGroupMenu> list) {
        super(context, R.layout.my_menu_row, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.my_menu_row, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.my_menu_name);
            viewHolder.flag = (ImageView) view.findViewById(R.id.my_menu_icon);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(list.get(position).getName());
        holder.flag.setImageDrawable(list.get(position).getFlag());
        return view;
    }
}
