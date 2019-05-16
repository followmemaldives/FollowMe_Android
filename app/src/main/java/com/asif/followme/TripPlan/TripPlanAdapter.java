package com.asif.followme.TripPlan;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.asif.followme.R;
import com.asif.followme.imageloader.ImageLoader;
import com.asif.followme.util.AppUtils;

import java.util.List;

public class TripPlanAdapter extends BaseAdapter {

	private static List<DataTripPlans> searchArrayList;

	private LayoutInflater mInflater;
	TripPlanAdapter custom = this;
	Context con;
	ImageLoader imageLoader;
	TripPlanFragment fragment;
	private final int TRIP_DRAFT = 1;
	private final int HIRE_PUBLISHED = 2;
	private final int HIRE_CONFIRMED = 3;
	private final int HIRE_COMPLETED = 4;
	public TripPlanAdapter(Context context, List<DataTripPlans> results, TripPlanFragment fragment)
	{
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
			convertView = mInflater.inflate(R.layout.trip_plan_adapter, null);
			holder = new ViewHolder();
			holder.txtname = (TextView) convertView.findViewById(R.id.name);
			holder.txtroute = (TextView) convertView.findViewById(R.id.description);
			holder.tripDate = (TextView) convertView.findViewById(R.id.trip_date);
			holder.itemMore = (ImageView)convertView.findViewById(R.id.item_more);
			holder.imageView = (ImageView) convertView.findViewById(R.id.imageview);
			holder.captainImg = (ImageView) convertView.findViewById(R.id.captain_img);
//			holder.captainBox = (LinearLayout) convertView.findViewById(R.id.captain_box);
//			holder.txtAgo = (TextView) convertView.findViewById(R.id.time_ago);
			holder.txtStatus = (TextView) convertView.findViewById(R.id.trip_status);
//			holder.bidText = (TextView) convertView.findViewById(R.id.bid_text);
//			holder.bidStatusText = (TextView) convertView.findViewById(R.id.bid_status_text);
//			holder.bidStatusBox = (LinearLayout) convertView.findViewById(R.id.bid_status_box);
			holder.btnAck = (Button) convertView.findViewById(R.id.btn_ack);
			holder.paxTotal = (TextView) convertView.findViewById(R.id.pax_total_text);
			holder.paxBooked = (TextView) convertView.findViewById(R.id.pax_booked_total);
			holder.paxConfirmed = (TextView) convertView.findViewById(R.id.pax_confirmed_total);
//			holder.itemRemove = (ImageView)convertView.findViewById(R.id.share_item_remove);
//			holder.itemEdit = (ImageView)convertView.findViewById(R.id.share_item_edit);
			convertView.setTag(holder);

			//me
			//convertView.setClickable(true);
			//convertView.setFocusable(true);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtname.setText(searchArrayList.get(position).getName());
		holder.txtroute.setText(searchArrayList.get(position).getRoute());
		holder.tripDate.setText(searchArrayList.get(position).getTripDate());
		holder.txtStatus.setText(searchArrayList.get(position).getTripStatusText());
		//String pax_total = "TO:"+searchArrayList.get(position).getPaxTotalCount()+" BK:"+searchArrayList.get(position).getPaxBookedCount()+" CN:"+searchArrayList.get(position).getPaxConfirmedCount();
		holder.paxTotal.setText(String.valueOf(searchArrayList.get(position).getPaxTotalCount()));
		holder.paxBooked.setText(String.valueOf(searchArrayList.get(position).getPaxBookedCount()));
		holder.paxConfirmed.setText(String.valueOf(searchArrayList.get(position).getPaxConfirmedCount()));
		if(searchArrayList.get(position).getCaptain()==0){
			if((searchArrayList.get(position).getTripAccess() & 4)==4){
				holder.btnAck.setVisibility(View.VISIBLE);
				holder.captainImg.setVisibility(View.GONE);
			} else {
				holder.btnAck.setVisibility(View.GONE);
				holder.captainImg.setVisibility(View.GONE);
//              holder.captainImg.setImageResource(R.drawable.baseline_clear_black_18dp);
//                holder.captainImg.setImageResource(R.drawable.captain_gray);
			}
		} else {
			holder.captainImg.setVisibility(View.VISIBLE);
			holder.btnAck.setVisibility(View.GONE);
//            holder.captainImg.setImageResource(R.drawable.baseline_done_black_18dp);
//            holder.captainImg.setImageResource(R.drawable.captain_gray);
		}
		if(searchArrayList.get(position).getTripStatus()==TRIP_DRAFT) {
		//	holder.bidText.setText("DRAFT");
		//	holder.txtBidCount.setText("...");
		} else {
		//	holder.bidText.setText("BIDS");
		//	holder.txtBidCount.setText(searchArrayList.get(position).getBids());
		}
//		holder.txtStatus.setText(searchArrayList.get(position).getStatusText());
//		if(!searchArrayList.get(position).getBidStatusText().equalsIgnoreCase("")){
//			holder.bidStatusText.setText(searchArrayList.get(position).getBidStatusText());
//			holder.bidStatusBox.setVisibility(View.VISIBLE);
//		} else {
//			holder.bidStatusText.setText("");
//			holder.bidStatusBox.setVisibility(View.GONE);
//		}
//		if(searchArrayList.get(position).getBidStatus()==HIRE_CONFIRMED){	//Accepted/Confirmed
//			holder.btnComplete.setVisibility(View.VISIBLE);
//		} else {
//			holder.btnComplete.setVisibility(View.GONE);
//		}
		if((searchArrayList.get(position).getTripAccess() & 2) == 2){	//1 means read only, 2-Manage, 4-Captain
			holder.itemMore.setVisibility(View.VISIBLE);
		} else {
			holder.itemMore.setVisibility(View.GONE);
		}
//		holder.txtAgo.setText(searchArrayList.get(position).getTimeAgo());

		imageLoader.DisplayImage(searchArrayList.get(position).getImage(), holder.imageView, (ProgressBar)convertView.findViewById(R.id.progres_bar));
//		imageLoader.DisplayImage(searchArrayList.get(position).getImage(), holder.imageView, (ProgressBar)convertView.findViewById(R.id.progres_bar));
		holder.itemMore.setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						TripPlanFragment.selected_item_id = searchArrayList.get(position).getTripId();
						TripPlanFragment.selected_item_name = searchArrayList.get(position).getName();
                        fragment.selectMenuItem(searchArrayList.get(position).getTripId(),searchArrayList.get(position).getTripAccess());
					}
				});
		holder.btnAck.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						AppUtils.showConfirmDialog(con,"Acknowledge","Click OK, to acknowledge that the boat captain had seen the Trip Plan",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										fragment.captainAcknowledged(searchArrayList.get(position).getTripId());
										//DeleteDevice mDeleteTask = new DeleteDevice(context,device_id,action);
										//mDeleteTask.execute((Void) null);
									}
								});
					}
				}
		);
//		if(searchArrayList.get(position).getHireStatus()==HIRE_CONFIRMED || searchArrayList.get(position).getHireStatus()==HIRE_COMPLETED) {
//			holder.itemMore.setVisibility(View.INVISIBLE);
//		} else {
//		    holder.itemMore.setVisibility(View.VISIBLE);
  //      }
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
		TextView txtroute;
		ImageView itemMore;
		TextView txtStatus;
		TextView txtAgo;
		TextView tripDate;
		TextView tripStatusText;
		ImageView imageView;
		ImageView captainImg;
		Button btnAck;
		//LinearLayout captainBox;
		TextView paxTotal;
		TextView paxBooked;
		TextView paxConfirmed;

//		ImageView itemEdit;
	}	
	

}
