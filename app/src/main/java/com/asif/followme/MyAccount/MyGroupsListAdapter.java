package com.asif.followme.MyAccount;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.asif.followme.R;
import com.asif.followme.imageloader.ImageLoader;
import com.asif.followme.util.SettingsPreferences;

import java.util.List;

/**
 * Created by user on 1/1/2018.
 */

public class MyGroupsListAdapter extends BaseAdapter
{
    private static List<DataMyGroups> searchArrayList;

    private LayoutInflater mInflater;
    MyGroupsListAdapter custom = this;
    Context con;
    ImageLoader imageLoader;
    Activity activity;
    MyGroupsFragment fragment;



    public MyGroupsListAdapter(Context context, List<DataMyGroups> results, MyGroupsFragment fragment) {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
        con  = context;
        imageLoader		=	new ImageLoader(context);
        this.fragment = fragment;
    }

    public int getCount()
    {
        return searchArrayList.size();
    }

    public Object getItem(int position)
    {
        return searchArrayList.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }


    public View getView(final int position, View convertView, final ViewGroup parent)
    {
        final ViewHolder holder;
        //View view = View.inflate(this, R.layout.list_item, null);
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.my_group_list_item, null);
//            int accountType = SettingsPreferences.getSelectedAccount(con);
            holder = new ViewHolder();
            holder.txtname = (TextView) convertView.findViewById(R.id.name);
            holder.txtCount = (TextView) convertView.findViewById(R.id.description);
            holder.imageView = (ImageView)convertView.findViewById(R.id.imageview);
            holder.itemMore = (ImageView)convertView.findViewById(R.id.item_more);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtname.setText(searchArrayList.get(position).getName());
        holder.txtCount.setText(searchArrayList.get(position).getCount());
        if(position==0){
            holder.itemMore.setVisibility(View.GONE);
        } else {
            holder.itemMore.setVisibility(View.VISIBLE);
        }

        imageLoader.DisplayImage(searchArrayList.get(position).getImage(), holder.imageView, (ProgressBar)convertView.findViewById(R.id.progres_bar));
        holder.itemMore.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View v) {
                    MyGroupsFragment.selected_item_id = searchArrayList.get(position).getId();
                    MyGroupsFragment.selected_item_name = searchArrayList.get(position).getName();
                    SettingsPreferences.setSelectedItemID(con, searchArrayList.get(position).getId());
                    fragment.selectMenuItem(searchArrayList.get(position).getId());
                }
            });
        return convertView;
    }

    static class ViewHolder
    {
        TextView txtname;
        TextView txtCount;
        ImageView imageView;
        ImageView itemMore;
    }


}
