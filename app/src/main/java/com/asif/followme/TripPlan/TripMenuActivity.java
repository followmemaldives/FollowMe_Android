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

public class TripMenuActivity extends ListActivity {
    public static String RESULT_IDS = "my_menu_ids";
    public String[] menuNames, menuCodes;
    private TypedArray menuIcons;
    private List<TripPlanMenu> menuList;
    Context context;
    Dialog dialog;
    ArrayAdapter<TripPlanMenu> adapter;
    int trip_id=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TripPlanMenu c = menuList.get(position);
                Intent returnIntent = new Intent();
                Intent mIntent;
                switch(c.getCode()) {
                    case "edit":
                        mIntent = new Intent(context, NewTripForm.class);
                        mIntent.putExtra("trip_id",trip_id);
                        mIntent.putExtra("action","edit");
                        startActivity(mIntent);
                        break;
                    case "delete":
                        mIntent = new Intent();
                        mIntent.putExtra("action","del");
                        setResult(RESULT_OK,mIntent );
                        finish();
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
        int acc =0;

        try{
            Bundle extras = getIntent().getExtras();
            vname = extras.getString("vname");
            acc = extras.getInt("acc");
            trip_id = extras.getInt("trip_id");
        } catch (Exception e){
            e.getStackTrace();
            vname="";
        }
        populateMenuList(acc);
        setTitle("Options");

    }
    private void populateMenuList(int acc) {
                menuList = new ArrayList<TripPlanMenu>();
                menuNames = getResources().getStringArray(R.array.trip_plan_menu_items);
                menuCodes = getResources().getStringArray(R.array.trip_plan_menu_ids);
                menuIcons = getResources().obtainTypedArray(R.array.trip_plan_menu_icons);

        for(int i = 0; i < menuCodes.length; i++){
            menuList.add(new TripPlanMenu(menuNames[i], menuCodes[i], menuIcons.getDrawable(i)));
        }
        adapter = new TripMenuAdapter(this, menuList);
        setListAdapter(adapter);

    }

    public class TripPlanMenu {
        private String name;
        private String code;
        private Drawable flag;
        public TripPlanMenu(String name, String code, Drawable flag){
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
