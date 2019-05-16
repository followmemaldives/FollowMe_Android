package com.asif.followme;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.AppUtils;
import com.asif.followme.util.SettingsPreferences;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class ProfileActivity extends AppCompatActivity implements OnClickListener {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    private Context context;
    EditText yourName, yourEmail,yourPass, yourContact;
    private Spinner spinnerCountry;
    private ProfileTask mProfileTask = null;
    ProgressDialog dialog;
    private Button btnSave;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_form);
        context = this;
        // Set up the login form.
        //mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        //populateAutoComplete();


        btnSave = (Button) findViewById(R.id.btn_save_now);
        yourName =(EditText) findViewById (R.id.your_name);
        yourContact = (EditText) findViewById(R.id.your_contact);
        yourEmail =(EditText) findViewById (R.id.your_email);
        yourPass =(EditText) findViewById (R.id.your_password);
        spinnerCountry = (Spinner) findViewById(R.id.spinner_country);
        btnSave.setOnClickListener(this);

        ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(this, R.array.country_values, android.R.layout.simple_spinner_item);
        //spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // spinnerCountry.setAdapter(spinner_adapter);
            int spinnerPosition = spinner_adapter.getPosition(SettingsPreferences.getCountry(context));
            System.out.println("Position:"+spinnerPosition);
           spinnerCountry.setSelection(spinnerPosition);

        mProfileTask = new ProfileTask("read","", "", "","","");
        mProfileTask.execute((Void) null);

        //mLoginFormView = findViewById(R.id.login_form);
        //mProgressView = findViewById(R.id.login_progress);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_save_now:
                attemptSave();
                break;
        }
    }


    private void attemptSave() {
        System.out.println("Attempt to Save...");
        // Reset errors.
        yourName.setError(null);
        yourContact.setError(null);
        yourEmail.setError(null);
        yourPass.setError(null);

        // Store values at the time of the login attempt.
        String name = yourName.getText().toString();
        String contact = yourContact.getText().toString();
        String email = yourEmail.getText().toString();
        String password = yourPass.getText().toString();
        int country_pos = spinnerCountry.getSelectedItemPosition();
        ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(this, R.array.country_values, android.R.layout.simple_spinner_item);
        String country = spinner_adapter.getItem(country_pos).toString();

        // Check for a valid password, if the user entered one.
        if(name.length()==0){
            yourName.setError("Please type Your Name");
            return;
        }
        if (contact.length()==0 && email.length()==0) {
            yourContact.setError("Please enter Your Phone Number or Email");
            return;
        } else {
            // Check for a valid email address.
            if (!TextUtils.isEmpty(email)) {
                if (!AppUtils.isEmailValid(email)) {
                    yourEmail.setError(getString(R.string.error_invalid_email));
                    return;
                }
            }

        }

        if (password.length() > 0){
            if(!AppUtils.isPasswordValid(password)) {
                yourPass.setError("Enter at least 5 character password");
                return;
            }
        }


//            focusView.requestFocus();
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            mProfileTask = new ProfileTask("save",name, email, contact, password, country);
            mProfileTask.execute((Void) null);
//            Toast.makeText(getApplicationContext(), "Lets Submit Registration", Toast.LENGTH_LONG).show();
    }






    public class ProfileTask extends AsyncTask<Void, Void, String> {
        String action;
        String name;
        String email;
        String contact;
        String pwd;
        String co;
        ProfileTask(String action, String name,String email,String contact, String pwd, String co) {
            this.action = action;
            this.name = name;
            this.email = email;
            this.contact = contact;
            this.pwd = pwd;
            this.co = co;
        }

        @Override
        protected void onPreExecute() {
            //if(alertSoundCheckBox.isChecked() || alertVibrateCheckBox.isChecked()){
            dialog=ProgressDialog.show(context, "Please wait", "Reading Preferences...");
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            try{
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.userProfileInfo(context,action, name, email, contact, pwd, co);
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
            if(dialog.isShowing())
            {
                dialog.dismiss();
            }
            if (!TextUtils.isEmpty(result)) {
//				System.out.println(result);
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        if (jObj.getString("status").equalsIgnoreCase("ok")) {
                            if(action.equalsIgnoreCase("read")) {
                                yourName.setText(jObj.getString("name"));
                                yourContact.setText(jObj.getString("phone"));
                                yourEmail.setText(jObj.getString("email"));
                            }
                            if(action.equalsIgnoreCase("save")){
                                SettingsPreferences.setUserName(context, email);
                                if(pwd.length()>0) {
                                    SettingsPreferences.setPassword(context, pwd);
                                }

                                finish();

                            }
                        } else {
                            String errmsg = jObj.getString("error");
                            //AppUtils.showAlertDialog(context, "Error", errmsg);
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Error")
                                    .setMessage(errmsg)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                            //Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
//					Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }

}
