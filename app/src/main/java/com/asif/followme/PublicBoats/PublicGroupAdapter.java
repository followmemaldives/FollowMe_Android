package com.asif.followme.PublicBoats;

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

import java.util.List;


/**
 * Created by user on 12/18/2017.
 */

public class PublicGroupAdapter extends BaseAdapter {

    private static List<DataPublicGroups> searchArrayList;
    private LayoutInflater mInflater;
//    PublicBoatsAdapter custom = this;
    Context con;
    ImageLoader imageLoader;
    int accountType;


    public PublicGroupAdapter(Context context, List<DataPublicGroups> results, PublicGroupFragment fragment)
    {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
        con  = context;
        imageLoader		=	new ImageLoader(context);
    }
    public Object getItem(int position)
    {
        return searchArrayList.get(position);
    }
    public long getItemId(int position)
    {
        return position;
    }
    public int getCount()
    {
        return searchArrayList.size();
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.public_group_item, null);

            holder = new ViewHolder();
            holder.txtname = (TextView) convertView.findViewById(R.id.name);
            holder.txtcount = (TextView) convertView.findViewById(R.id.count);
            holder.imageView = (ImageView)convertView.findViewById(R.id.imageview);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtname.setText(searchArrayList.get(position).getName());
        holder.txtcount.setText(searchArrayList.get(position).getCount()+" devices ");
        imageLoader.DisplayImage(searchArrayList.get(position).getImage(), holder.imageView, (ProgressBar)convertView.findViewById(R.id.progres_bar));

        return convertView;

    }
    static class ViewHolder
    {
        TextView txtname;
        ImageView imageView;
        TextView txtcount;

    }

}
