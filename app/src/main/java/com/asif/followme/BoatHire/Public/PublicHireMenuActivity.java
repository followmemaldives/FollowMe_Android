package com.asif.followme.BoatHire.Public;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.asif.followme.R;
import com.asif.followme.manager.ContentParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 1/6/2018.
 */

public class PublicHireMenuActivity extends ListActivity {
    public static String RESULT_IDS = "my_menu_ids";
    public String[] menuNames, menuCodes;
    private TypedArray imgs;
    private List<MyMenu> menuList;
    Context context;
    Dialog dialog;
    private getBoatHireStatusTask mHireTask = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // populateBoatHireMenu();
    //    ArrayAdapter<MyMenu> adapter = new MyBoatHireMenuAdapter(this, menuList);
    //    setListAdapter(adapter);
        context = this;
        mHireTask = new getBoatHireStatusTask("my_trip_boats");
        mHireTask.execute((Void) null);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyMenu c = menuList.get(position);
                Intent returnIntent = new Intent();
                //returnIntent.putExtra(RESULT_IDS, c.getCode());
                System.out.println("Code:"+c.getCode()+"-----------------------------------------------------");
                Intent mIntent;
                Intent intent = new Intent();
                switch(c.getCode()) {
                    case "edit":    //only on draft
                        mIntent = new Intent(context, NewHireForm.class);
                        mIntent.putExtra("hire_id", Integer.parseInt(PublicHireFragment.selected_item_id));
                        startActivity(mIntent);
                        break;
                    case "publish":  // publish
                        intent.putExtra("action","hire_publish");
                        setResult(RESULT_OK,intent );
                        finish();
                        break;
                    case "cancel":  //published items which has bids/should not delete
                        intent = new Intent();
                        intent.putExtra("action","hire_cancel");
                        setResult(RESULT_OK,intent );
                        finish();
                        break;
                    case "unpublish":  //published items which has bids/should not delete
                        intent = new Intent();
                        intent.putExtra("action","hire_unpublish");
                        setResult(RESULT_OK,intent );
                        finish();
                        break;
                    case "delete":  //no bids, safe to delete
                        intent.putExtra("action","hire_delete");
                        setResult(RESULT_OK,intent );
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
        try{
            Bundle extras = getIntent().getExtras();
            vname = extras.getString("vname");
        } catch (Exception e){
            vname="";
        }
        setTitle(vname);

    }
    private void populateBoatHireMenu(String action, int state, int bids, int bidState) {
        System.out.println("populateBoatHireMenu("+state+")");
        menuList = new ArrayList<MyMenu>();
        if(action.equalsIgnoreCase("get_hire_status")) {
            switch (state) {
                case 1: //draft
                    menuNames = getResources().getStringArray(R.array.my_boat_hire_menu_items_1);
                    menuCodes = getResources().getStringArray(R.array.my_boat_hire_menu_ids_1);
                    imgs = getResources().obtainTypedArray(R.array.my_boat_hire_menu_icons_1);
                    break;
                case 2: //published
                    switch(bidState){
                        case 1: //started bidding, not awarded
                            menuNames = getResources().getStringArray(R.array.my_boat_hire_menu_items_21);
                            menuCodes = getResources().getStringArray(R.array.my_boat_hire_menu_ids_21);
                            imgs = getResources().obtainTypedArray(R.array.my_boat_hire_menu_icons_21);
                            break;
                        case 2: // awarded.. allow cancel bid, reject ??
                            menuNames = getResources().getStringArray(R.array.my_boat_hire_menu_items_21);
                            menuCodes = getResources().getStringArray(R.array.my_boat_hire_menu_ids_21);
                            imgs = getResources().obtainTypedArray(R.array.my_boat_hire_menu_icons_21);
                            break;
                        case 3: // confirmed/accepted
                            menuNames = getResources().getStringArray(R.array.my_boat_hire_menu_items_23);
                            menuCodes = getResources().getStringArray(R.array.my_boat_hire_menu_ids_23);
                            imgs = getResources().obtainTypedArray(R.array.my_boat_hire_menu_icons_23);
                            break;
                        default:  // assume this will be 0, no bids allow unpublish
                            menuNames = getResources().getStringArray(R.array.my_boat_hire_menu_items_20);
                            menuCodes = getResources().getStringArray(R.array.my_boat_hire_menu_ids_20);
                            imgs = getResources().obtainTypedArray(R.array.my_boat_hire_menu_icons_20);

                    }
/*                    if (bids > 0) {  //started bidding, cannot delete,unpublish
                        menuNames = getResources().getStringArray(R.array.my_boat_hire_menu_items_21);
                        menuCodes = getResources().getStringArray(R.array.my_boat_hire_menu_ids_21);
                        imgs = getResources().obtainTypedArray(R.array.my_boat_hire_menu_icons_21);
                    } else {    //no bids
                        menuNames = getResources().getStringArray(R.array.my_boat_hire_menu_items_20);
                        menuCodes = getResources().getStringArray(R.array.my_boat_hire_menu_ids_20);
                        imgs = getResources().obtainTypedArray(R.array.my_boat_hire_menu_icons_20);
                    }
*/
                    break;
            }
        } else {
                menuNames = getResources().getStringArray(R.array.boat_hire_award_items);
                menuCodes = getResources().getStringArray(R.array.boat_hire_award_ids);
                imgs = getResources().obtainTypedArray(R.array.boat_hire_award_icons);
        }
        for(int i = 0; i < menuCodes.length; i++){
            menuList.add(new MyMenu(menuNames[i], menuCodes[i], imgs.getDrawable(i)));
        }
            ArrayAdapter<MyMenu> adapter = new PublicHireMenuAdapter(this, menuList);
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


    public class getBoatHireStatusTask extends AsyncTask<Void, Void, String> {
        int hire_id;
        String action;
        getBoatHireStatusTask(String pair) {
            this.hire_id = hire_id;
        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO: attempt authentication against a network service.

            try {
                ContentParser parser = new ContentParser(getBaseContext());
                    action = "get_hire_status";
                if(PublicHireActivity.HireNav > 0){
                    action = "get_award_status";
                }
                return parser.getAJAX(context, action, PublicHireFragment.selected_item_id, "0","");
            } catch (Exception ex) {
                //finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String result) {
            mHireTask = null;
            //showProgress(false);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            int state = jObj.getInt("state");
                            int bids = jObj.getInt("bids");
                            int bidState = jObj.getInt("bid_state");  //awarded,confirmed, etc..
                            populateBoatHireMenu(action,state, bids, bidState);


                            //processed at background
                            //ArrayAdapter<NameValuePairs> adapter = new ArrayAdapter<NameValuePairs>(getBaseContext(), android.R.layout.simple_spinner_item, AppUtils.setNameValuePairs(jObj, getBaseContext()));
                            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //vesselSpinner.setAdapter(adapter);


                            //ArrayAdapter<NameValuePairs> adapter2 = new ArrayAdapter<NameValuePairs>(getBaseContext(), android.R.layout.simple_spinner_item, AppUtils.setNameValuePairs(jObj.getJSONObject("islands"), getBaseContext()));
                            //multiAutoCompleteTextView.setAdapter(adapter2);
                            //multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

                            // String emailArr[] = getResources().getStringArray(R.array.atoll_list);
//                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, emailArr);
//                            multiAutoCompleteTextView.setAdapter(arrayAdapter);
//                            multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

                     /*       for (int position=0; position < adapter.getCount(); position++) {
                                if (adapter.getItem(position).getId() == 6) {   //6-person
                                    //v_type = adapter.getItem(position).getId();
                                    vesselSpinner.setSelection(adapter.getItem(position).getIndex());
                                }
                            }
                  */
                            //	deviceTypeSpinner.setSelection(((ArrayAdapter<String>)deviceTypeSpinner.getAdapter()).getPosition(jObj.getString("vtype")));
                            //	int spinnerPosition = adapter.getPosition(((ArrayAdapter<String>)mySpinner.getAdapter()).getPosition(myString));
                            // vesselTypeSpinner.setSelection(jObj.getInt("vtype")-1); //not the correct way
                            //	Data_vtypes country =  jObj.getInt("vtype");
                        } else {
                            String errmsg = jObj.getString("error");
//                            AppUtils.showAlertDialog(context, "Error", errmsg);
							Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection 1", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection 2", Toast.LENGTH_LONG).show();
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Unable to reach server. Please check your internet connection 2", Toast.LENGTH_LONG).show();
                //AppUtils.showAlertDialog(context, "Timeout", "Unable to reach Server. Please try again!");
            }


        }

        @Override
        protected void onCancelled() {
            mHireTask = null;
            //showProgress(false);
        }
    }

}
