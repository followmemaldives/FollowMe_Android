package com.asif.followme.BoatHire.Public;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.asif.followme.R;
import com.asif.followme.imageloader.ImageLoader;

import java.util.List;

public class PublicHistoryAdapter extends BaseAdapter {

	private static OnItemClickListener mOnItemClickLister;

	public interface OnItemClickListener {
		void onItemClicked(View view, int pos);
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		mOnItemClickLister = listener;
	}

	private static List<DataPublicHire> searchArrayList;

	private LayoutInflater mInflater;
	PublicHistoryAdapter custom = this;
	Context con;
	ImageLoader imageLoader;
	PublicHistoryFragment fragment;
	public PublicHistoryAdapter(Context context, List<DataPublicHire> results, PublicHistoryFragment fragment)
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
			convertView = mInflater.inflate(R.layout.public_hire_adapter, null);
			holder = new ViewHolder();
			holder.txtname = (TextView) convertView.findViewById(R.id.name);
			holder.txtdesc = (TextView) convertView.findViewById(R.id.description);
			holder.imageView = (ImageView)convertView.findViewById(R.id.imageview);
			holder.txtBidCount = (TextView) convertView.findViewById(R.id.bid_count);
			holder.itemMore = (ImageView)convertView.findViewById(R.id.item_more);
			holder.bidBox = (LinearLayout) convertView.findViewById(R.id.bid_box);
			holder.txtAgo = (TextView) convertView.findViewById(R.id.time_ago);
			holder.txtStatus = (TextView) convertView.findViewById(R.id.bid_status);
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
		holder.txtdesc.setText(searchArrayList.get(position).getDescription());
		if(searchArrayList.get(position).getHireStatus() != 1) {
			holder.txtBidCount.setText(searchArrayList.get(position).getBids());
		} else {
			holder.bidBox.setVisibility(View.GONE);
		}
		holder.txtStatus.setText(searchArrayList.get(position).getStatusText());
		holder.txtAgo.setText(searchArrayList.get(position).getTimeAgo());

//		imageLoader.DisplayImage(searchArrayList.get(position).getImage(), holder.imageView, (ProgressBar)convertView.findViewById(R.id.progres_bar));
		imageLoader.DisplayImage(searchArrayList.get(position).getImage(), holder.imageView, (ProgressBar)convertView.findViewById(R.id.progres_bar));
		holder.itemMore.setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						// MyActivity.selectMenuItem mi = new selectMenuItem(searchArrayList.get(position).getId());
						// MyActivity.mi = new MyActivity.selectMenuItem();
						//MyActivity.selectMenuItem(searchArrayList.get(position).getId());//Get_list.popupMenu.show();
						//   MyActivity.shared_vessel_id=searchArrayList.get(position).getId();
					//	SettingsPreferences.setSelectedDevice(con, searchArrayList.get(position).getId());
						//  MyActivity.shared_vessel=searchArrayList.get(position).getName();
						//   MyActivity.selected_fleet_id=searchArrayList.get(position).getId();
						//   MyActivity.selected_fleet_name=searchArrayList.get(position).getName();
					//	MyActivity.selected_item_name =searchArrayList.get(position).getName();
					//	MyActivity.selected_item_id = searchArrayList.get(position).getId();
//						PublicHireActivity.selected_item_name = searchArrayList.get(position).getName();
//						PublicHireActivity.selectMenuItem(searchArrayList.get(position).getId(),searchArrayList.get(position).getNav());
			//			mOnItemClickLister.onItemClicked(v, position);
            //            mOnItemClickLister.onItemClicked(v, position);
                        fragment.selectMenuItem(searchArrayList.get(position).getId(),searchArrayList.get(position).getNav());

					//	PublicHireFragment.selected_item_name = searchArrayList.get(position).getName();
					//	PublicHireFragment.selectMenuItem(searchArrayList.get(position).getId(),searchArrayList.get(position).getNav());
					}
				});
		if(searchArrayList.get(position).getHireStatus() == 3) {
			holder.itemMore.setVisibility(View.INVISIBLE);
		} else {
		    holder.itemMore.setVisibility(View.VISIBLE);
        }
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
		TextView txtBidCount;
		ImageView itemMore;
		LinearLayout bidBox;
		TextView txtStatus;
		TextView txtAgo;

//		ImageView itemEdit;
	}	
	

}
