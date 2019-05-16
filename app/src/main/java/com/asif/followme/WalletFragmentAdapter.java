package com.asif.followme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.asif.followme.imageloader.ImageLoader;
import com.asif.followme.model.DataWallet;

import java.util.List;

public class WalletFragmentAdapter extends BaseAdapter
{
	private static List<DataWallet> searchArrayList;

	private LayoutInflater mInflater;
	WalletFragmentAdapter custom = this;
	Context con;
	ImageLoader imageLoader;
	int tenderStatus;
	public WalletFragmentAdapter(Context context, List<DataWallet> results)
	{
		searchArrayList = results;
		mInflater = LayoutInflater.from(context);
		con  = context;
		imageLoader		=	new ImageLoader(context);
//		tenderStatus = PublicBidsActivity.tenderStatus;
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

    
	public View getView(final int position, View convertView, final ViewGroup parent){
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.wallet_fragment_adapter, null);
			holder = new ViewHolder();
			holder.txtDate = (TextView) convertView.findViewById(R.id.wallet_date);
			holder.txtDesc = (TextView) convertView.findViewById(R.id.wallet_desc);
			holder.txtAmount = (TextView) convertView.findViewById(R.id.wallet_amount);
			holder.imgSuccess1 = (ImageView) convertView.findViewById(R.id.reload_success1);
			holder.imgSuccess2 = (ImageView) convertView.findViewById(R.id.reload_success2);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.imgSuccess1.setVisibility(View.GONE);
		holder.imgSuccess2.setVisibility(View.GONE);
		switch(searchArrayList.get(position).getStatus()){
			case 2:
				holder.imgSuccess1.setVisibility(View.VISIBLE);
				break;
			case 3:
				holder.imgSuccess2.setVisibility(View.VISIBLE);
				break;
		}
		holder.txtDate.setText(searchArrayList.get(position).getDate());
		holder.txtDesc.setText(searchArrayList.get(position).getDesc());
		holder.txtAmount.setText(searchArrayList.get(position).getAmount());
		return convertView;
	}

	static class ViewHolder {
		TextView txtDate;
		TextView txtDesc;
		TextView txtAmount;
		ImageView imgSuccess1;
		ImageView imgSuccess2;
	}
	

}
