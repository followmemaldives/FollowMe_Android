package com.asif.followme;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.asif.followme.model.DataLogs;

import java.util.List;


public class tripAdapter extends BaseAdapter 
{
	private static List<DataLogs> searchArrayList;

	private LayoutInflater mInflater;
	tripAdapter custom = this;
	Context con;
//	ImageLoader imageLoader;
	static StringBuilder shareData;
//	SimpleDateFormat  readFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",java.util.Locale.getDefault());	//database
//	SimpleDateFormat  showFormat =  new SimpleDateFormat("HH:mm",java.util.Locale.getDefault());	//display format
//	SimpleDateFormat  readFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	//database
//	SimpleDateFormat  showFormat =  new SimpleDateFormat("HH:mm");	//display format


	public tripAdapter(Context context, List<DataLogs> results)
	{
		searchArrayList = results;
		mInflater = LayoutInflater.from(context);
		con  = context;
		//readFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

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
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.trip_list_item, null);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				convertView.setNestedScrollingEnabled(true);
			}
				holder.txtHeaderTitle = convertView.findViewById(R.id.trip_header_title);
//				holder.txtname = (TextView) convertView.findViewById(R.id.trip_name);
				holder.txtdate = (TextView) convertView.findViewById(R.id.trip_date);
				holder.txtstatus = (TextView) convertView.findViewById(R.id.trip_status);
				holder.txtisland = (TextView)convertView.findViewById(R.id.trip_island);
				holder.layoutLogItems = convertView.findViewById(R.id.layout_log_items);

/*			if(SettingsPreferences.getMyNavigationName(con).equalsIgnoreCase("nav_my_groups")) {
				holder.txtname.setVisibility(View.VISIBLE);
			} else {
				holder.txtname.setVisibility(View.GONE);
			}
*/
//			holder.itemRemove = (ImageView)convertView.findViewById(R.id.trip_item_remove);
//			holder.itemEdit = (ImageView)convertView.findViewById(R.id.fleet_item_edit);
			convertView.setTag(holder);
//			a = searchArrayList.get(position).getValue().equalsIgnoreCase("0") ? "DEP" : "ARR";
//			shareData.append(searchArrayList.get(position).getName()+" "+a+" "+searchArrayList.get(position).getImage()+"\n");
			//shareData.append(searchArrayList.get(position).getName()+" "+searchArrayList.get(position).getValue()+" "+searchArrayList.get(position).getImage()+"\n");
//			System.out.println("__________________ trip adapter __________________");
			
			//me
			//convertView.setClickable(true);
			//convertView.setFocusable(true);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
/*		Date date_hm;
		try{
			date_hm = readFormat.parse(searchArrayList.get(position).getTime());
			System.out.println("Date:"+date_hm);
			holder.txtdate.setText(showFormat.format(date_hm));
		} catch (ParseException e){
			e.printStackTrace();
		}
*/

//		holder.txtname.setText(searchArrayList.get(position).getId());
		if(searchArrayList.get(position).getIsHeader()==1){
		    holder.txtHeaderTitle.setVisibility(View.VISIBLE);
			holder.txtHeaderTitle.setText(searchArrayList.get(position).getName());
			holder.layoutLogItems.setVisibility(View.GONE);
		} else {
		    holder.txtHeaderTitle.setVisibility(View.GONE);
		    holder.layoutLogItems.setVisibility(View.VISIBLE);
//			holder.txtname.setText(searchArrayList.get(position).getName());
			holder.txtdate.setText(searchArrayList.get(position).getTime());
			holder.txtstatus.setText(searchArrayList.get(position).getStatus());
			holder.txtisland.setText(searchArrayList.get(position).getDesc());

		}

		return convertView;
	}


	static class ViewHolder
	{
		TextView txtdate;
//		TextView txtname;
		TextView txtstatus;
		TextView txtisland;
		TextView txtHeaderTitle;
		LinearLayout layoutLogItems;

	}	
	
/*	  public static String getShareData(){
		  shareData = new StringBuilder();
		  for (int i = 0; i < searchArrayList.size(); i++) {
				shareData.append(searchArrayList.get(i).getName()+" "+searchArrayList.get(i).getStatus()+" "+searchArrayList.get(i).getDesc()+"\n");
		  }
	        return shareData.toString();
	  }
*/
	  public static String getShareData(){
		  shareData = new StringBuilder();
		  for (int i = 0; i < searchArrayList.size(); i++) {
			  if(searchArrayList.get(i).getIsHeader()==0) {
				  shareData.append(" "+searchArrayList.get(i).getName()+ "  "+searchArrayList.get(i).getTime() + "  " + searchArrayList.get(i).getStatus() + "  " + searchArrayList.get(i).getDesc() + "\n");
			  } else {
			  	shareData.append(searchArrayList.get(i).getName()+"\n");
			  }
		  }
	        return shareData.toString();
	    }
}
