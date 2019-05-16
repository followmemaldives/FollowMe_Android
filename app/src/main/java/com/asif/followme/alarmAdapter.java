package com.asif.followme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.asif.followme.model.DataLogs;

import java.util.List;


public class alarmAdapter extends BaseAdapter
{
	private static List<DataLogs> searchArrayList;

	private LayoutInflater mInflater;
	alarmAdapter custom = this;
	Context con;
//	ImageLoader imageLoader;
	static StringBuilder shareData;
	public alarmAdapter(Context context, List<DataLogs> results)
	{
		searchArrayList = results;
		mInflater = LayoutInflater.from(context);
		con  = context;
//		imageLoader		=	new ImageLoader(context);
//		shareData = new StringBuilder();
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
			convertView = mInflater.inflate(R.layout.alarm_adapter, null);
			holder = new ViewHolder();
//			holder.txtname = (TextView) convertView.findViewById(R.id.alarm_name);

			holder.txtdate = (TextView) convertView.findViewById(R.id.alarm_date);
			holder.txtdesc = (TextView)convertView.findViewById(R.id.alarm_desc);
			holder.layoutLogItems = convertView.findViewById(R.id.layout_log_items);
			holder.txtHeaderTitle = convertView.findViewById(R.id.alarm_header_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}


		if(searchArrayList.get(position).getIsHeader()==1){
			holder.txtHeaderTitle.setVisibility(View.VISIBLE);
			holder.txtHeaderTitle.setText(searchArrayList.get(position).getName());
			holder.layoutLogItems.setVisibility(View.GONE);
		} else {
			holder.txtHeaderTitle.setVisibility(View.GONE);
			holder.layoutLogItems.setVisibility(View.VISIBLE);
//			holder.txtname.setText(searchArrayList.get(position).getName());
			holder.txtdate.setText(searchArrayList.get(position).getTime());
			holder.txtdesc.setText(searchArrayList.get(position).getDesc());

		}

		return convertView;
	}


	static class ViewHolder
	{
		TextView txtname;
		TextView txtdate;
		TextView txtdesc;
		TextView txtHeaderTitle;
		LinearLayout layoutLogItems;
	}
}
