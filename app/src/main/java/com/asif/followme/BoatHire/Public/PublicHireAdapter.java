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

public class PublicHireAdapter extends BaseAdapter {

	private static List<DataPublicHire> searchArrayList;

	private LayoutInflater mInflater;
	PublicHireAdapter custom = this;
	Context con;
	ImageLoader imageLoader;
	PublicHireFragment fragment;
	private final int HIRE_DRAFT = 1;
	private final int HIRE_PUBLISHED = 2;
	private final int HIRE_CONFIRMED = 3;
	private final int HIRE_COMPLETED = 4;
	public PublicHireAdapter(Context context, List<DataPublicHire> results, PublicHireFragment fragment)
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
			holder.hireDate = (TextView) convertView.findViewById(R.id.hire_date);
			holder.txtBidCount = (TextView) convertView.findViewById(R.id.bid_count);
			holder.itemMore = (ImageView)convertView.findViewById(R.id.item_more);
			holder.bidBox = (LinearLayout) convertView.findViewById(R.id.bid_box);
//			holder.txtAgo = (TextView) convertView.findViewById(R.id.time_ago);
			holder.txtStatus = (TextView) convertView.findViewById(R.id.bid_status);
			holder.bidText = (TextView) convertView.findViewById(R.id.bid_text);
			holder.bidStatusText = (TextView) convertView.findViewById(R.id.bid_status_text);
			holder.bidStatusBox = (LinearLayout) convertView.findViewById(R.id.bid_status_box);
			holder.btnComplete = (Button) convertView.findViewById(R.id.btn_complete);
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
		holder.hireDate.setText(searchArrayList.get(position).getHireDate());
		if(searchArrayList.get(position).getHireStatus()==HIRE_DRAFT) {
			holder.bidText.setText("DRAFT");
			holder.txtBidCount.setText("...");
		} else {
			holder.bidText.setText("BIDS");
			holder.txtBidCount.setText(searchArrayList.get(position).getBids());
		}
		holder.txtStatus.setText(searchArrayList.get(position).getStatusText());
		if(!searchArrayList.get(position).getBidStatusText().equalsIgnoreCase("")){
			holder.bidStatusText.setText(searchArrayList.get(position).getBidStatusText());
			holder.bidStatusBox.setVisibility(View.VISIBLE);
		} else {
			holder.bidStatusText.setText("");
			holder.bidStatusBox.setVisibility(View.GONE);
		}
		if(searchArrayList.get(position).getBidStatus()==HIRE_CONFIRMED){	//Accepted/Confirmed
			holder.btnComplete.setVisibility(View.VISIBLE);
		} else {
			holder.btnComplete.setVisibility(View.GONE);
		}
		if(searchArrayList.get(position).getHireStatus()==HIRE_COMPLETED){
			holder.itemMore.setVisibility(View.GONE);
		} else {
			holder.itemMore.setVisibility(View.VISIBLE);
		}
//		holder.txtAgo.setText(searchArrayList.get(position).getTimeAgo());

//		imageLoader.DisplayImage(searchArrayList.get(position).getImage(), holder.imageView, (ProgressBar)convertView.findViewById(R.id.progres_bar));
//		imageLoader.DisplayImage(searchArrayList.get(position).getImage(), holder.imageView, (ProgressBar)convertView.findViewById(R.id.progres_bar));
		holder.itemMore.setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						PublicHireFragment.selected_item_id = searchArrayList.get(position).getId();
						PublicHireFragment.selected_item_name = searchArrayList.get(position).getName();
                        fragment.selectMenuItem(searchArrayList.get(position).getId(),searchArrayList.get(position).getNav());
					}
				});
		if(searchArrayList.get(position).getHireStatus()==HIRE_CONFIRMED || searchArrayList.get(position).getHireStatus()==HIRE_COMPLETED) {
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
		TextView txtBidCount;
		ImageView itemMore;
		LinearLayout bidBox, bidStatusBox;
		TextView txtStatus;
		TextView txtAgo;
		TextView hireDate;
		TextView bidText;
		TextView bidStatusText;
		Button btnComplete;

//		ImageView itemEdit;
	}	
	

}
