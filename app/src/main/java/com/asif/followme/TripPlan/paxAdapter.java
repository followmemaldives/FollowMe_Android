package com.asif.followme.TripPlan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.asif.followme.R;
import com.asif.followme.imageloader.ImageLoader;

import java.util.List;

public class paxAdapter extends BaseAdapter
{
	private static List<DataPax> searchArrayList;

	private LayoutInflater mInflater;
	paxAdapter custom = this;
	Context con;
	ImageLoader					imageLoader;
	PassengerList activity;
	public paxAdapter(Context context, List<DataPax> results, PassengerList activity)
	{
		searchArrayList = results;
		mInflater = LayoutInflater.from(context);
		con  = context;
		imageLoader		=	new ImageLoader(context);
		this.activity = activity;
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
			convertView = mInflater.inflate(R.layout.trip_pax_list_item, null);
			holder = new ViewHolder();
			holder.pax_name = (TextView) convertView.findViewById(R.id.pax_name);
			holder.route_name = (TextView) convertView.findViewById(R.id.route_name);
			holder.contact_box = (LinearLayout) convertView.findViewById(R.id.contact_box);
			holder.contact_number = (TextView) convertView.findViewById(R.id.contact_number);
			holder.booking_status = (TextView) convertView.findViewById(R.id.booking_status);

			holder.imageView = (ImageView)convertView.findViewById(R.id.imageview);
			//holder.itemRemove = (ImageView)convertView.findViewById(R.id.share_item_remove);
			holder.itemMore = (ImageView)convertView.findViewById(R.id.item_more);
			convertView.setTag(holder);
			
			//me
			//convertView.setClickable(true);
			//convertView.setFocusable(true);
		} 
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.pax_name.setText(searchArrayList.get(position).getName());
		String route = searchArrayList.get(position).getFrom()+" > "+searchArrayList.get(position).getTo();
		holder.route_name.setText(route);
		String contact = searchArrayList.get(position).getContact();
		if(contact.equals("")){
			holder.contact_box.setVisibility(View.GONE);
		} else {
			holder.contact_number.setText(contact);
			holder.contact_box.setVisibility(View.VISIBLE);
		}
		holder.booking_status.setText(searchArrayList.get(position).getBookingStatusStr());
		String image = "http://followme.mv/images/profile/"+searchArrayList.get(position).getImage();
		System.out.println("Image:"+image);
		imageLoader.DisplayImage(image, holder.imageView, (ProgressBar)convertView.findViewById(R.id.progres_bar));

		holder.itemMore.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View v) {
                    //PassengerList.selected_pax_id = searchArrayList.get(position).getPaxId();
                    activity.selectMenuItem(con, searchArrayList.get(position).getPaxId());
//					SettingsPreferences.setSelectedItemID(con, searchArrayList.get(position).getId());
                //  fragment.selectMenuItem(searchArrayList.get(position).getId());
            }
		});


//		holder.txtvalue.setText(searchArrayList.get(position).getValue());
//		imageLoader.DisplayImage(searchArrayList.get(position).getImage(), holder.imageView, (ProgressBar)convertView.findViewById(R.id.progres_bar));
		//ImageButton button = (ImageButton)v.findViewById( R.id.item_more);
/*		holder.itemRemove.setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						//Get_list.popupMenu.show();
						SettingsPreferences.setSelectedSharedUser(con, searchArrayList.get(position).getId());
						ShareList.shared_user_id=searchArrayList.get(position).getId();
						ShareList.shareAlert.setMessage("Remove the user '"+searchArrayList.get(position).getName()+"' from shared list?");
						ShareList.shareAlert.show();
					}
		});
		holder.itemEdit.setOnClickListener(
			new OnClickListener() {
				public void onClick(View v) {
					SettingsPreferences.setSelectedSharedUser(con, searchArrayList.get(position).getId());

					Intent intent = new Intent(con, ShareFormActivity.class);
					intent.putExtra("vname",searchArrayList.get(position).getName());
					intent.putExtra("user_id",searchArrayList.get(position).getId());
					intent.putExtra("user_email",searchArrayList.get(position).getValue());
					con.startActivity(intent);
					//showEditDialog();
			        //Get_shared_list.shareDialog.show();
				}
		});
		*/
		return convertView;
	}

/*	protected static void showEditDialog() {
		// TODO Auto-generated method stub
		Get_shared_list sss = new Get_shared_list();
		sss.showEditDialog();
		
	}
*/
	static class ViewHolder
	{
		TextView pax_name;
		TextView route_name;
		ImageView imageView;
		ImageView itemRemove;
		ImageView itemMore;
		LinearLayout contact_box;
		TextView contact_number;
		TextView booking_status;
	}	
	

}
