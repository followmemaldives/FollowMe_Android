package com.asif.followme;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.asif.followme.MyAccount.MyActivity;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.AppUtils;
import com.asif.followme.util.SettingsPreferences;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity implements OnClickListener {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserRegisterTask mRegisterTask = null;

    private Context context;
    EditText yourName, yourEmail,yourPass, yourContact;
    private Spinner spinnerCountry;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_form);
        context = this;
        // Set up the login form.
        //mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        //populateAutoComplete();


        Button btnRegister = (Button) findViewById(R.id.btn_register_now);
        yourName =(EditText) findViewById (R.id.your_name);
        yourContact = (EditText) findViewById(R.id.your_contact);
        yourEmail =(EditText) findViewById (R.id.your_email);
        yourPass =(EditText) findViewById (R.id.your_password);
        spinnerCountry = (Spinner) findViewById(R.id.spinner_country);

        ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(this, R.array.country_values, android.R.layout.simple_spinner_item);
        //spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // spinnerCountry.setAdapter(spinner_adapter);
            int spinnerPosition = spinner_adapter.getPosition(SettingsPreferences.getCountry(context));
            System.out.println("Position:"+spinnerPosition);
           spinnerCountry.setSelection(spinnerPosition);

        btnRegister.setOnClickListener(this);

        //mLoginFormView = findViewById(R.id.login_form);
        //mProgressView = findViewById(R.id.login_progress);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_register_now:
                attemptRegister();
                break;
        }
    }


    private void attemptRegister() {
        if (mRegisterTask != null) {
            return;
        }

        // Reset errors.
        yourName.setError(null);
        yourContact.setError(null);
        yourEmail.setError(null);
        yourPass.setError(null);

        // Store values at the time of the login attempt.
        String name = yourName.getText().toString();
        String contact = yourContact.getText().toString();
        String email = yourEmail.getText().toString().trim();
        String password = yourPass.getText().toString().trim();
        int country_pos = spinnerCountry.getSelectedItemPosition();
        ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(this, R.array.country_values, android.R.layout.simple_spinner_item);
        String country = spinner_adapter.getItem(country_pos).toString();

        boolean cancel = false;
        View focusView = null;
        System.out.println("Name:"+name+":");

        // Check for a valid password, if the user entered one.
        if(name.length()==0){
            yourName.setError("Please type Your Name");
            yourName.requestFocus();
//            focusView = yourName;
            return;
        }
        if (contact.length()==0) {
            yourContact.setError("Please enter Your Contact Number");
            yourContact.requestFocus();
//            focusView = yourContact;
            return;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            yourEmail.setError("Please type Your Email");
            yourEmail.requestFocus();
//            focusView = yourEmail;
            return;
        } else if (!AppUtils.isEmailValid(email)) {
            yourEmail.setError(getString(R.string.error_invalid_email));
            yourEmail.requestFocus();
//            focusView = yourEmail;
            return;
        }
        if (password.length()==0 || !AppUtils.isPasswordValid(password)) {
            yourPass.setError("Enter at least 5 character password");
            yourPass.requestFocus();
//            focusView = yourPass;
            return;
        }


//            focusView.requestFocus();
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            mRegisterTask = new UserRegisterTask(name,contact,email, password, country);
            mRegisterTask.execute((Void) null);
//            Toast.makeText(getApplicationContext(), "Lets Submit Registration", Toast.LENGTH_LONG).show();
    }






    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, String> {

        private final String name;
        private final String contact;
        private final String email;
        private final String pwd;
        private final String country;

        UserRegisterTask(String name, String contact, String email, String password, String country) {
            this.name = name;
            this.contact = contact;
            this.email = email;
            this.pwd = password;
            this.country = country;
        }

        @Override
        protected String doInBackground(Void... arg0) {

            try{
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.registerUser(context,name, contact, email, pwd, country);
            }
            catch(Exception ex)
            {
                //finish();
            }
            return null;

        }

        @Override
        protected void onPostExecute(final String result) {
            mRegisterTask = null;
           // showProgress(false);

            System.out.println("Login REsult: "+result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
//					System.out.println("SID:"+jObj.getString("sid"));
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            SettingsPreferences.setUserName(context, email);
                            SettingsPreferences.setPassword(context, pwd);
//                            SettingsPreferences.setSessionID(context,jObj.getString("sid"));

                           // AppUtils.saveMyVessel(jObj, context);
                            Intent intent = new Intent(context, MyActivity.class);
                            startActivity(intent);
                            LoginActivity.loginActivity.finish();
                            finish();
                            //    SettingsPreferences.setUserName(context, uname);
                        //    SettingsPreferences.setPassword(context, pwd);


                        } else {
                            String errmsg = jObj.getString("error");
                            AppUtils.showAlertDialog(context, "Error", errmsg);
//							Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Unable to reach server. Please check your internet connection", Toast.LENGTH_LONG).show();
                //AppUtils.showAlertDialog(context, "Timeout", "Unable to reach Server. Please try again!");
            }








         /*   if (result=="") {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }

            */


        }

        @Override
        protected void onCancelled() {
            mRegisterTask = null;
           // showProgress(false);
        }
    }
}

