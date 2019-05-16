package com.asif.followme.TripPlan;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.asif.followme.R;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.model.Data_vtypes;
import com.asif.followme.util.AppUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 1/6/2018.
 */

public class NewPaxForm extends AppCompatActivity implements View.OnClickListener {
    public static String RESULT_IDS = "my_menu_ids";
    Context context;
    public Button addButton,cancelButton;
    public EditText pax_input_single,pax_input_multiple,contact_input;
    private Spinner fromSpinner,toSpinner,bookingStatusSpinner;
    private LinearLayout pax_multi_layout,pax_single_layout, body_layout;
    private TextView error_label;
 //   public CheckBox checkBox_cl,checkBox_hs,checkBox_ds,checkBox_as,checkBox_tp, checkBox_bh;
 //   public TextView shareLabel1;
    private AsyncTask<Void, Void, String> SharedPostAsyncTask;
//    private AsyncTask<Void, Void, String> SharedReadAsyncTask;
    private Dialog sDialog;
    public static String shared_user_id,shared_user_email,action;
    private addPassengers postTask = null;
    private GetPaxInfo getPaxInfo = null;
    private int trip_id = 0;
    ArrayAdapter<Data_vtypes> from_adapter;
    ArrayAdapter<Data_vtypes> to_adapter;
    ArrayAdapter<Data_vtypes> booking_status_adapter;
    int pax_id=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_new_pax_form);
        context = this;

        addButton = (Button) findViewById(R.id.addButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        pax_input_multiple = (EditText) findViewById(R.id.pax_input_multiple);
        pax_input_single = (EditText) findViewById(R.id.pax_input_single);
        contact_input = (EditText) findViewById(R.id.contact_input);
        fromSpinner = (Spinner) findViewById(R.id.from_spinner);
        toSpinner = (Spinner) findViewById(R.id.to_spinner);
        bookingStatusSpinner = (Spinner) findViewById(R.id.booking_status_spinner);
        pax_single_layout = (LinearLayout) findViewById(R.id.pax_single_layout);
        pax_multi_layout = (LinearLayout) findViewById(R.id.pax_multiple_layout);
        body_layout = (LinearLayout) findViewById(R.id.body_layout);
        error_label = (TextView) findViewById(R.id.error_label);
 //       share_email = new EditText(new ContextThemeWrapper(this, R.style.App_EditTextDisable), null, 0);
 //       shareLabel1 = (TextView) findViewById(R.id.share_label1);
        addButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });


    }
    @Override
    public void onStart() {
        super.onStart();
        String vname = "Opt...";
        trip_id=TripPlanFragment.selected_item_id;
        pax_id=0;

        try{
            Bundle extras = getIntent().getExtras();
            pax_id=extras.getInt("pax_id");
            System.out.println("Pax id: "+pax_id+" ........................................");
        } catch (Exception e){
            vname="";
        }
        if(pax_id==0){
            pax_single_layout.setVisibility(View.GONE);
            pax_multi_layout.setVisibility(View.VISIBLE);
            setTitle("New Passenger");
        } else {
            pax_single_layout.setVisibility(View.VISIBLE);
            pax_multi_layout.setVisibility(View.GONE);
            setTitle("Edit Passenger");
            addButton.setText("Save");
        }
        getPaxInfo = new GetPaxInfo(context,trip_id, pax_id);
        getPaxInfo.execute((Void) null);
    }
    @Override
    public void onClick(View view) {
        System.out.println("Clicked:"+view.getId());
        switch (view.getId() /*to get clicked view id**/) {
            case R.id.addButton:
                String pax_names ="";
                if(pax_id==0) {
                    pax_names = pax_input_multiple.getText().toString();
                    if (pax_input_multiple.getText().toString().equals("")) {
                        pax_input_multiple.setError("Please Enter at least 1 passenger");
                        return;
                    }
                } else {
                    pax_names = pax_input_single.getText().toString();
                    if (pax_input_single.getText().toString().equals("")) {
                        pax_input_single.setError("Please Enter passenger Name");
                        return;
                    }
                }
//              String pax_names = pax_input.getText().toString();
                int trip_id= TripPlanFragment.selected_item_id;
                int index_f = fromSpinner.getSelectedItemPosition();
                int index_t = toSpinner.getSelectedItemPosition();
                int index_bs = bookingStatusSpinner.getSelectedItemPosition();
                int bfrom = from_adapter.getItem(index_f).getId();
                int bto = to_adapter.getItem(index_t).getId();
                int b_status = booking_status_adapter.getItem(index_bs).getId();
                String contact = contact_input.getText().toString();
                postTask = new addPassengers("save",pax_id,trip_id,pax_names,bfrom, bto,contact,b_status);
                postTask.execute((Void) null);

                break;
            case R.id.cancelButton:
                finish();
                break;
        }

    }

    public class addPassengers extends AsyncTask<Void, Void, String> {
        private final int pax_id;
        private final String pax_names;
        private final int trip_id;
        private final  String contact;
        private Context con;
        private final String action;
        private int b_from;
        private  int b_to;
        private int b_status;

        addPassengers(String action, int pax_id,int trip_id, String pax_names, int b_from, int b_to, String contact, int b_status) {
            this.pax_id=pax_id;
            this.pax_names = pax_names;
            this.trip_id = trip_id;
            this.contact = contact;
            this.action = action;
            this.b_from = b_from;
            this.b_to = b_to;
            this.b_status = b_status;
        }
        @Override
        protected void onPreExecute() {
        //    swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
        //    swipeLayout.setRefreshing(true);
            sDialog= ProgressDialog.show(NewPaxForm.this, null, "Posting, Please wait...");
            sDialog.show();

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            try{
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.addPassengers(context,action, pax_id,trip_id,pax_names,b_from, b_to,contact,b_status);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onCancelled() {
            if(sDialog.isShowing()){
                sDialog.dismiss();
            }
//			System.out.println("Cancelled.....................");
//		    onMyAsyncTaskCompletedListener(null);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(sDialog.isShowing()){
                sDialog.dismiss();
            }
            //System.out.println(result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        if (jObj.getString("status").equalsIgnoreCase("ok")) {
                            //Intent intent = new Intent();
                            //intent.putExtra("sender", "edit");
                           // intent.putExtra("reload", true);
                           // setResult(RESULT_OK, intent);
                            finish();

                        } else {
                            //share_email_new.setError(jObj.getString("error"));
                            //AppUtils.showAlertDialog(getApplicationContext(), "Error", jObj.getString("error"));
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                        //	System.out.println("Result"+result);

                    }
                } catch (JSONException e) {
                    //loading.setText("Unable to reach Server...");
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

            }
        }
    }



    public class GetPaxInfo extends AsyncTask<Void, Void, String> {
        private int trip_id =0;
        private int pax_id =0;
        GetPaxInfo(Context context, int trip_id, int pax_id){
            this.trip_id=trip_id;
            this.pax_id=pax_id;
        }
            @Override
            protected void onPreExecute() {
              //  sDialog =ProgressDialog.show(context, null, "Please wait...");
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(Void... arg0) {
                try{
                    ContentParser parser = new ContentParser(getBaseContext());
                    return parser.getPaxInfo(context,trip_id,pax_id);
                    //GetText();
                }
                catch(Exception ex)
                {
                    //finish();
//				System.out.println("err??????????????????????");
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                body_layout.setVisibility(View.VISIBLE);
                error_label.setVisibility(View.GONE);
            //    if(sDialog.isShowing()){
            //        sDialog.dismiss();
            //    }
                System.out.println(result);
                //		SettingsPreferences.setSelectedAccount(context, id2);
                if (!TextUtils.isEmpty(result)) {
                    try {
                        JSONObject jObj = new JSONObject(result);
                        if (jObj.has("status")) {
                            if (jObj.getString("status").equalsIgnoreCase("ok")) {
                                from_adapter = new ArrayAdapter<Data_vtypes>(getBaseContext(), android.R.layout.simple_spinner_item, AppUtils.setBookingsFromSpinner(jObj, getBaseContext()));
                                to_adapter = new ArrayAdapter<Data_vtypes>(getBaseContext(), android.R.layout.simple_spinner_item, AppUtils.setBookingsToSpinner(jObj, getBaseContext()));
                                booking_status_adapter = new ArrayAdapter<Data_vtypes>(getBaseContext(), android.R.layout.simple_spinner_item, AppUtils.setBookingsStatusSpinner(jObj, getBaseContext()));
                                from_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                to_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                booking_status_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                fromSpinner.setAdapter(from_adapter);
                                toSpinner.setAdapter(to_adapter);
                                bookingStatusSpinner.setAdapter(booking_status_adapter);

                                if(pax_id > 0){ //edit attempt, populate fields & spinners
                                    String contact = jObj.getString("contact");
                                    String bto = jObj.getString("bto");
                                    String bfrom = jObj.getString("bfrom");
                                    contact_input.setText(contact);
                                    pax_input_single.setText(jObj.getString("name"));

                                    for (int position=0; position < from_adapter.getCount(); position++) {
                                        //System.out.println("Spinner ID:"+from_adapter.getItem(position).getId()+", Received:"+jObj.getInt("bfrom"));
                                        if (from_adapter.getItem(position).getId() == jObj.getInt("bfrom")) {
                                            fromSpinner.setSelection(position);
                                            //int ind = from_adapter.getItem(position).getIndex();
                                            //System.out.println("Index:"+ind);
                                            //fromSpinner.setSelection(from_adapter.getItem(position).getIndex());
                                        }
                                    }
                                    for (int position=0; position < to_adapter.getCount(); position++) {
                                        System.out.println("Spinner ID:"+to_adapter.getItem(position).getId()+", Received:"+jObj.getInt("bto"));
                                        if (to_adapter.getItem(position).getId() == jObj.getInt("bto")) {
                                            toSpinner.setSelection(position);
                                            //int ind2 = to_adapter.getItemId(position);
                                            //System.out.println("Index:"+ind2);
                                            //toSpinner.setSelection(to_adapter.getItem(position).getIndex());
                                        }
                                    }

                                } else {
                                    pax_input_multiple.setText("");


                                }

                                //int access=jObj.getInt("access");
                                //share_email.setText(access);
								//share_email = (EditText) shareDialog.findViewById(R.id.share_email);
						//		share_email_edit.setText(shared_user_email);
								//share_email.setTextAppearance(R.style.App_EditTextDisable);
						//		share_email_edit.setEnabled(false);

	//							shareLabel1.setVisibility(View.GONE);

          //                      shareDialog.setTitle("Change Permission");
         //                       shareDialog.show();

                            } else {
                                //AppUtils.showAlertDialog(context, "Error", jObj.getString("error"));
                                //showAlert(context,"Error",jObj.getString("error"));
                                //alert.show();
                                Toast.makeText(getApplicationContext(), jObj.getString("error"), Toast.LENGTH_LONG).show();
                            }


                        } else {
                            Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        //loading.setText("Unable to reach Server...");
                        Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

                }
            }
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
