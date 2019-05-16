package com.asif.followme.TripPlan;

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
import com.asif.followme.DeviceInfoActivity;
import com.asif.followme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 1/6/2018.
 */

public class TripPaxMenuActivity extends ListActivity {
    public static String RESULT_IDS = "my_menu_ids";
    public String[] menuNames, menuCodes;
    private TypedArray menuIcons;
    private List<MyMenu> menuList;
    Context context;
    Dialog dialog;
    ArrayAdapter<MyMenu> adapter;
    private int pax_id = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyMenu c = menuList.get(position);
                Intent returnIntent = new Intent();
                Intent mIntent;
                switch(c.getCode()){
                    case "edit_booking":
                        mIntent = new Intent(context, NewPaxForm.class);
                        mIntent.putExtra("pax_id",pax_id);
                        startActivity(mIntent);
                        break;
                    case "remove":
                        Intent intent = new Intent();
                        intent.putExtra("action","rem");
                        setResult(RESULT_OK,intent );
                        finish();
                        break;
                    case "delete":
                        mIntent = new Intent();
                        mIntent.putExtra("action","del");
                        setResult(RESULT_OK,mIntent );
                        finish();
                        break;
                    case "info":
                        mIntent = new Intent(context, DeviceInfoActivity.class);
                        startActivity(mIntent);
                        break;
                }

                //setResult(RESULT_OK, returnIntent);
                menuIcons.recycle(); //recycle images
                finish();
            }
        });

    }
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        String vname = "Opt...";
        String acc ="";
        try{
            Bundle extras = getIntent().getExtras();
            vname = extras.getString("vname");
            pax_id = extras.getInt("pax_id");
        } catch (Exception e){
            e.getStackTrace();
            vname="";
        }
        populateMenuList(acc);
        setTitle("Options");

    }
    private void populateMenuList(String acc) {
        menuList = new ArrayList<MyMenu>();
        menuNames = getResources().getStringArray(R.array.trip_pax_menu_items);
        menuCodes = getResources().getStringArray(R.array.trip_pax_menu_ids);
        menuIcons = getResources().obtainTypedArray(R.array.trip_pax_menu_icons);

        for(int i = 0; i < menuCodes.length; i++){
            menuList.add(new MyMenu(menuNames[i], menuCodes[i], menuIcons.getDrawable(i)));
        }
        adapter = new TripPaxMenuAdapter(this, menuList);
        setListAdapter(adapter);

    }

    public class MyMenu {
        private String name;
        private String code;
        private Drawable flag;
        public MyMenu(String name, String code, Drawable flag){
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
