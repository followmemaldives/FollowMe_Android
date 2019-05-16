package com.asif.followme.BoatHire.Operator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.asif.followme.BoatHire.DataBids;
import com.asif.followme.R;
import com.asif.followme.imageloader.ImageLoader;

import java.util.List;

public class MyBidAdapter extends BaseAdapter
{
	private static List<DataBids> searchArrayList;

	private LayoutInflater mInflater;
	MyBidAdapter custom = this;
	Context con;
	ImageLoader imageLoader;
	final int BID_DONE = 1;
	final int BID_AWARDED = 2;
	final int BID_ACCEPTED = 3;
	final int BID_COMPLETED = 4;

	public MyBidAdapter(Context context, List<DataBids> results)
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
		if (convertView == null) 
		{
			convertView = mInflater.inflate(R.layout.my_bid_adapter, null);
			holder = new ViewHolder();
			holder.txtname = (TextView) convertView.findViewById(R.id.name);
			holder.txtContact = (TextView) convertView.findViewById(R.id.description);
			holder.imageView = (ImageView)convertView.findViewById(R.id.imageview);
		//	holder.txtBidCount = (TextView) convertView.findViewById(R.id.bid_count);
			holder.txtprice = (TextView) convertView.findViewById(R.id.bid_price);
			holder.bidAwardStatus = (TextView) convertView.findViewById(R.id.bid_award_status);
			holder.completedStatusBox = (LinearLayout) convertView.findViewById(R.id.completed_status_box);
//			holder.cancelBox = (LinearLayout) convertView.findViewById(R.id.cancel_box);
//			holder.acceptBox = (LinearLayout) convertView.findViewById(R.id.accept_box);
			holder.awardStatusBox = (LinearLayout) convertView.findViewById(R.id.award_status_box);
			holder.acceptStatusBox = (LinearLayout) convertView.findViewById(R.id.accept_status_box);
			holder.btnCancel = (Button) convertView.findViewById(R.id.btn_cancel);
			holder.btnAccept = (Button) convertView.findViewById(R.id.btn_accept);

//			holder.itemRemove = (ImageView)convertView.findViewById(R.id.share_item_remove);
//			holder.itemEdit = (ImageView)convertView.findViewById(R.id.share_item_edit);
			convertView.setTag(holder);
			
			//me
			//convertView.setClickable(true);
			//convertView.setFocusable(true);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtname.setText(searchArrayList.get(position).getName());	
		holder.txtContact.setText("Contact: "+searchArrayList.get(position).getContact());
		holder.txtprice.setText(searchArrayList.get(position).getPrice());
		holder.bidAwardStatus.setText(searchArrayList.get(position).getBidStatusText());

		holder.btnCancel.setVisibility(View.GONE);
		holder.btnAccept.setVisibility(View.GONE);
		holder.awardStatusBox.setVisibility(View.GONE);
		holder.acceptStatusBox.setVisibility(View.GONE);
		holder.completedStatusBox.setVisibility(View.GONE);

		if(searchArrayList.get(position).getBidStatus() == BID_AWARDED){
			holder.awardStatusBox.setVisibility(View.VISIBLE);
			holder.btnAccept.setVisibility(View.VISIBLE);
		}
		if(searchArrayList.get(position).getBidStatus() == BID_ACCEPTED){
			holder.acceptStatusBox.setVisibility(View.VISIBLE);
		}
		if(searchArrayList.get(position).getBidStatus() == BID_COMPLETED){
			holder.completedStatusBox.setVisibility(View.VISIBLE);
		}

/*		if(searchArrayList.get(position).getBidStatus() > 1){
			holder.awardStatusBox.setVisibility(View.VISIBLE);
		} else {
			holder.cancelBox.setVisibility(View.VISIBLE);
			holder.awardStatusBox.setVisibility(View.GONE);
		}
*/
		imageLoader.DisplayImage(searchArrayList.get(position).getImage(), holder.imageView, (ProgressBar)convertView.findViewById(R.id.progres_bar));
		holder.btnCancel.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
					//	SettingsPreferences.setSelectedSharedUser(con, searchArrayList.get(position).getId());
						MyBidActivity.alertAction = "bid_cancel";
						MyBidActivity.selected_bid_id=searchArrayList.get(position).getId();
						MyBidActivity.cancelAlert.setTitle("BID Cancel");
						MyBidActivity.cancelAlert.setMessage("You cannot bid for the request again if cancelled. Do you wish to cancel the Bid anyway?");
						MyBidActivity.cancelAlert.show();
					}
				});

		holder.btnAccept.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						//	SettingsPreferences.setSelectedSharedUser(con, searchArrayList.get(position).getId());
						MyBidActivity.selected_bid_id=searchArrayList.get(position).getId();
						MyBidActivity.alertAction = "bid_accept";
						MyBidActivity.cancelAlert.setTitle("Confirm");
						MyBidActivity.cancelAlert.setMessage("You are confirming to arrange the boat as requested. Confirm now?");
						MyBidActivity.cancelAlert.show();
					}
				});
		//ImageButton button = (ImageButton)v.findViewById( R.id.item_more);
		//holder.imageView.setImageBitmap(Utilities.getImage(searchArrayList.get(position).getImage()));
		/*Bitmap bitmap = BitmapFactory.decodeFile(searchArrayList.get(position).getImage());		
		holder.imageView.setImageBitmap(bitmap);*/
		return convertView;
	}

/*	protected static void showEditDialog() {
		// TODO Auto-generated method stub
		Get_shared_list sss = new Get_shared_list();
		sss.showEditDialog();
		
	}
*/
	static class ViewHolder	{
		TextView txtname;
		TextView txtdesc;
		ImageView imageView;
		TextView txtContact;
		TextView txtprice;
		TextView bidAwardStatus;
//		LinearLayout cancelBox;
		LinearLayout awardStatusBox;
		LinearLayout acceptStatusBox;
		LinearLayout completedStatusBox;
//		LinearLayout acceptBox;
		Button btnAccept;
		Button btnCancel;
	}
	

}
