package com.asif.followme.PublicBoats;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.asif.followme.*;
import com.asif.followme.imageloader.ImageLoader;
import com.asif.followme.model.DataPublicBoats;
import com.asif.followme.util.SettingsPreferences;

import java.util.List;


/**
 * Created by user on 12/18/2017.
 */

public class PublicBoatsAdapter extends BaseAdapter {

    private static List<DataPublicBoats> searchArrayList;
    private LayoutInflater mInflater;
//    PublicBoatsAdapter custom = this;
    Context con;
    ImageLoader imageLoader;
    int accountType;
    PublicBoatsFragment fragment;


    public PublicBoatsAdapter(Context context, List<DataPublicBoats> results, PublicBoatsFragment fragment)
    {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
        con  = context;
        imageLoader		=	new ImageLoader(context);
        this.fragment = fragment;
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
        accountType = SettingsPreferences.getSelectedPublicAccount(con);
        //View view = View.inflate(this, R.layout.list_item, null);
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.public_list_item, null);
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
    //    String clientx = searchArrayList.get(position).getValue();
    //    String client = clientx.equalsIgnoreCase("null") ? "" : " - "+clientx;

        //if(client != null){client=" -  "+client;}
        holder.txtname.setText(searchArrayList.get(position).getName());
        //String[] b = searchArrayList.get(position).getBatt().split(":");
//		int batt = searchArrayList.get(position).getBatt();
            if(searchArrayList.get(position).getNotice()==0){
                holder.itemMail.setVisibility(View.GONE);
            } else {
                holder.itemMail.setVisibility(View.VISIBLE);
            }

            int moveIcon = searchArrayList.get(position).getMarker();
            holder.txtvalue.setText("ID: "+searchArrayList.get(position).getId());
            holder.timeAgo.setText(searchArrayList.get(position).getDate());
            holder.txtIslandSpeed.setText(searchArrayList.get(position).getIslandSpeed());
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
            if(searchArrayList.get(position).getFav()==1){
                holder.itemStar.setImageResource(R.drawable.star1);
            } else {
                holder.itemStar.setImageResource(R.drawable.star0);
            }
            holder.FavCount.setText(searchArrayList.get(position).getFavCount());
            String tel = searchArrayList.get(position).getContact();
            if(tel.length()>6) {
                holder.contactBox.setVisibility(View.VISIBLE);
                holder.txtContact.setText(searchArrayList.get(position).getContact());
            } else {
                holder.contactBox.setVisibility(View.GONE);
            }
            //	System.out.println("FAV:"+searchArrayList.get(position).getFav());
            //	holder.imgCharge.setVisibility(View.VISIBLE);

       // new DownloadImageTask(holder.imageView).execute(searchArrayList.get(position).getImage());
       //System.out.println(searchArrayList.get(position).getImage());
        imageLoader.DisplayImage(searchArrayList.get(position).getImage(), holder.imageView, (ProgressBar)convertView.findViewById(R.id.progres_bar));
        //ImageButton button = (ImageButton)v.findViewById( R.id.item_more);
        holder.starBox.setOnClickListener(
                new OnClickListener() {
                    public void onClick(View v) {
                        System.out.println("You clicked the star...............................................");
                        int fav_total =Integer.parseInt(searchArrayList.get(position).getFavCount());
                        //Get_list.popupMenu.show();
                        PublicBoatsFragment.selected_item_id=searchArrayList.get(position).getId();
                        SettingsPreferences.setSelectedItemID(con, searchArrayList.get(position).getId());
                        //	PublicList.myAlert.setTitle(searchArrayList.get(position).getName());
                        //	PublicList.myAlert.show();
                        //System.out.println("ID"+PublicList.selected_vessel_id+","+searchArrayList.get(position).getFav());
                        if(searchArrayList.get(position).getFav()==1){
                            //searchArrayList.set(position, object)
                            searchArrayList.get(position).setFav(0);
                            holder.itemStar.setImageResource(R.drawable.star0);
                            holder.FavCount.setText(String.valueOf(fav_total-1));
                        } else {
                            searchArrayList.get(position).setFav(1);
                            holder.itemStar.setImageResource(R.drawable.star1);
                            holder.FavCount.setText(String.valueOf(fav_total+1));
                        }
                        searchArrayList.get(position).setFavCount(holder.FavCount.getText().toString());
                        PublicActivity.updateFavourites(con,searchArrayList.get(position).getId(),searchArrayList.get(position).getFav());
                    }
                });
        holder.itemMail.setOnClickListener(
                new OnClickListener() {
                    public void onClick(View v) {
                        //Get_list.popupMenu.show();
                        PublicBoatsFragment.selected_item_id=searchArrayList.get(position).getId();
                        SettingsPreferences.setSelectedItemID(con, searchArrayList.get(position).getId());
                        Intent intent = new Intent(con, PublicNoticeActivity.class);
                        intent.putExtra("v_name", searchArrayList.get(position).getName());
                        con.startActivity(intent);

                        //	PublicList.myAlert.setTitle(searchArrayList.get(position).getName());
                        //	PublicList.myAlert.show();
                        //System.out.println("ID"+PublicList.selected_vessel_id);
//                        PublicActivity.showNotice(con,searchArrayList.get(position).getId(),searchArrayList.get(position).getName());
                    }
                });
        holder.contactBox.setOnClickListener(
                new OnClickListener() {
                    public void onClick(View v) {
                        String phone = searchArrayList.get(position).getContact();
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                        //Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + searchArrayList.get(position).getContact()));
                        con.startActivity(intent);                    }
                });
        holder.imageView.setOnClickListener(
                new OnClickListener() {
                    public void onClick(View v) {
                        if(accountType!=0 && accountType!=2){
                            return;
                        }
                        //Get_list.popupMenu.show();

                        //PublicList.showDialog(con,searchArrayList.get(position).getImage());
                        PhotoActivity.selectedImage = searchArrayList.get(position).getImage();
                        SettingsPreferences.setSelectedItemID(con, searchArrayList.get(position).getId());
//						SettingsPreferences.setItemId(con, searchArrayList.get(position).getId());
                        Intent intent = new Intent(con, PhotoActivity.class);
                        intent.putExtra("account","public");
                        con.startActivity(intent);

                    }
                });
        holder.itemMore.setOnClickListener(
                new View.OnClickListener() {
            public void onClick(View v) {
                SettingsPreferences.setSelectedItemID(con, searchArrayList.get(position).getId());
                SettingsPreferences.setSelectedItemName(con,searchArrayList.get(position).getName());
                fragment.selected_item_name =searchArrayList.get(position).getName();
                fragment.selected_item_id = searchArrayList.get(position).getId();
                fragment.currentIsland = searchArrayList.get(position).getIsland();
                fragment.selectMenuItem(accountType);

            }
        });
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
