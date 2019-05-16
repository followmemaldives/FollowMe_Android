package com.asif.followme.BoatHire.Public;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.asif.followme.BoatHire.DataBids;
import com.asif.followme.PhotoActivity;
import com.asif.followme.R;
import com.asif.followme.imageloader.ImageLoader;
import com.asif.followme.util.SettingsPreferences;

import java.util.List;

public class PublicBidAdapter extends BaseAdapter
{
	private static List<DataBids> searchArrayList;

	private LayoutInflater mInflater;
	PublicBidAdapter custom = this;
	Context con;
	ImageLoader imageLoader;
	PublicBidFragment fragment;
	int tenderStatus;
	final int TENDER_PUBLISHED = 2;
	final int TENDER_COMPLETED = 4;
	final int BID_DONE = 1;
	final int BID_AWARDED = 2;
	final int BID_ACCEPTED = 3;
	final int BID_COMPLETED = 4;
	public PublicBidAdapter(Context context, List<DataBids> results, PublicBidFragment fragment)
	{
		searchArrayList = results;
		mInflater = LayoutInflater.from(context);
		con  = context;
		imageLoader		=	new ImageLoader(context);
		tenderStatus = PublicBidFragment.tenderStatus;
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
			convertView = mInflater.inflate(R.layout.public_bid_adpater, null);
			holder = new ViewHolder();
			holder.txtname = (TextView) convertView.findViewById(R.id.name);
			holder.txtContact = (TextView) convertView.findViewById(R.id.description);
			holder.imageView = (ImageView)convertView.findViewById(R.id.imageview);
		//	holder.txtBidCount = (TextView) convertView.findViewById(R.id.bid_count);
			holder.txtprice = (TextView) convertView.findViewById(R.id.bid_price);
			holder.imgStop = (ImageView)convertView.findViewById(R.id.img_stop);
			holder.txtIslandSpeed = (TextView) convertView.findViewById(R.id.txt_island);
	//		holder.awardBox = (LinearLayout) convertView.findViewById(R.id.award_box);
			holder.awardButton = (Button) convertView.findViewById(R.id.btn_award);
			holder.rateButton = (Button) convertView.findViewById(R.id.btn_rate);
			holder.txtAgo = (TextView) convertView.findViewById(R.id.time_ago);
			holder.awardStatusBox = (LinearLayout) convertView.findViewById(R.id.award_status_box);
			holder.txtAwardStatus = (TextView) convertView.findViewById(R.id.bid_award_status);
			holder.awardAcceptBox = (LinearLayout) convertView.findViewById(R.id.award_accept_box);
			holder.bidCompletedBox = (LinearLayout) convertView.findViewById(R.id.bid_completed_box);

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
//		holder.txtBidCount.setText("AWARD");
		holder.txtprice.setText(searchArrayList.get(position).getPrice());
//		imageLoader.DisplayImage(searchArrayList.get(position).getImage(), holder.imageView, (ProgressBar)convertView.findViewById(R.id.progres_bar));
		imageLoader.DisplayImage(searchArrayList.get(position).getImage(), holder.imageView, (ProgressBar)convertView.findViewById(R.id.progres_bar));
		int moveIcon = searchArrayList.get(position).getMarker();
		holder.txtIslandSpeed.setText(searchArrayList.get(position).getIslandSpeed());
		holder.txtAgo.setText(searchArrayList.get(position).getTimeAgo());
		holder.awardButton.setVisibility(View.GONE);
		holder.awardAcceptBox.setVisibility(View.GONE);
		holder.awardStatusBox.setVisibility(View.GONE);
		holder.rateButton.setVisibility(View.GONE);
		holder.bidCompletedBox.setVisibility(View.GONE);
		if(searchArrayList.get(position).getTenderStatus() == TENDER_PUBLISHED){	// tender published
			System.out.println("Tender Status is Equal to:"+TENDER_PUBLISHED);

			if(searchArrayList.get(position).getBidStatus() == BID_DONE){
				if(searchArrayList.get(position).getIsExpired()==0) {	//show only if not expired
					holder.awardButton.setVisibility(View.VISIBLE);
				}
			}
			if(searchArrayList.get(position).getBidStatus()==BID_AWARDED) {    // bid awarded
				holder.awardStatusBox.setVisibility(View.VISIBLE);
//				holder.awardBox.setVisibility(View.GONE);
//				holder.awardStatusBox.setVisibility(View.VISIBLE);
//				holder.txtAwardStatus.setText("AWARDED");
			}
			if(searchArrayList.get(position).getBidStatus() == BID_ACCEPTED){	//confirmed
					holder.awardAcceptBox.setVisibility(View.VISIBLE);
					holder.rateButton.setVisibility(View.VISIBLE);
			}
			if(searchArrayList.get(position).getBidStatus() == BID_COMPLETED){
				holder.awardAcceptBox.setVisibility(View.VISIBLE);
				holder.rateButton.setVisibility(View.VISIBLE);
			}

		} else {
			holder.awardButton.setVisibility(View.GONE);
			if (searchArrayList.get(position).getBidStatus() == BID_COMPLETED) {
				holder.bidCompletedBox.setVisibility(View.VISIBLE);
			}
		}
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
		holder.imageView.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						PhotoActivity.selectedImage = searchArrayList.get(position).getImage();
						SettingsPreferences.setSelectedItemID(con, searchArrayList.get(position).getDeviceID());
//						fragment.selectMenuItem(searchArrayList.get(position).getId());
//						SettingsPreferences.setItemId(con, searchArrayList.get(position).getId());
						Intent intent = new Intent(con, PhotoActivity.class);
						intent.putExtra("account","public");
						con.startActivity(intent);
					}
				});
		holder.awardButton.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						//Get_list.popupMenu.show();
						//BoatHireActivity.selected_item_id=searchArrayList.get(position).getId();
						//SettingsPreferences.setSelectedDevice(con, searchArrayList.get(position).getId());
						fragment.openBidAwardActivity(searchArrayList.get(position).getId(), searchArrayList.get(position).getName());
/*						Intent intent = new Intent(con, BidAwardActivity.class);
						intent.putExtra("v_name", searchArrayList.get(position).getName());
						System.out.println(searchArrayList.get(position).getId());
						intent.putExtra("bid_id",searchArrayList.get(position).getId());
						con.startActivity(intent);

*/
						 //	PublicList.myAlert.setTitle(searchArrayList.get(position).getName());
						//	PublicList.myAlert.show();
						//System.out.println("ID"+PublicList.selected_vessel_id);
//                        PublicActivity.showNotice(con,searchArrayList.get(position).getId(),searchArrayList.get(position).getName());
					}
				});
		holder.rateButton.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						fragment.openBidRateActivity(searchArrayList.get(position).getId(), searchArrayList.get(position).getName());
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
	static class ViewHolder
	{
		TextView txtname;
		TextView txtdesc;
		ImageView imageView;
		TextView txtContact;
		TextView txtprice;
		ImageView imgStop;
		TextView txtIslandSpeed;
	//	LinearLayout awardBox;
		Button awardButton;
		TextView txtAgo;
		Button rateButton;

		LinearLayout awardStatusBox;
		TextView txtAwardStatus;

		LinearLayout awardAcceptBox;
		LinearLayout bidCompletedBox;


//		ImageView itemRemove;
//		ImageView itemEdit;
	}	
	

}
