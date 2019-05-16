package com.asif.followme.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asif.followme.MyAccount.DataMyShare;
import com.asif.followme.MyAccount.ShareList;
import com.asif.followme.R;
import com.asif.followme.ShareFormActivity;
import com.asif.followme.imageloader.ImageLoader;
import com.asif.followme.util.SettingsPreferences;

import java.util.List;

public class shareAdapter extends BaseAdapter 
{
	private static List<DataMyShare> searchArrayList;

	private LayoutInflater mInflater;
	shareAdapter custom = this;
	Context con;
	ImageLoader					imageLoader;
	public shareAdapter(Context context, List<DataMyShare> results)
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
			convertView = mInflater.inflate(R.layout.share_list_item, null);
			holder = new ViewHolder();
			holder.txtname = (TextView) convertView.findViewById(R.id.share_name);
			holder.txtvalue = (TextView) convertView.findViewById(R.id.share_description);
			holder.imageView = (ImageView)convertView.findViewById(R.id.share_imageview);
			holder.itemRemove = (ImageView)convertView.findViewById(R.id.share_item_remove);
			holder.itemEdit = (ImageView)convertView.findViewById(R.id.share_item_edit);
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
		holder.txtvalue.setText(searchArrayList.get(position).getValue());	
//		imageLoader.DisplayImage(searchArrayList.get(position).getImage(), holder.imageView, (ProgressBar)convertView.findViewById(R.id.progres_bar));
		imageLoader.DisplayImage(searchArrayList.get(position).getImage(), holder.imageView, (ProgressBar)convertView.findViewById(R.id.progres_bar));
		//ImageButton button = (ImageButton)v.findViewById( R.id.item_more);
		holder.itemRemove.setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						//Get_list.popupMenu.show();
						SettingsPreferences.setSelectedSharedUser(con, searchArrayList.get(position).getId());
						ShareList.shared_user_id=searchArrayList.get(position).getId();
						ShareList.shareAlert.setMessage("Remove the user '"+searchArrayList.get(position).getName()+"' from shared list?");
						ShareList.shareAlert.show();
						//Get_list.showOptionDialog();
					    //Get_list.pw.showAtLocation(v.findViewById(R.id.list), Gravity.CENTER, 0, 0); 
						//Get_list.pw.showAtLocation(parent, Gravity.CENTER, 0, 0);
						//Toast.makeText( con,"ImageButton clicked "+searchArrayList.get(position).getId(),Toast.LENGTH_SHORT).show();
					}
		});
		holder.itemEdit.setOnClickListener(
			new OnClickListener() {
				public void onClick(View v) {
					SettingsPreferences.setSelectedSharedUser(con, searchArrayList.get(position).getId());
					//ShareList.shared_user_id=searchArrayList.get(position).getId();
					//ShareList.shared_user_email=searchArrayList.get(position).getValue();
					//ShareList.shareAlert.setMessage("Remove the user '"+searchArrayList.get(position).getName()+"' from shared list?");
					//ShareList sss = new ShareList();
				 	//   sss.getSharedUser(con,MyActivity.shared_vessel_id,searchArrayList.get(position).getId());

					Intent intent = new Intent(con, ShareFormActivity.class);
					intent.putExtra("vname",searchArrayList.get(position).getName());
					intent.putExtra("user_id",searchArrayList.get(position).getId());
					intent.putExtra("user_email",searchArrayList.get(position).getValue());
					con.startActivity(intent);
					//showEditDialog();
			        //Get_shared_list.shareDialog.show();
				}
		});
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
		TextView txtvalue;		
		ImageView imageView;
		ImageView itemRemove;
		ImageView itemEdit;
	}	
	

}
