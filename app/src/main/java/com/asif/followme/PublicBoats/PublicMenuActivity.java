package com.asif.followme.PublicBoats;

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

public class PublicMenuActivity extends ListActivity {
    public static String RESULT_IDS = "my_menu_ids";
    public String[] countrynames, countrycodes;
    private TypedArray imgs;
    private List<MyMenu> menuList;
    Context context;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateCountryList();
        ArrayAdapter<MyMenu> adapter = new PublicMenuAdapter(this, menuList);
        setListAdapter(adapter);
        context = this;
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyMenu c = menuList.get(position);
                Intent returnIntent = new Intent();
                //returnIntent.putExtra(RESULT_IDS, c.getCode());
                System.out.println("Code:"+c.getCode()+"-----------------------------------------------------");
                Intent mIntent;
                switch(c.getCode()) {
                    case "share":
                        Intent intent = new Intent();
                        intent.putExtra("action","share");
                        setResult(RESULT_OK,intent );
                        finish();
                        break;
                    case "alert_me":
                        mIntent = new Intent();
                        mIntent.putExtra("action","alert_me");
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
        menuList = new ArrayList<MyMenu>();
        countrynames = getResources().getStringArray(R.array.public_menu_items);
        countrycodes = getResources().getStringArray(R.array.public_menu_ids);
        imgs = getResources().obtainTypedArray(R.array.public_menu_icons);
        for(int i = 0; i < countrycodes.length; i++){
            menuList.add(new MyMenu(countrynames[i], countrycodes[i], imgs.getDrawable(i)));
        }
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
