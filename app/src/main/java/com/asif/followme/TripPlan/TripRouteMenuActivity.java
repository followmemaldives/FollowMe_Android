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
import com.asif.followme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 1/6/2018.
 */

public class TripRouteMenuActivity extends ListActivity {
    public static String RESULT_IDS = "my_menu_ids";
    public String[] menuNames, menuCodes;
    private TypedArray menuIcons;
    private List<MyMenu> menuList;
    Context context;
    Dialog dialog;
    ArrayAdapter<MyMenu> adapter;
    private int route_id = 0;
    private int client_id = 0;
    private String route_status="Draft";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        menuList = new ArrayList<MyMenu>();
        menuNames = getResources().getStringArray(R.array.trip_route_menu_items);
        menuCodes = getResources().getStringArray(R.array.trip_route_menu_ids);
        menuIcons = getResources().obtainTypedArray(R.array.trip_route_menu_icons);

        for(int i = 0; i < menuCodes.length; i++){
            menuList.add(new MyMenu(menuNames[i], menuCodes[i], menuIcons.getDrawable(i)));
        }
        adapter = new TripRouteMenuAdapter(this, menuList);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyMenu c = menuList.get(position);
                Intent mIntent;
                switch(c.getCode()){
                    case "edit_route":
                        mIntent = new Intent(context, NewTemplateForm.class);
                        mIntent.putExtra("route_id",route_id);
                        mIntent.putExtra("route_status", route_status);
                        mIntent.putExtra("client_id", client_id);
                        startActivity(mIntent);
                        break;
                    case "del_route":
                        mIntent = new Intent();
                        mIntent.putExtra("action","del");
                        setResult(RESULT_OK,mIntent );
                        finish();
                        break;
                }
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
            route_id = extras.getInt("route_id");
            client_id = extras.getInt("client_id");
            System.out.println("Menu CLient:"+client_id+"...........................................");
            route_status = extras.getString("route_status");
        } catch (Exception e){
            e.getStackTrace();
            vname="";
        }
        setTitle("Options");

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
