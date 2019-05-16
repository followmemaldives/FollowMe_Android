package com.asif.followme.BoatHire.Public;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.asif.followme.R;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.AppUtils;
import com.asif.followme.util.SettingsPreferences;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;


public class ContactVerifyForm extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = ContactVerifyForm.class.toString();
    Context context;
    Button btnVerify;
    private LinearLayout securityLayout;
    private TextView contactNumber,securityNumber;
    private verifyTask mVerifyTask = null;
    ProgressDialog dialog;
    private static final String PHONE_NUMBER_GARBAGE_REGEX = "[()\\s-]+";
    private static final String PHONE_NUMBER_REGEX = "^[7,9]{1}?[0-9]{6}$";
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(PHONE_NUMBER_REGEX);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_verify_form);
        // butter knife
        context = this;

        //View view = layoutCheckbox.getChildAt(0);
        //FlexboxLayout.LayoutParams lp = (FlexboxLayout.LayoutParams) view.getLayoutParams();
        //lp.order = -1;
        //lp.flexGrow = 2;
        //view.setLayoutParams(lp);


        dialog = new ProgressDialog(ContactVerifyForm.this);
        contactNumber = (EditText) findViewById(R.id.contact_number);
        securityNumber = (EditText) findViewById(R.id.security_number);
        securityLayout = (LinearLayout) findViewById(R.id.security_layout);
        btnVerify = (Button) findViewById(R.id.btn_verify);
        btnVerify.setOnClickListener(this);
        mVerifyTask = new verifyTask("get_mobile","", "Loading...");
        mVerifyTask.execute((Void) null);

    }


    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        System.out.println(v.getId());
        System.out.println("OnClick");
        switch (v.getId()) {
            case R.id.btn_verify:
                verifyContact();
               // showDateTimePicker();
            //    break;

        }
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        return phoneNumber != null && PHONE_NUMBER_PATTERN.matcher(phoneNumber.replaceAll(PHONE_NUMBER_GARBAGE_REGEX, "")).matches();
    }

    public void verifyContact() {
 //       InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        String mobile = contactNumber.getText().toString();
        String code = securityNumber.getText().toString();
        if(validatePhoneNumber(mobile)){
            System.out.println("Valid.....");
        } else {
            contactNumber.setError("The mobile number is invalid");
            return;
        }
        if(code.length() > 4){
            mVerifyTask = new verifyTask("confirm_sms_code", code,"Verifying your Contact...");
            mVerifyTask.execute((Void) null);
        } else {
            mVerifyTask = new verifyTask("send_sms", mobile, "Sending SMS...");
            mVerifyTask.execute((Void) null);
        }

    }

/*    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        //Log.v(TAG, "The choosen one " + date.getTime());
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }
*/






    public class verifyTask extends AsyncTask<Void, Void, String> {
        String action;
        String mobile;
        String message;

        verifyTask(String action,String mobile, String message) {
            this.action=action;
            this.mobile = mobile;
            this.message = message;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage(message);
            dialog.show();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO: attempt authentication against a network service.

            try {
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.getAJAX(context, action, mobile, "","");
            } catch (Exception ex) {
                //finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String result) {
            mVerifyTask = null;
            dialog.dismiss();
            //showProgress(false);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            if(action.equalsIgnoreCase("get_mobile")){
                                String m = jObj.getString("mobile");
                                contactNumber.setText(m);
                            }
                            if(action.equalsIgnoreCase("send_sms")){
                                securityLayout.setVisibility(View.VISIBLE);
                                contactNumber.setEnabled(false);
                                AppUtils.showAlertDialog(context, "Confirmation SMS", "An SMS had been sent to the given phone number. Please enter the 6 digit number received to confirm your contact number.");
                            }

                            if(action.equalsIgnoreCase("confirm_sms_code")){
                                SettingsPreferences.setContactVerified(context,true);
                                Intent intent = getIntent();
                                intent.putExtra("action", "verify_ok");
                                setResult(RESULT_OK,intent );
                                finish();

                            }
                        /*    Intent intent = getIntent();
                            intent.putExtra("action", "reload");
                            setResult(RESULT_OK,intent );
                            finish();
                        */
                        } else {
                            String errmsg = jObj.getString("error");
                            AppUtils.showAlertDialog(context, "Error", errmsg);
//							Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_LONG).show();
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
            mVerifyTask = null;
            //showProgress(false);
        }
    }

}
