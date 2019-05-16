package com.asif.followme.TripPlan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.asif.followme.R;

import java.util.List;

public class legInfoAdapter extends BaseAdapter {
	private static List<DataLegInfo> searchArrayList;

	private LayoutInflater mInflater;
	legInfoAdapter custom = this;
	Context con;
	PassengerList activity;
	public legInfoAdapter(Context context, List<DataLegInfo> results, PassengerList activity)
	{
		searchArrayList = results;
		mInflater = LayoutInflater.from(context);
		con  = context;
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
			convertView = mInflater.inflate(R.layout.trip_leg_info_item, null);
			holder = new ViewHolder();
			holder.from_island = (TextView) convertView.findViewById(R.id.from_island);
			holder.to_island = (TextView) convertView.findViewById(R.id.to_island);
			holder.pax_qty = (TextView) convertView.findViewById(R.id.pax_qty);
			convertView.setTag(holder);
			
			//me
			//convertView.setClickable(true);
			//convertView.setFocusable(true);
		} 
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.from_island.setText(searchArrayList.get(position).getLegName());
//		String route = searchArrayList.get(position).getFrom()+" > "+searchArrayList.get(position).getTo();
		//holder.to_island.setText(searchArrayList.get(position).getIslandTo());
		holder.pax_qty.setText(String.valueOf(searchArrayList.get(position).getPaxQty()));

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
		TextView from_island;
		TextView to_island;
		TextView pax_qty;
	}	
	

}
