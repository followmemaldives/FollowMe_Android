package com.asif.followme.TripPlan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.asif.followme.R;
import com.asif.followme.imageloader.ImageLoader;

import java.util.List;

public class templateAdapter extends BaseAdapter
{
	private static List<DataRouteInfo> searchArrayList;

	private LayoutInflater mInflater;
	templateAdapter custom = this;
	Context con;
	ImageLoader					imageLoader;
	TemplateList activity;
	public templateAdapter(Context context, List<DataRouteInfo> results, TemplateList activity)
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
			convertView = mInflater.inflate(R.layout.trip_template_list_item, null);
			holder = new ViewHolder();
			holder.route_name = (TextView) convertView.findViewById(R.id.route_name);
			holder.route_state = (TextView) convertView.findViewById(R.id.route_state);
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

		holder.route_name.setText(searchArrayList.get(position).getRouteName());
		String route = searchArrayList.get(position).getRouteName();
		holder.route_name.setText(route);
		holder.route_state.setText(searchArrayList.get(position).getRouteState());

		holder.itemMore.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View v) {
                	//PassengerList.selected_pax_id = searchArrayList.get(position).getPaxId();
                    activity.selectMenuItem(con, searchArrayList.get(position).getRouteId(),searchArrayList.get(position).getRouteState());
//					SettingsPreferences.setSelectedItemID(con, searchArrayList.get(position).getId());
                //  fragment.selectMenuItem(searchArrayList.get(position).getId());
            }
		});


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
		TextView route_name;
		ImageView itemMore;
		TextView route_state;
	}	
	

}
