package com.asif.followme;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.asif.followme.MyAccount.MyActivity;
import com.asif.followme.TripPlan.NewTripForm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 1/6/2018.
 */

public class MyTripMenuActivity extends ListActivity {
    public static String RESULT_IDS = "my_menu_ids";
    public String[] groupnames, groupcodes;
    private TypedArray imgs;
    private List<MyTripMenu> menuTripList;
    Context context;
    Dialog dialog;
    int tripID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateCountryList();
        ArrayAdapter<MyTripMenu> adapter = new MyTripMenuAdapter(this, menuTripList);
        setListAdapter(adapter);
        context = this;
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyTripMenu c = menuTripList.get(position);
                Intent returnIntent = new Intent();
                //returnIntent.putExtra(RESULT_IDS, c.getCode());
                //System.out.println("Code:"+c.getCode()+"-----------------------------------------------------");
                Intent mIntent;
                switch(c.getCode()) {
                    case "trip_edit":   //show group device list
                        mIntent = new Intent(context, NewTripForm.class);
                        mIntent.putExtra("trip_id", tripID);
                        startActivity(mIntent);
                        break;
                    case "trip_delete":
                        mIntent = new Intent();
                        mIntent.putExtra("action","trip_delete");
                       setResult(RESULT_OK,mIntent );
                       finish();
                        break;
                }

                //setResult(RESULT_OK, returnIntent);
                imgs.recycle(); //recycle images
                finish();
            }
        });
    }
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        String vname = "Opt...";
        tripID = Integer.parseInt(MyActivity.selected_item_id);
        try{
            Bundle extras = getIntent().getExtras();
            vname = extras.getString("vname");
        } catch (Exception e){
            vname="";
        }
        setTitle(vname);

    }
    private void populateCountryList() {
        menuTripList = new ArrayList<MyTripMenu>();
        groupnames = getResources().getStringArray(R.array.my_trip_menu_items);
        groupcodes = getResources().getStringArray(R.array.my_trip_menu_ids);
        imgs = getResources().obtainTypedArray(R.array.my_trip_menu_icons);
        for(int i = 0; i < groupcodes.length; i++){
            menuTripList.add(new MyTripMenu(groupnames[i], groupcodes[i], imgs.getDrawable(i)));
        }
    }

    public class MyTripMenu {
        private String name;
        private String code;
        private Drawable flag;
        public MyTripMenu(String name, String code, Drawable flag){
            this.name = name;
            this.code = code;
            this.flag = flag;
        }
        public String getName() {
            return name;
        }
        public Drawable getFlag() {
            return flag;
        }
        public String getCode() {
            return code;
        }
    }



}
