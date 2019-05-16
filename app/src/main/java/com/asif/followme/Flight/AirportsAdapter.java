package com.asif.followme.Flight;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.asif.followme.R;
import com.asif.followme.imageloader.ImageLoader;

import java.util.List;


/**
 * Created by user on 12/18/2017.
 */

public class AirportsAdapter extends BaseAdapter {

    private static List<DataAirports> searchArrayList;
    private LayoutInflater mInflater;
//    PublicBoatsAdapter custom = this;
    Context con;
    ImageLoader imageLoader;
    int accountType;


    public AirportsAdapter(Context context, List<DataAirports> results, AirportFragment fragment)
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
        //accountType = SettingsPreferences.getSelectedPublicAccount(con);
        //View view = View.inflate(this, R.layout.list_item, null);
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.flight_airport_item, null);
            //    int accountType = SettingsPreferences.getSelectedPublicAccount(con);
            holder = new ViewHolder();
            holder.txtname = (TextView) convertView.findViewById(R.id.name);
            holder.txtvalue = (TextView) convertView.findViewById(R.id.description);
            holder.imageView = (ImageView)convertView.findViewById(R.id.imageview);
			holder.itemMore = (ImageView)convertView.findViewById(R.id.item_more);
            holder.itemStar = (ImageView)convertView.findViewById(R.id.item_star);
            holder.vidMore = (TextView)convertView.findViewById(R.id.vid);
            holder.timeAgo = (TextView)convertView.findViewById(R.id.time_ago);
//			holder.imgBatt = (ImageView)convertView.findViewById(R.id.img_batt);
            holder.itemMail = (ImageView)convertView.findViewById(R.id.item_mail);
            holder.imgStop = (ImageView)convertView.findViewById(R.id.img_stop);
            holder.txtIslandSpeed = (TextView) convertView.findViewById(R.id.txt_island);
            holder.txtSpeed = (TextView) convertView.findViewById(R.id.txt_speed);
            holder.FavCount = (TextView) convertView.findViewById(R.id.fav_count);
            holder.starBox = (LinearLayout) convertView.findViewById(R.id.star_box);
            holder.contactBox = (LinearLayout) convertView.findViewById(R.id.contact_box);
            holder.txtContact = (TextView) convertView.findViewById(R.id.contact_number);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtname.setText(searchArrayList.get(position).getNameAirport());
        return convertView;

    }
    static class ViewHolder
    {
        TextView txtname;
        TextView txtvalue;
        ImageView imageView;
        //		ImageView itemMore;
        ImageView itemStar;
        TextView vidMore;
        TextView timeAgo;
        ImageView itemMore;
        ImageView imgStop;
        TextView txtIslandSpeed;
        TextView txtSpeed;
        ImageView itemMail;
        TextView FavCount;
        LinearLayout starBox;
        LinearLayout contactBox;
        TextView txtContact;
    }

}
