package com.asif.followme;

import android.app.Dialog;
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
import android.widget.LinearLayout;
import android.widget.Toast;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.AppUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class ForgotActivity extends AppCompatActivity implements OnClickListener {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private CodeVarifyTask mVarifyTask = null;

    private Context context;
    EditText yourEmail,yourCode,yourPass1,yourPass2;
    Button btnGetCode,btnNext,btnChangePass;
    LinearLayout layout1,layout2;
    private Dialog mDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_form);
        context = this;
        // Set up the login form.
        //mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        //populateAutoComplete();


        btnGetCode = (Button) findViewById(R.id.btn_get_code);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnChangePass = (Button) findViewById(R.id.btn_change_pass);

        yourEmail =(EditText) findViewById (R.id.your_email);
        yourCode = (EditText) findViewById(R.id.your_code);

        yourPass1 =(EditText) findViewById (R.id.your_pass1);
        yourPass2 = (EditText) findViewById(R.id.your_pass2);

        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout1.setVisibility(View.VISIBLE);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        layout2.setVisibility(View.GONE);


        btnGetCode.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnChangePass.setOnClickListener(this);

        //mLoginFormView = findViewById(R.id.login_form);
        //mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            Bundle extras = getIntent().getExtras();
            String email = extras.getString("email");
            yourEmail.setText(email);
        } catch (Exception e){
            // System.out.println(e);
            // vname="";
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_next:
                attemptNext();
                break;
            case R.id.btn_get_code:
                attemptCode();
                break;
            case R.id.btn_change_pass:
                attemptChangePass();
                break;
        }
    }

    private void attemptNext() {
        if (mVarifyTask != null) {
            return;
        }
        yourEmail.setError(null);
        String email = yourEmail.getText().toString();
        String code = yourCode.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (email.length()==0) {
            yourEmail.setError("Please type Your Registered Email with FollowMe");
            focusView = yourEmail;
            cancel = true;
        } else if (!AppUtils.isEmailValid(email)) {
            yourEmail.setError(getString(R.string.error_invalid_email));
            focusView = yourEmail;
            cancel = true;
        }
        if(code.length()==0){
            yourCode.setError("Please type the code received to your email");
            focusView = yourCode;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
             mVarifyTask = new CodeVarifyTask(email,code,"varify","");
             mVarifyTask.execute((Void) null);
        }
    }

    private void attemptCode() {
        if (mVarifyTask != null) {
            return;
        }
        yourEmail.setError(null);
        String email = yourEmail.getText().toString();
        String code = yourCode.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (email.length()==0) {
            yourEmail.setError("Please type Your Registered Email with FollowMe");
            focusView = yourEmail;
            cancel = true;
        } else if (!AppUtils.isEmailValid(email)) {
            yourEmail.setError(getString(R.string.error_invalid_email));
            focusView = yourEmail;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
        } else {
            mVarifyTask = new CodeVarifyTask(email,code,"new_code", "");
            mVarifyTask.execute((Void) null);

        }
    }

    private void attemptChangePass() {
        if (mVarifyTask != null) {
            return;
        }
        yourPass1.setError(null);
        yourPass2.setError(null);
        String pass1 = yourPass1.getText().toString();
        String pass2 = yourPass2.getText().toString();
        String email = yourEmail.getText().toString();
        String code = yourCode.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (pass1.length()==0) {
            yourPass1.setError("Please type Your New Password");
            focusView = yourPass1;
            cancel = true;
        } else if (!AppUtils.isPasswordValid(pass1)) {
            yourPass1.setError("Invalid Password. Please enter atleast 5 characters");
            focusView = yourPass1;
            cancel = true;
        }
        if (pass2.length()==0) {
            yourPass2.setError("Please type Your New Password again");
            focusView = yourPass2;
            cancel = true;
        } else if (!AppUtils.isPasswordValid(pass2)) {
            yourPass2.setError("Invalid Password. Please enter atleast 5 characters");
            focusView = yourPass2;
            cancel = true;
        } else if(!pass1.equalsIgnoreCase(pass2)){
            yourPass2.setError("The two passwords are not same");
            focusView = yourPass2;
            cancel = true;

        }


        if (cancel) {
            focusView.requestFocus();
        } else {
            mVarifyTask = new CodeVarifyTask(email,code,"change_pass", pass1);
            mVarifyTask.execute((Void) null);

        }
    }




    public class CodeVarifyTask extends AsyncTask<Void, Void, String> {

        private final String code;
        private final String email;
        private  final String action;
        private final String pass;

        CodeVarifyTask(String email, String code,String action, String pass) {
            this.code = code;
            this.email = email;
            this.action = action;
            this.pass = pass;
        }
        protected void onPreExecute() {
            mDialog= ProgressDialog.show(context, null, "Please wait...");
            mDialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO: attempt authentication against a network service.

            try{
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.forgotPassword(context,email, code, action, pass);
            }
            catch(Exception ex)
            {
                //finish();
            }
            return null;

        }

        @Override
        protected void onPostExecute(final String result) {
            mVarifyTask = null;
           // showProgress(false);
            if(mDialog.isShowing()){
                mDialog.dismiss();
            }
            System.out.println("Login REsult: "+result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
//					System.out.println("SID:"+jObj.getString("sid"));
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                            if(action.equalsIgnoreCase("varify")) {  //varify if the given code is correct
                                if (statuss.equalsIgnoreCase("ok")) {
                                    System.out.println("Varification OK");
                                    layout1.setVisibility(View.GONE);
                                    layout2.setVisibility(View.VISIBLE);

                                } else {
                                    System.out.println("Varification Failed");
                                    String errmsg = jObj.getString("error");
                                    AppUtils.showAlertDialog(context, "Error", errmsg);

                                }
                            }
                            if(action.equalsIgnoreCase("new_code")){
                                String errmsg = jObj.getString("error");
                                if (statuss.equalsIgnoreCase("ok")) {
                                    AppUtils.showAlertDialog(context, "Security Code", errmsg);
                                } else {
                                    yourEmail.setError(errmsg);
                                }
                            }
                            if(action.equalsIgnoreCase("change_pass")){
                                String errmsg = jObj.getString("error");
                                if (statuss.equalsIgnoreCase("ok")) {
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    intent.putExtra("email",email);
                                    intent.putExtra("sender","my_account");
                                    startActivity(intent);
                                    finish();
                                    //AppUtils.showAlertDialog(context, "Password Changed", errmsg);
                                } else {
                                    yourEmail.setError(errmsg);
                                }

                            }

                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Unable to reach server. Please check your internet connection", Toast.LENGTH_LONG).show();
                //AppUtils.showAlertDialog(context, "Timeout", "Unable to reach Server. Please try again!");
            }
        }

        @Override
        protected void onCancelled() {
            mVarifyTask = null;
           // showProgress(false);
        }
    }
}

