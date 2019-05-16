package com.asif.followme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asif.followme.imageloader.ImageLoader;
import com.asif.followme.model.DataMy;

import java.util.List;

public class fleetAdapter extends BaseAdapter
{
	private static List<DataMy> searchArrayList;

	private LayoutInflater mInflater;
	fleetAdapter custom = this;
	Context con;
	ImageLoader					imageLoader;
	public fleetAdapter(Context context, List<DataMy> results)
	{
		searchArrayList = results;
		mInflater = LayoutInflater.from(context);
		con  = context;
		imageLoader		=	new ImageLoader(context);
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
			convertView = mInflater.inflate(R.layout.group_device_list_item, null);
			holder = new ViewHolder();
			holder.txtname = (TextView) convertView.findViewById(R.id.device_name);
			holder.txtvalue = (TextView) convertView.findViewById(R.id.device_description);
			holder.imageView = (ImageView)convertView.findViewById(R.id.imageview);
			holder.itemRemove = (ImageView)convertView.findViewById(R.id.fleet_device_remove);
//			holder.itemEdit = (ImageView)convertView.findViewById(R.id.fleet_item_edit);
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
		holder.txtvalue.setText("ID: "+searchArrayList.get(position).getId());
//		imageLoader.DisplayImage(searchArrayList.get(position).getImage(), holder.imageView, (ProgressBar)convertView.findViewById(R.id.fleet_progres_bar));
		imageLoader.DisplayImage(searchArrayList.get(position).getImage(), holder.imageView, (ProgressBar)convertView.findViewById(R.id.progres_bar));
		//ImageButton button = (ImageButton)v.findViewById( R.id.item_more);
		//System.out.println(searchArrayList.get(position).getImage());
		holder.itemRemove.setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						//Get_list.popupMenu.show();
						GroupDeviceList.fleet_device_id=searchArrayList.get(position).getId();
						GroupDeviceList.fleetAlert.setMessage("Remove the device '"+searchArrayList.get(position).getName()+"' from the group?");
						GroupDeviceList.fleetAlert.show();
						//Get_list.showOptionDialog();
						//Get_list.pw.showAtLocation(v.findViewById(R.id.list), Gravity.CENTER, 0, 0);
						//Get_list.pw.showAtLocation(parent, Gravity.CENTER, 0, 0);
						//Toast.makeText( con,"ImageButton clicked "+searchArrayList.get(position).getId(),Toast.LENGTH_SHORT).show();
					}
				});
		//holder.imageView.setImageBitmap(Utils.getImage(searchArrayList.get(position).getImage()));
		//Bitmap bitmap = BitmapFactory.decodeFile(searchArrayList.get(position).getImage());
		//holder.imageView.setImageBitmap(bitmap);
		return convertView;
	}

	/*	protected static void showEditDialog() {
            Get_shared_list sss = new Get_shared_list();
            sss.showEditDialog();

        }
    */
	static class ViewHolder
	{
		TextView txtname;
		TextView txtvalue;
		ImageView imageView;
		ImageView itemRemove;
		ImageView itemEdit;
	}


}
