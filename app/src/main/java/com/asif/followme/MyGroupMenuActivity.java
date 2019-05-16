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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 1/6/2018.
 */

public class MyGroupMenuActivity extends ListActivity {
    public static String RESULT_IDS = "my_menu_ids";
    public String[] groupnames, groupcodes;
    private TypedArray imgs;
    private List<MyGroupMenu> menuGroupList;
    Context context;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateCountryList();
        ArrayAdapter<MyGroupMenu> adapter = new MyGroupMenuAdapter(this, menuGroupList);
        setListAdapter(adapter);
        context = this;
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyGroupMenu c = menuGroupList.get(position);
                Intent returnIntent = new Intent();
                //returnIntent.putExtra(RESULT_IDS, c.getCode());
                //System.out.println("Code:"+c.getCode()+"-----------------------------------------------------");
                Intent mIntent;
                switch(c.getCode()) {
                    case "group_devices":   //show group device list
                        mIntent = new Intent(context, GroupDeviceList.class);
                        startActivity(mIntent);
                        break;
                    case "group_rename":  //remove
                        Intent intent = new Intent(context, NewGroupForm.class);
                    //    intent.putExtra("group_name","");
                        intent.putExtra("action","edit");
                        startActivityForResult(intent,6);
                        break;
                    case "group_delete":
                        mIntent = new Intent();
                        mIntent.putExtra("action","group_delete");
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
        super.onStart();
        String vname = "Opt...";
        try{
            Bundle extras = getIntent().getExtras();
            vname = extras.getString("vname");
        } catch (Exception e){
            vname="";
        }
        setTitle(vname);

    }
    private void populateCountryList() {
        menuGroupList = new ArrayList<MyGroupMenu>();
        groupnames = getResources().getStringArray(R.array.my_group_menu_items);
        groupcodes = getResources().getStringArray(R.array.my_group_menu_ids);
        imgs = getResources().obtainTypedArray(R.array.my_group_menu_icons);
        for(int i = 0; i < groupcodes.length; i++){
            menuGroupList.add(new MyGroupMenu(groupnames[i], groupcodes[i], imgs.getDrawable(i)));
        }
    }

    public class MyGroupMenu {
        private String name;
        private String code;
        private Drawable flag;
        public MyGroupMenu(String name, String code, Drawable flag){
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
