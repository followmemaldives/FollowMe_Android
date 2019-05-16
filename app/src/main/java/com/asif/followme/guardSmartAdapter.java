package com.asif.followme;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.asif.followme.model.DataSmartLogs;

import java.util.List;


public class guardSmartAdapter extends BaseAdapter
{
	private static List<DataSmartLogs> searchArrayList;

	private LayoutInflater mInflater;
	guardSmartAdapter custom = this;
	Context con;
//	ImageLoader imageLoader;
	static StringBuilder shareData;
	private final int MAX_POSTS = 11;
//	TextView[] textViewArray = new TextView[MAX_POSTS];


//	SimpleDateFormat  readFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",java.util.Locale.getDefault());	//database
//	SimpleDateFormat  showFormat =  new SimpleDateFormat("HH:mm",java.util.Locale.getDefault());	//display format
//	SimpleDateFormat  readFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	//database
//	SimpleDateFormat  showFormat =  new SimpleDateFormat("HH:mm");	//display format


	public guardSmartAdapter(Context context, List<DataSmartLogs> results)
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
			convertView = mInflater.inflate(R.layout.guard_smart_list_item, null);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				convertView.setNestedScrollingEnabled(true);
			}
				holder.t0 = (TextView) convertView.findViewById(R.id.t0);
				holder.t1 = (TextView) convertView.findViewById(R.id.t1);
				holder.t2 = (TextView) convertView.findViewById(R.id.t2);
				holder.t3 = (TextView) convertView.findViewById(R.id.t3);
				holder.t4 = (TextView) convertView.findViewById(R.id.t4);
				holder.t5 = (TextView) convertView.findViewById(R.id.t5);
				holder.t6 = (TextView) convertView.findViewById(R.id.t6);
				holder.t7 = (TextView) convertView.findViewById(R.id.t7);
				holder.t8 = (TextView) convertView.findViewById(R.id.t8);
				holder.t9 = (TextView) convertView.findViewById(R.id.t9);
				holder.t10 = (TextView) convertView.findViewById(R.id.t10);

				holder.layoutLogItems = convertView.findViewById(R.id.layout_log_items);
			/*	textViewArray[0] = holder.t0;
				textViewArray[1] = holder.t1;
				textViewArray[2] = holder.t2;
				textViewArray[3] = holder.t3;
				textViewArray[4] = holder.t4;
				textViewArray[5] = holder.t5;
				textViewArray[6] = holder.t6;
				*/

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
		//JSONArray mm = searchArrayList.get(position).getSmartArray();

			//	System.out.println("MM:"+mm);
		holder.t0.setText(searchArrayList.get(position).getS(0));
		holder.t1.setText(searchArrayList.get(position).getS(1));
		holder.t2.setText(searchArrayList.get(position).getS(2));
		holder.t3.setText(searchArrayList.get(position).getS(3));
		holder.t4.setText(searchArrayList.get(position).getS(4));
		holder.t5.setText(searchArrayList.get(position).getS(5));
		holder.t6.setText(searchArrayList.get(position).getS(6));
		holder.t7.setText(searchArrayList.get(position).getS(7));
		holder.t8.setText(searchArrayList.get(position).getS(8));
		holder.t9.setText(searchArrayList.get(position).getS(9));
		holder.t10.setText(searchArrayList.get(position).getS(10));


/*	    int m = searchArrayList.get(position).getSizes();
	    System.out.println("M:"+m);
		for(int j=0; j < searchArrayList.get(position).getSizes();j++) {
			System.out.println("J:" + j);
			if (j < MAX_POSTS) {
				textViewArray[j].setText(searchArrayList.get(position).getS(j));
				//textViewArray[j].setVisibility(View.VISIBLE);
			}

		}
*/

//		holder.txtname.setText(searchArrayList.get(position).getId());
//		    holder.txtHeaderTitle.setVisibility(View.GONE);
//		    holder.layoutLogItems.setVisibility(View.VISIBLE);
/*		try {
			System.out.println(searchArrayList.get(position).getSmartArray().get(1));
			holder.t0.setText(searchArrayList.get(position).getSmartArray().get(0).toString());
			holder.t1.setText(searchArrayList.get(position).getSmartArray().getString(1));
			holder.t2.setText(searchArrayList.get(position).getSmartArray().getString(2));
			holder.t3.setText(searchArrayList.get(position).getSmartArray().getString(3));
		} catch (JSONException e) {
			e.printStackTrace();
		}
*/

		return convertView;
	}


	static class ViewHolder
	{
		TextView t0;
		TextView t1;
		TextView t2;
		TextView t3;
		TextView t4;
		TextView t5;
		TextView t6;
		TextView t7;
		TextView t8;
		TextView t9;
		TextView t10;
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

}
