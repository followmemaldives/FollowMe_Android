package com.asif.followme.BoatHire.Operator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.asif.followme.R;
import com.asif.followme.imageloader.ImageLoader;

import java.util.List;

public class HireMyAdapter extends BaseAdapter {

	private static List<DataMyHire> searchArrayList;
//	private DataMyHire row;
	private LayoutInflater mInflater;
	HireMyAdapter custom = this;
	Context con;
	ImageLoader imageLoader;
	HireMyFragment fragment;
	final int BID_DONE = 1;
	final int BID_AWARDED = 2;
	final int BID_ACCEPTED = 3;
	final int HIRE_PUBLISHED = 2;
	final int HIRE_COMPLETED = 4;

	public HireMyAdapter(Context context, List<DataMyHire> results, HireMyFragment fragment)
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
			convertView = mInflater.inflate(R.layout.hire_my_adapter, null);
			holder = new ViewHolder();
			holder.txtname = (TextView) convertView.findViewById(R.id.name);
			holder.txtDesc1 = (TextView) convertView.findViewById(R.id.description1);
			holder.txtDesc2 = (TextView) convertView.findViewById(R.id.description2);
//			holder.imageView = (ImageView)convertView.findViewById(R.id.imageview);
			holder.txtBidCount = (TextView) convertView.findViewById(R.id.bid_count);
			holder.txtHireDate = (TextView) convertView.findViewById(R.id.hire_date);
//			holder.itemMore = (ImageView)convertView.findViewById(R.id.item_more);
			holder.bidCountBox = (LinearLayout) convertView.findViewById(R.id.bid_count_box);
			holder.completedBox = (LinearLayout) convertView.findViewById(R.id.hire_completed_box);

//			holder.bidDone = (ImageView)convertView.findViewById(R.id.bid_done);
//			holder.bidWin = (ImageView)convertView.findViewById(R.id.bid_win);
			holder.txtBidAction = (TextView) convertView.findViewById(R.id.bid_action_status);
			holder.bidNowBox = (LinearLayout) convertView.findViewById(R.id.bid_now_box);
			holder.bidDoneBox = (LinearLayout) convertView.findViewById(R.id.bid_done_box);
			holder.bidAwardBox = (LinearLayout) convertView.findViewById(R.id.bid_award_box);
			holder.bidAcceptBox = (LinearLayout) convertView.findViewById(R.id.bid_accept_box);
			holder.txtAgo = (TextView) convertView.findViewById(R.id.time_ago);
//			holder.txtStatus = (TextView) convertView.findViewById(R.id.bid_status);
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
//		row = searchArrayList.get(position);

		holder.txtname.setText(searchArrayList.get(position).getName());
		holder.txtDesc1.setText(searchArrayList.get(position).getDesc1());
		holder.txtDesc2.setText(searchArrayList.get(position).getDesc2());
		holder.txtAgo.setText(searchArrayList.get(position).getTimeAgo());
//		if(!searchArrayList.get(position).getStatus().equalsIgnoreCase("2")) {
			holder.txtBidCount.setText(searchArrayList.get(position).getBids());
//		} else {
//			holder.bidCountBox.setVisibility(View.GONE);
//		}
//		holder.txtStatus.setText(row.getStatusText());
		holder.bidDoneBox.setVisibility(View.GONE);
		holder.bidNowBox.setVisibility(View.GONE);
		holder.bidAwardBox.setVisibility(View.GONE);
		holder.bidAcceptBox.setVisibility(View.GONE);

		holder.txtHireDate.setText(searchArrayList.get(position).getHireDate());
		if(searchArrayList.get(position).getStatus()==HIRE_PUBLISHED) {
			holder.completedBox.setVisibility(View.GONE);
			if (searchArrayList.get(position).getMyBidCount() > 0) {    // show only for new bids, to have space for bid status boxes
				holder.txtDesc2.setVisibility(View.GONE);
			} else {
				holder.txtDesc2.setVisibility(View.VISIBLE);
			}
			if (searchArrayList.get(position).getMyAcceptCount() > 0) {
				holder.bidAcceptBox.setVisibility(View.VISIBLE);
				//holder.bidNowBox.setVisibility(View.VISIBLE);
			} else {
				if (searchArrayList.get(position).getMyWinCount() > 0) {
					holder.bidAwardBox.setVisibility(View.VISIBLE);
				} else {
					if (searchArrayList.get(position).getMyBidCount() > 0) {
						holder.bidDoneBox.setVisibility(View.VISIBLE);
					}

				}

			}
		} else {
			holder.txtDesc2.setVisibility(View.GONE);
			holder.completedBox.setVisibility(View.VISIBLE);

		}

		return convertView;
	}

	static class ViewHolder
	{
		TextView txtname;
		TextView txtDesc1;
		TextView txtDesc2;
//		ImageView imageView;
		TextView txtBidCount;
		TextView txtHireDate;
		TextView txtBidAction;
//		ImageView bidDone;
//		ImageView bidWin;
//		ImageView itemMore;
		LinearLayout bidNowBox;
		LinearLayout bidDoneBox;
		LinearLayout bidAwardBox;
		LinearLayout bidCountBox;
		LinearLayout bidAcceptBox;
		LinearLayout completedBox;
//		TextView txtStatus;
		TextView txtAgo;

//		ImageView itemEdit;
	}	
	

}
