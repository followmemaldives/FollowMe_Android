package com.asif.followme;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asif.followme.MyAccount.MyActivity;
import com.asif.followme.imageloader.ImageLoader;
import com.asif.followme.model.DataMyTrip;
import com.asif.followme.util.SettingsPreferences;

import java.util.List;

/**
 * Created by user on 1/1/2018.
 */

public class MyTripAdapter extends BaseAdapter
{
    private static List<DataMyTrip> searchArrayList;

    private LayoutInflater mInflater;
    MyTripAdapter custom = this;
    Context con;
    ImageLoader imageLoader;
    double v_min=360.0;
    double v_max=432.0;
    double v_diff = v_max-v_min;
    Activity activity;



    public MyTripAdapter(Context context, List<DataMyTrip> results)
    {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
        con  = context;
        imageLoader		=	new ImageLoader(context);
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
        //accountType = SettingsPreferences.getMyNavigationIndex(con);
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.my_trip_plan_item, null);
//            int accountType = SettingsPreferences.getSelectedAccount(con);
            holder = new ViewHolder();
            holder.txtname = (TextView) convertView.findViewById(R.id.name);
            holder.txtdesc = (TextView) convertView.findViewById(R.id.description);
            holder.imageView = (ImageView)convertView.findViewById(R.id.imageview);
            holder.itemMore = (ImageView)convertView.findViewById(R.id.item_more);
            holder.txtdatetime = (TextView)convertView.findViewById(R.id.date_time);
            holder.timeAgo = (TextView)convertView.findViewById(R.id.time_ago);
            holder.captain_status = (TextView)convertView.findViewById(R.id.captain_status);
            holder.trip_status = (TextView)convertView.findViewById(R.id.trip_status);
            //if(accountType==0){	//hide item more button, if public
            //holder.itemMore.setVisibility(View.INVISIBLE);
            //	holder.itemMore.setImageResource(R.drawable.ic_action_email);
            //}
            convertView.setTag(holder);

            //me
            //convertView.setClickable(true);
            //convertView.setFocusable(true);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        //accountType = SettingsPreferences.getSelectedAccount(con);
        holder.txtname.setText(searchArrayList.get(position).getName());
        holder.txtdesc.setText(searchArrayList.get(position).getValue());
        holder.txtdatetime.setText(searchArrayList.get(position).getDate());
        //holder.captain_status.setTextColor(Color.parseColor("#00AA00"));
        String trip_st = searchArrayList.get(position).getTripStatus();
        if(trip_st.equalsIgnoreCase("CONFIRMED")){
            System.out.println("Name: "+searchArrayList.get(position).getName()+",Trip Status:"+trip_st);
            holder.trip_status.setTextColor(Color.parseColor("#00AA00"));
        } else
        if(trip_st.equalsIgnoreCase("CANCELLED")){
            holder.trip_status.setTextColor(Color.parseColor("#AA0000"));
        } else {
            holder.trip_status.setTextColor(Color.parseColor("#555555"));

        }

        holder.trip_status.setText(trip_st);

        imageLoader.DisplayImage(searchArrayList.get(position).getImage(), holder.imageView, (ProgressBar)convertView.findViewById(R.id.progres_bar));
        //ImageButton button = (ImageButton)v.findViewById( R.id.item_more);
        holder.itemMore.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                       // MyActivity.selectMenuItem mi = new selectMenuItem(searchArrayList.get(position).getId());
                       // MyActivity.mi = new MyActivity.selectMenuItem();
                       //MyActivity.selectMenuItem(searchArrayList.get(position).getId());

                        //Get_list.popupMenu.show();
                     //   MyActivity.shared_vessel_id=searchArrayList.get(position).getId();
                        SettingsPreferences.setSelectedItemID(con, searchArrayList.get(position).getId());
                      //  MyActivity.shared_vessel=searchArrayList.get(position).getName();
                     //   MyActivity.selected_fleet_id=searchArrayList.get(position).getId();
                     //   MyActivity.selected_fleet_name=searchArrayList.get(position).getName();
                        MyActivity.selected_item_name =searchArrayList.get(position).getName();
                        MyActivity.selected_item_id = searchArrayList.get(position).getId();
                        //MyActivity.selectMenuItem(accountType);
                    }
                });

        //holder.imageView.setImageBitmap(Utilities.getImage(searchArrayList.get(position).getImage()));
		/*Bitmap bitmap = BitmapFactory.decodeFile(searchArrayList.get(position).getImage());
		holder.imageView.setImageBitmap(bitmap);*/
        return convertView;
    }

    static class ViewHolder
    {
        TextView txtname;
        TextView txtdesc;
        ImageView imageView;
        ImageView itemMore;
        TextView txtdatetime;
        TextView timeAgo;
        TextView trip_status;
        TextView captain_status;
    }


}
