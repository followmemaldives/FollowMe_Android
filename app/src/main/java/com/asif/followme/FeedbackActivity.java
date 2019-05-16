package com.asif.followme;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class FeedbackActivity extends AppCompatActivity implements OnClickListener {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private FeedbackTask feedbackTask = null;

    private Context context;
    EditText yourName, yourEmail,yourFeedback, yourContact;
    ProgressDialog sDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_form);
        context = this;
        // Set up the login form.
        //mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        //populateAutoComplete();


        Button btnSendFeedback = (Button) findViewById(R.id.btn_send_feedback);
        yourName =(EditText) findViewById (R.id.your_name);
        yourContact = (EditText) findViewById(R.id.your_contact);
        yourEmail =(EditText) findViewById (R.id.your_email);
        yourFeedback =(EditText) findViewById (R.id.your_feedback);


        btnSendFeedback.setOnClickListener(this);

        //mLoginFormView = findViewById(R.id.login_form);
        //mProgressView = findViewById(R.id.login_progress);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_send_feedback:
                attemptFeedback();
                break;
        }
    }


    private void attemptFeedback() {
        if (feedbackTask != null) {
            return;
        }

        // Reset errors.
        yourName.setError(null);
        yourContact.setError(null);
        yourEmail.setError(null);
        yourFeedback.setError(null);

        // Store values at the time of the login attempt.
        String name = yourName.getText().toString();
        String contact = yourContact.getText().toString();
        String email = yourEmail.getText().toString();
        String feedback = yourFeedback.getText().toString();
        String country = "";

        boolean cancel = false;
        View focusView = null;
        System.out.println("Name:"+name+":");

        if (feedback.length()==0) {
            yourFeedback.setError("Enter your feedback");
            focusView = yourFeedback;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            feedbackTask = new FeedbackTask(name,contact,email, feedback);
            feedbackTask.execute((Void) null);
//            Toast.makeText(getApplicationContext(), "Lets Submit Registration", Toast.LENGTH_LONG).show();

        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }






    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class FeedbackTask extends AsyncTask<Void, Void, String> {

        private final String name;
        private final String contact;
        private final String email;
        private final String feedback;

        FeedbackTask(String name, String contact, String email, String feedback) {
            this.name = name;
            this.contact = contact;
            this.email = email;
            this.feedback = feedback;

        }
        @Override
        protected void onPreExecute() {
            sDialog= ProgressDialog.show(context, null, "Please wait...");
            sDialog.show();
            super.onPreExecute();

        }
        @Override
        protected String doInBackground(Void... arg0) {

            try{
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.sendFeedback(context,name, contact, email, feedback);
            }
            catch(Exception ex)
            {
                //finish();
            }
            return null;

        }

        @Override
        protected void onPostExecute(final String result) {
            feedbackTask = null;
            if(sDialog.isShowing()){
                sDialog.dismiss();
            }
           // showProgress(false);

            System.out.println("Login REsult: "+result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
//					System.out.println("SID:"+jObj.getString("sid"));
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            Intent intent = getIntent();
                            intent.putExtra("action", "feedback_ok");
                            setResult(RESULT_OK,intent );

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
            feedbackTask = null;
           // showProgress(false);
        }
    }
}

