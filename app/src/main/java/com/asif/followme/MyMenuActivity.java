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

import com.asif.followme.MyAccount.EditDevice;
import com.asif.followme.MyAccount.MyBoatsFragment;
import com.asif.followme.MyAccount.ShareList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 1/6/2018.
 */

public class MyMenuActivity extends ListActivity {
    public static String RESULT_IDS = "my_menu_ids";
    public String[] menuNames, menuCodes;
    private TypedArray menuIcons;
    private List<MyMenu> menuList;
    Context context;
    Dialog dialog;
    ArrayAdapter<MyMenu> adapter;


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
                    case "edit":
                        mIntent = new Intent(context, EditDevice.class);
                        startActivity(mIntent);
                        break;
                    case "share":
                        mIntent = new Intent(context, ShareList.class);
                        startActivity(mIntent);
                        break;
                    case "logs":
                        mIntent		=	new Intent(context, LogsActivity.class);
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
                    case "notice":
                        mIntent = new Intent(context, NoticeActivity.class);
                        mIntent.putExtra("v_name", MyBoatsFragment.selected_item_name);
                        mIntent.putExtra("action","edit");
                        startActivity(mIntent);
                        break;
                    case "info":
                        mIntent = new Intent(context, DeviceInfoActivity.class);
                        startActivity(mIntent);
                        break;
                    case "group_devices":   //show group device list
                        mIntent = new Intent(context, GroupDeviceList.class);
                        startActivity(mIntent);
                        break;
                    case "group_rename":  //remove
                        mIntent = new Intent(context, NewGroupForm.class);
                        mIntent.putExtra("action","edit");
                        startActivityForResult(mIntent,6);
                        break;
                    case "group_delete":
                        mIntent = new Intent();
                        mIntent.putExtra("action","group_delete");
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
        String acc ="";
        try{
            Bundle extras = getIntent().getExtras();
            vname = extras.getString("vname");
            acc = extras.getString("acc");
        } catch (Exception e){
            e.getStackTrace();
            vname="";
        }
        populateMenuList(acc);
        setTitle(vname);

    }
    private void populateMenuList(String acc) {
        menuList = new ArrayList<MyMenu>();
        switch (acc){
            case "groups":
                menuNames = getResources().getStringArray(R.array.my_group_menu_items);
                menuCodes = getResources().getStringArray(R.array.my_group_menu_ids);
                menuIcons = getResources().obtainTypedArray(R.array.my_group_menu_icons);
                break;
            default:
                menuNames = getResources().getStringArray(R.array.my_menu_items);
                menuCodes = getResources().getStringArray(R.array.my_menu_ids);
                menuIcons = getResources().obtainTypedArray(R.array.my_menu_icons);
                break;
        }
        for(int i = 0; i < menuCodes.length; i++){
            menuList.add(new MyMenu(menuNames[i], menuCodes[i], menuIcons.getDrawable(i)));
        }
        adapter = new MyMenuAdapter(this, menuList);
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
