package com.asif.followme.TripPlan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class PaxFilterForm extends Activity implements View.OnClickListener {
//    public static String RESULT_IDS = "my_menu_ids";
    Context context;
    public Button filterButton,cancelButton;
//    public EditText pax_input_single,pax_input_multiple,contact_input;
    private Spinner fromSpinner,toSpinner,bookingStatusSpinner;
    private LinearLayout body_layout;
    private TextView error_label;
    private AsyncTask<Void, Void, String> SharedPostAsyncTask;
//    private AsyncTask<Void, Void, String> SharedReadAsyncTask;
    private Dialog sDialog;
    public static String shared_user_id,shared_user_email,action;
//    private addPassengers postTask = null;
    private GetPaxDestinations getPaxDestinations = null;
    private int trip_id = 0;
    ArrayAdapter<Data_vtypes> from_adapter;
    ArrayAdapter<Data_vtypes> to_adapter;
    ArrayAdapter<Data_vtypes> booking_status_adapter;
    int pax_id=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_pax_filter_form);
        context = this;

        filterButton = (Button) findViewById(R.id.filterButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        fromSpinner = (Spinner) findViewById(R.id.from_spinner);
        toSpinner = (Spinner) findViewById(R.id.to_spinner);
        bookingStatusSpinner = (Spinner) findViewById(R.id.booking_status_spinner);
        body_layout = (LinearLayout) findViewById(R.id.body_layout);
        error_label = (TextView) findViewById(R.id.error_label);
 //       share_email = new EditText(new ContextThemeWrapper(this, R.style.App_EditTextDisable), null, 0);
 //       shareLabel1 = (TextView) findViewById(R.id.share_label1);
        filterButton.setOnClickListener(this);
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

        setTitle("Filter");
        getPaxDestinations = new GetPaxDestinations(context,trip_id);
        getPaxDestinations.execute((Void) null);
    }
    @Override
    public void onClick(View view) {
        System.out.println("Clicked:"+view.getId());
        switch (view.getId() /*to get clicked view id**/) {
            case R.id.filterButton:
                //int trip_id= TripPlanFragment.selected_item_id;
                int index_f = fromSpinner.getSelectedItemPosition();
                int index_t = toSpinner.getSelectedItemPosition();
                int index_bs = bookingStatusSpinner.getSelectedItemPosition();
                int bfrom = from_adapter.getItem(index_f).getId();
                int bto = to_adapter.getItem(index_t).getId();
                int bstatus = booking_status_adapter.getItem(index_bs).getId();
                System.out.println("Bfrom:"+bfrom+",Bto:"+bto+",Bstatus:"+bstatus);
                Intent intent = new Intent();
                //intent.putExtra("action","filter_dest");
                intent.putExtra("bfrom",bfrom);
                intent.putExtra("bto",bto);
                intent.putExtra("bstatus",bstatus);
                setResult(RESULT_OK,intent );
                finish();
                break;
            case R.id.cancelButton:
                finish();
                break;
        }

    }

/*    public class addPassengers extends AsyncTask<Void, Void, String> {
        private final String pax;
        private final int trip_id;
        private final  String contact;
        private Context con;
        private final String action;
        private int b_from;
        private  int b_to;
        private int b_status;

        addPassengers(String action, String pax,int trip_id, int b_from, int b_to, String contact, int b_status) {
            this.pax = pax;
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
            sDialog= ProgressDialog.show(PaxFilterForm.this, null, "Posting, Please wait...");
            sDialog.show();

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            try{
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.addPassengers(context,action, pax_id,trip_id,pax,b_from, b_to,contact,b_status);
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
                            Intent intent = new Intent();
                            intent.putExtra("reload", true);
                            setResult(RESULT_OK, intent);
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

*/

    public class GetPaxDestinations extends AsyncTask<Void, Void, String> {
        private int trip_id =0;
        GetPaxDestinations(Context context, int trip_id){
            this.trip_id=trip_id;
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
                    return parser.getPaxDestinations(context,trip_id);
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
                                from_adapter = new ArrayAdapter<Data_vtypes>(getBaseContext(), android.R.layout.simple_spinner_item, AppUtils.setBookingsFromSpinnerAny(jObj, getBaseContext()));
                                to_adapter = new ArrayAdapter<Data_vtypes>(getBaseContext(), android.R.layout.simple_spinner_item, AppUtils.setBookingsToSpinnerAny(jObj, getBaseContext()));
                                booking_status_adapter = new ArrayAdapter<Data_vtypes>(getBaseContext(), android.R.layout.simple_spinner_item, AppUtils.setBookingsStatusSpinnerAny(jObj, getBaseContext()));
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
