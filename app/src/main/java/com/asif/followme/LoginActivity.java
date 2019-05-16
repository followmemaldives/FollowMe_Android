package com.asif.followme;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.asif.followme.BoatHire.Public.PublicHireActivity;
import com.asif.followme.MyAccount.MyActivity;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.AppUtils;
import com.asif.followme.util.SettingsPreferences;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener, LoaderCallbacks<Cursor> {


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
//    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mUserNameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Context context;
    public static Activity loginActivity;
    public String sender = "my_account";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        context = this;
        // Set up the login form.
//        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mUserNameView = (AutoCompleteTextView) findViewById(R.id.user_name);
        //populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button btnLogin = (Button) findViewById(R.id.btn_login);
        Button btnRegister = (Button) findViewById(R.id.btn_register);
        Button btnForgot = (Button) findViewById(R.id.btn_forgot);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnForgot.setOnClickListener(this);
        loginActivity = this;


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    @Override
    public void onStart() {
        super.onStart();
//        mEmailView.setText(SettingsPreferences.getUserName(context));
        mUserNameView.setText(SettingsPreferences.getUserName(context));
        mPasswordView.setText(SettingsPreferences.getPassword(context));
        HomeActivity.initCookie();
        try {
            Bundle extras = getIntent().getExtras();
            sender = extras.getString("sender");
//            String email = extras.getString("email");
 //           String act = extras.getString("action");
//            if(email.length()>4) {
//              //  mEmailView.setText(email);
//                mUserNameView.setText(email);
//                mPasswordView.requestFocus();
//                AppUtils.showAlertDialog(context,"Password Change", "Your password sucessfully changed. Please try login with the new password");
//            }
//            if(act.equalsIgnoreCase("relogin")){
//                mEmailView.setText(SettingsPreferences.getUserName(context));
//                mUserNameView.setText(SettingsPreferences.getUserName(context));
//                mPasswordView.setText(SettingsPreferences.getPassword(context));
//            }
        } catch (Exception e){
        // System.out.println(e);
        // vname="";
        }
    }


/*    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }
*/
    /**
     * Callback received when a permissions request has been completed.
     */
/*    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

*/
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_login:
                attemptLogin();
                break;
            case R.id.btn_register:
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
                //	finish();
                break;
            case R.id.btn_forgot:
                Intent intent2 = new Intent(context, ForgotActivity.class);
//                String em = mEmailView.getText().toString();
                String em = mUserNameView.getText().toString();
                intent2.putExtra("email",em);
                startActivity(intent2);
                break;
        }
    }


    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
//        mEmailView.setError(null);
        mUserNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
//        String email = mEmailView.getText().toString();
        String user_name = mUserNameView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !AppUtils.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            mPasswordView.requestFocus();
         //   focusView = mPasswordView;
            return;
//            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(user_name)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            mUserNameView.requestFocus();
            return;
         //   focusView = mEmailView;
         //   cancel = true;
        } else if (!AppUtils.isEmailValid(user_name)) {
            if(!TextUtils.isDigitsOnly(user_name)) {
                mUserNameView.setError("Enter valid Email or Phone number");
                mUserNameView.requestFocus();
                return;
            }
        //    focusView = mEmailView;
        //    cancel = true;
        }

//        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
//            focusView.requestFocus();
//        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(user_name, password);
            mAuthTask.execute((Void) null);
//        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mUserNameView.setAdapter(adapter);
    }




    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String uname;
        private final String pwd;

        UserLoginTask(String email, String password) {
            uname = email;
            pwd = password;
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try{
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.AuthenticateFM(context,uname,pwd);
                //GetText();
            }
            catch(Exception ex){
                ex.printStackTrace();
                //finish();
            }
            return null;

        }

        @Override
        protected void onPostExecute(final String result) {
            mAuthTask = null;
            showProgress(false);

//            System.out.println("Login REsult: "+result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
//					System.out.println("SID:"+jObj.getString("sid"));
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            SettingsPreferences.setUserName(context, uname);
                            SettingsPreferences.setPassword(context, pwd);
                            SettingsPreferences.setFullName(context,jObj.getString("uname"));

                         //   Time m = new Time();
                         //   m.setToNow();
                         //   long m1 = m.toMillis(true);
                         //   SettingsPreferences.setListMyTime(context, m1);
//                            SettingsPreferences.setSessionID(context, jObj.getString("sid"));
                        //    SettingsPreferences.setAccess(context, jObj.getString("access"));
                        //    SettingsPreferences.setAd(context,jObj.getString("ad"));
/*                            if(jObj.getString("reg").equalsIgnoreCase("true")){
                                SettingsPreferences.setIsTracker(context,true);
                            } else {
                                SettingsPreferences.setIsTracker(context,false);
                            }
*/
//                            AppUtils.saveMyVessel(jObj, context);
                            Intent intent;
                            if(sender.equalsIgnoreCase("my_account")) {
                                intent = new Intent(context, MyActivity.class);
                            } else {
                                intent = new Intent(context, PublicHireActivity.class);
                            }
                            if(jObj.getString("verify").equalsIgnoreCase("1")){
                                SettingsPreferences.setContactVerified(context,true);
                            } else {
                                SettingsPreferences.setContactVerified(context,false);
                            }
                            if(jObj.getString("serial").length()>6){
                                SettingsPreferences.setIMEI(context,jObj.getString("serial"));
                            }
                            if(jObj.getString("me").length()>3){    // my tracker device id, if you track by phone
                                SettingsPreferences.setMyTracker(context,jObj.getString("me"));
                            }

                            startActivity(intent);
                            finish();
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
            mAuthTask = null;
            showProgress(false);
        }
    }
}

