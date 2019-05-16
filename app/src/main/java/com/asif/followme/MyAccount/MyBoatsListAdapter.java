package com.asif.followme.MyAccount;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.asif.followme.PhotoActivity;
import com.asif.followme.R;
import com.asif.followme.imageloader.ImageLoader;
import com.asif.followme.model.DataMyBoats;
import com.asif.followme.util.AppConstants;
import com.asif.followme.util.SettingsPreferences;

import java.util.List;

public class MyBoatsListAdapter extends BaseAdapter
{
    private static List<DataMyBoats> searchArrayList;

    private LayoutInflater mInflater;
//    MyGroupAdapter custom = this;
    Context con;
    ImageLoader imageLoader;
//    int accountType;
    String nav_item;
    double v_min=360.0;
    double v_max=432.0;
    double v_diff = v_max-v_min;
    Activity activity;
    MyBoatsFragment fragment;
    String my_tracker_id;


    public MyBoatsListAdapter(Context context, List<DataMyBoats> results, MyBoatsFragment fragment)
    {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
        con  = context;
        imageLoader		=	new ImageLoader(context);
        this.fragment = fragment;
        my_tracker_id = SettingsPreferences.getMyTracker(context);
        if(MyBoatsFragment.switchItem!=null) {
            MyBoatsFragment.switchItem.setVisible(false);
        }
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
            convertView = mInflater.inflate(R.layout.my_boat_list_item, null);
//            int accountType = SettingsPreferences.getSelectedAccount(con);
            holder = new ViewHolder();
            holder.txtname = (TextView) convertView.findViewById(R.id.name);
            holder.txtvalue = (TextView) convertView.findViewById(R.id.description);
            holder.imageView = (ImageView)convertView.findViewById(R.id.imageview);
            holder.itemMore = (ImageView)convertView.findViewById(R.id.item_more);
            holder.vidMore = (TextView)convertView.findViewById(R.id.vid);
            holder.timeAgo = (TextView)convertView.findViewById(R.id.time_ago);
            holder.imgBatt = (ImageView)convertView.findViewById(R.id.img_batt);
            holder.imgStop = (ImageView)convertView.findViewById(R.id.img_stop);
            holder.txtIsland = (TextView) convertView.findViewById(R.id.txt_island);
            holder.txtSpeed = (TextView) convertView.findViewById(R.id.txt_speed);
            holder.contactBox = (LinearLayout) convertView.findViewById(R.id.contact_box);
            holder.txtContact = (TextView) convertView.findViewById(R.id.contact_number);
            holder.expiredBox = (LinearLayout) convertView.findViewById(R.id.expired_box);
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
        nav_item = SettingsPreferences.getMyNavigationName(con);
        String client = searchArrayList.get(position).getValue();
        if(!client.equalsIgnoreCase("")){client=" -  "+client;}
        holder.txtname.setText(searchArrayList.get(position).getName());
        //String[] b = searchArrayList.get(position).getBatt().split(":");
        int batt = searchArrayList.get(position).getBatt();
//		if(accountType==0 && b.length>2){
//        if(nav_item.equalsIgnoreCase("nav_my_vessels")){
            switch(batt){
                case 200:
                    holder.imgBatt.setImageResource(R.drawable.b100c);
                    break;
                case 100:
                    holder.imgBatt.setImageResource(R.drawable.b100);
                    break;
                case 90:
                    holder.imgBatt.setImageResource(R.drawable.b90);
                    break;
                case 80:
                    holder.imgBatt.setImageResource(R.drawable.b80);
                    break;
                case 70:
                    holder.imgBatt.setImageResource(R.drawable.b70);
                    break;
                case 60:
                    holder.imgBatt.setImageResource(R.drawable.b60);
                    break;
                case 50:
                    holder.imgBatt.setImageResource(R.drawable.b50);
                    break;
                case 40:
                    holder.imgBatt.setImageResource(R.drawable.b40);
                    break;
                case 30:
                    holder.imgBatt.setImageResource(R.drawable.b30);
                    break;
                case 20:
                    holder.imgBatt.setImageResource(R.drawable.b20);
                    break;
                case 10:
                    holder.imgBatt.setImageResource(R.drawable.b10);
                    break;
                default:
                    holder.imgBatt.setImageResource(R.drawable.b0);
                    break;
            }

 //           System.out.println("My Tracker ID:"+my_tracker_id+"********************************************");
            if(searchArrayList.get(position).getId().equalsIgnoreCase(my_tracker_id)){

                if(MyBoatsFragment.switchItem!=null) {
                    MyBoatsFragment.switchItem.setVisible(true);
                }
//                MyBoatsFragment.switchItem.setVisible(true);
            }
            holder.imgBatt.setVisibility(View.VISIBLE);
//			holder.itemMore.setImageResource(R.drawable.ic_action_email);
            int moveIcon = searchArrayList.get(position).getMarker();
            holder.txtvalue.setText("ID: "+searchArrayList.get(position).getId()+" "+client);
            holder.timeAgo.setText(searchArrayList.get(position).getDate());
            holder.txtIsland.setText(searchArrayList.get(position).getIsland());
            //	holder.txtSpeed.setText(searchArrayList.get(position).getSpeed());
            switch(moveIcon){
                case 1:
                    holder.imgStop.setImageResource(R.drawable.stop_green);
                    break;
                case 2:
                    holder.imgStop.setImageResource(R.drawable.arrow_green_s);
                    break;
                case 3:
                    holder.imgStop.setImageResource(R.drawable.stop_red);
                    break;
                case 4:
                    holder.imgStop.setImageResource(R.drawable.arrow_red_s);
                    break;
            }
        String tel = searchArrayList.get(position).getContact();
        if(tel.length()>6) {
            holder.contactBox.setVisibility(View.VISIBLE);
            holder.txtContact.setText(searchArrayList.get(position).getContact());
        } else {
            holder.contactBox.setVisibility(View.GONE);
        }
        if(searchArrayList.get(position).getIsExpired() > 0){
            holder.expiredBox.setVisibility(View.VISIBLE);
        } else {
            holder.expiredBox.setVisibility(View.GONE);
        }
            //	holder.imgCharge.setVisibility(View.VISIBLE);
/*        } else {
            //holder.imgBatt.setImageDrawable(null);
            holder.imgBatt.setVisibility(View.GONE);
            //holder.imgCharge.setVisibility(View.GONE);
            if(position==0){
                holder.txtvalue.setText(searchArrayList.get(position).getValue());
                holder.itemMore.setVisibility(View.GONE);
            } else {
                holder.txtvalue.setText(searchArrayList.get(position).getValue()+" devices ");
                holder.itemMore.setVisibility(View.VISIBLE);
            }
        }
        */
        String  imageUrl=AppConstants.IMAGE_SMALL_URL+searchArrayList.get(position).getImage();

        imageLoader.DisplayImage(imageUrl, holder.imageView, (ProgressBar)convertView.findViewById(R.id.progres_bar));
        //ImageButton button = (ImageButton)v.findViewById( R.id.item_more);
        holder.itemMore.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        MyBoatsFragment.selected_item_id = searchArrayList.get(position).getId();
                        MyBoatsFragment.selected_item_name = searchArrayList.get(position).getName();
                        MyActivity.selected_item_id = searchArrayList.get(position).getId();
                        MyActivity.selected_item_name = searchArrayList.get(position).getName();
                        SettingsPreferences.setSelectedItemID(con, searchArrayList.get(position).getId());
                        fragment.selectMenuItem(searchArrayList.get(position).getId());
                    }
                });
        holder.contactBox.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        String phone = searchArrayList.get(position).getContact();
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                        //Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + searchArrayList.get(position).getContact()));
                        con.startActivity(intent);                    }
                });
//        if(nav_item.equalsIgnoreCase("nav_my_vessels")){
            holder.imageView.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            //Get_list.popupMenu.show();

                            //PublicList.showDialog(con,searchArrayList.get(position).getImage());
//                            PublicActivity.selectedImage = searchArrayList.get(position).getImage();
                            PhotoActivity.selectedImage = searchArrayList.get(position).getImage();
                            SettingsPreferences.setSelectedItemID(con, searchArrayList.get(position).getId());
//						SettingsPreferences.setItemId(con, searchArrayList.get(position).getId());
                            Intent intent = new Intent(con, PhotoActivity.class);
                            intent.putExtra("account","my");
                            con.startActivity(intent);

                        }
                    });
//        }
        //holder.imageView.setImageBitmap(Utilities.getImage(searchArrayList.get(position).getImage()));
        return convertView;
    }

    static class ViewHolder
    {
        TextView txtname;
        TextView txtvalue;
        ImageView imageView;
        ImageView itemMore;
        TextView vidMore;
        TextView timeAgo;
        ImageView imgBatt;
        ImageView imgStop;
        TextView txtIsland;
        TextView txtSpeed;
        LinearLayout contactBox;
        TextView txtContact;
        LinearLayout expiredBox;
    }

}
