package com.asif.followme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asif.followme.MyAccount.MyActivity;
import com.asif.followme.imageloader.ImageLoader;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.AppConstants;
import com.asif.followme.util.SettingsPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnMy,btnPublic;
    private AsyncTask<Void, Void, String> ProfileAsyncTask;
    private getDeviceInfo deviceInfoTask = null;

    boolean isInternetPresent = false;
//    ConnectionDetector cd;
    public Activity activity;
    String statuss, errmsg;
    ProgressDialog mDialog;
    String uname, pwd;

    String statuspass = "ok";
    String statusfailed = "failed";
    String text = "";
    JSONObject jObj = null;
    JSONArray alldata = null;
    //	ImageLoader imageLoader = new ImageLoader(this);
    public String token,fb_id;
    public String loginType = "";
    Toast toast;
    private Context context;
    public static Activity loginActivity;
//    ConnectionDetector connectionCheck;
    TelephonyManager Tel;
    public static String imei,country,operator;
    String versionCode;
    public Intent ServiceIntent;
    private ImageLoader imageLoader;
    private ImageView photoView;
    String filePath;
    String Account;
    private AsyncTask<Void, Void, String> VesselInfoTask;
    private TextView vesselName,vesselContact,vesselAddress,vesselNotes,alarmInput1,alarmInput2,vesselExpiry,vesselPackage,vesselFav,deviceSIM;
    private RelativeLayout adminLayout;
    private LinearLayout alarmLayout,alarmLayout1,alarmLayout2,pkgLayout;
    private Button btnPhotoEdit, btnPhotoRemove;
    private Button btnPhotoSave;
    public static Boolean RemoveImage;
    public String DefaultIcon;
    public Bitmap profileBitmap;
    public static String selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);
        context=this;

        //Account = SettingsPreferences.getAccount(context);
        photoView = (ImageView) findViewById(R.id.photo);
        btnPhotoEdit = (Button) findViewById(R.id.btn_photo_edit);
        btnPhotoRemove = (Button) findViewById(R.id.btn_photo_remove);
        btnPhotoSave = (Button) findViewById(R.id.btn_photo_save);
        imageLoader		=	new ImageLoader(context);
        imageLoader.DisplayImage(selectedImage, photoView, (ProgressBar) findViewById(R.id.progres_bar));
        //	btnClose = (ImageButton) findViewById(R.id.btn_my);
        //	btnPublic = (Button) findViewById(R.id.btn_public);
        vesselName = (TextView) findViewById(R.id.vessel_name_input);
        vesselContact = (TextView) findViewById(R.id.vessel_contact_input);
        vesselExpiry = (TextView) findViewById(R.id.vessel_expiry);
        vesselFav = (TextView) findViewById(R.id.fav_qty);
        vesselPackage = (TextView) findViewById(R.id.vessel_pkg);
        vesselAddress = (TextView) findViewById(R.id.vessel_address_input);
        deviceSIM = (TextView) findViewById(R.id.device_sim);
        vesselNotes = (TextView) findViewById(R.id.vessel_notes_input);
        alarmInput1 = (TextView) findViewById(R.id.alarm1_input);
        alarmInput2 = (TextView) findViewById(R.id.alarm2_input);
        adminLayout = (RelativeLayout) findViewById(R.id.photo_admin_layout);
        alarmLayout = (LinearLayout) findViewById(R.id.alarm_layout_box);
        alarmLayout1 = (LinearLayout) findViewById(R.id.alarm_layout_1);
        alarmLayout2 = (LinearLayout) findViewById(R.id.alarm_layout_2);
        pkgLayout = (LinearLayout) findViewById(R.id.pkg_layout);
        btnPhotoEdit.setOnClickListener(this);
        btnPhotoRemove.setOnClickListener(this);
        btnPhotoSave.setOnClickListener(this);
        alarmLayout1.setVisibility(View.GONE);
        alarmLayout2.setVisibility(View.GONE);
/*        if(Account.equalsIgnoreCase("my")){
            adminLayout.setVisibility(View.VISIBLE);
            btnPhotoSave.setVisibility(View.VISIBLE);
            alarmLayout.setVisibility(View.VISIBLE);
            pkgLayout.setVisibility(View.VISIBLE);
        } else {	// Hide if Public
            adminLayout.setVisibility(View.GONE);
            btnPhotoSave.setVisibility(View.GONE);
            alarmLayout.setVisibility(View.GONE);
            pkgLayout.setVisibility(View.GONE);
        }
       */
//        connectionCheck = new ConnectionDetector(getApplicationContext());
//        isInternetPresent = connectionCheck.isConnectingToInternet();
//        if (isInternetPresent){
        deviceInfoTask = new getDeviceInfo();
        deviceInfoTask.execute();
//      getDeviceInfo();
//        }
        RemoveImage=false;


    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        try{
            Bundle extras = getIntent().getExtras();
            if(extras.getString("account").equalsIgnoreCase("my")){
                Account = "my";
                adminLayout.setVisibility(View.VISIBLE);
                btnPhotoSave.setVisibility(View.VISIBLE);
                alarmLayout.setVisibility(View.VISIBLE);
                pkgLayout.setVisibility(View.VISIBLE);
            } else {
                Account = "public";
                adminLayout.setVisibility(View.GONE);
                btnPhotoSave.setVisibility(View.GONE);
                alarmLayout.setVisibility(View.GONE);
                pkgLayout.setVisibility(View.GONE);
                deviceSIM.setVisibility(View.GONE);
            }
        } catch (Exception e){
            Account = "public";
            adminLayout.setVisibility(View.GONE);
            btnPhotoSave.setVisibility(View.GONE);
            alarmLayout.setVisibility(View.GONE);
            pkgLayout.setVisibility(View.GONE);
            deviceSIM.setVisibility(View.GONE);
        }
    }


    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.btn_photo_edit:
                //System.out.println("Lets Edit Photo Here");
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent,1);
                break;
            case R.id.btn_photo_remove:
                //System.out.println("Lets Remove Photo and show default icon Here");
                imageLoader.DisplayImage(AppConstants.IMAGE_SMALL_URL+DefaultIcon, photoView, (ProgressBar) findViewById(R.id.progres_bar));
                RemoveImage =true;
                break;
//			userNameEditText.setText("demo");
//			passWordEditText.setText("welcome");
//			doLogin();
//			break;
            case R.id.btn_photo_save:
                SaveProfile();

                //	Intent intent = new Intent(context, PublicList.class);
                //	startActivity(intent);
                //	finish();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                //    filePath = selectedImage.getPath();
                filePath = getPath(selectedImage);
                String file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);
                //   int IMAGE_MAX_SIZE = 500;
                File f = new File(filePath);

                // try {
                //System.out.println("A...................................................");

                if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
                    //	System.out.println("Lets upload, File ok");
                    //imageLoader.DisplayImage(filePath, photoView, (ProgressBar) findViewById(R.id.progres_bar_photo));
	                	/*
	            	    BitmapFactory.Options o = new BitmapFactory.Options();
	            	    o.inJustDecodeBounds = true;
	        		    int scale = 1;
	        		    if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
	        		        scale = (int)Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
	        		           (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
	        		    }
	        		    BitmapFactory.Options o2 = new BitmapFactory.Options();
	        		    o2.inSampleSize = scale;
	        		    FileInputStream fis;
						try {
							fis = new FileInputStream(filePath);
		        		    profileBitmap = BitmapFactory.decodeStream(fis, null, o2);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            	    */

	                /*
	            		profileBitmap = BitmapFactory.decodeFile(filePath);
	            		System.out.println("B File:"+filePath+" ...................................................");
	            	       int width = profileBitmap.getWidth();
	            	       int height = profileBitmap.getHeight();
	            	       System.out.println("Original Width:"+width);
	            	       System.out.println("Original Height:"+height);
	            	       double ratio = (double) width/height;
	            	       System.out.println("Ratio:"+ratio);
	            	       if(ratio > 1){	// width is larger than height
	            	    	   System.out.println("Width is greater");
	            	    	   width=500;
	            	    	   height=(int) (width/ratio);
	            	       } else {
	            	    	   System.out.println("Height is greater");
	            	    	   height=500;
	            	    	   width=(int) (height/ratio);
	            	       }

	            	       ByteArrayOutputStream out = new ByteArrayOutputStream();
	            	       profileBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
	            	       profileBitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
	            	       System.out.println("New Width:"+width);
	            	   //    System.out.println("New Height:"+height);
           	            profileBitmap = Bitmap.createScaledBitmap(profileBitmap, width, height, true);
*/

                    profileBitmap = decodeFile(800,f);

                    //System.out.println("G...................................................");

                    photoView.setImageBitmap(profileBitmap);
                    RemoveImage=false;
                    //FINE
                } else {
                    //NOT IN REQUIRED FORMAT
                }

            }
    }
    private Bitmap decodeFile(int IMAGE_MAX_SIZE, File f){
        Bitmap b = null;

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis;
        try {
            fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int)Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.3)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return b;
    }
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
//	    int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
//	    imagePath = cursor.getString(column_index);

        return cursor.getString(column_index);
    }

    public class getDeviceInfo extends AsyncTask<Void, Void, String> {

 //       getDeviceInfo(String id) {
 //           hire_id=Integer.parseInt(id);
  //      }

            @Override
            protected void onPreExecute() {
                System.out.println("getDeviceInfo........................");
                //if(alertSoundCheckBox.isChecked() || alertVibrateCheckBox.isChecked()){
                mDialog=ProgressDialog.show(context, null, "Loading, Please wait...");
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(Void... arg0) {
                // TODO Auto-generated method stub
                try{
                    ContentParser parser = new ContentParser(getBaseContext());
                    return parser.GetVesselInfo(context,SettingsPreferences.getSelectedItemID(context));
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
                if(mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                if (!TextUtils.isEmpty(result)) {
                    System.out.println(result);
                    try {
                        JSONObject jObj = new JSONObject(result);
                        //	JSONArray jObj2 = new JSONArray(jObj.getString("alarms"));
                        if (jObj.has("status")) {
                            statuss = jObj.getString("status");
                            if (statuss.equalsIgnoreCase(statuspass)) {
                                vesselName.setText(jObj.getString("name"));
                                vesselContact.setText(jObj.getString("contact"));
                                vesselExpiry.setText(jObj.getString("expiry"));
                                vesselPackage.setText(jObj.getString("pkg"));
                                vesselAddress.setText(jObj.getString("company"));
                                vesselNotes.setText(jObj.getString("notes"));
                                vesselFav.setText(jObj.getString("fav")+" people");
                                deviceSIM.setText(jObj.getString("sim"));
                           //     imageLoader		=	new ImageLoader(context);
                                int input_qty = Integer.parseInt(jObj.getString("input_qty"));
                                if(input_qty > 0){
                                    if(Account.equalsIgnoreCase("my")){
                                        alarmLayout.setVisibility(View.VISIBLE);
                                    }
                                    if(jObj.getString("alarms")!="null"){
                                        JSONArray jData = jObj.getJSONArray("alarms");
                                        TextView[] textViews = new TextView[] { alarmInput1, alarmInput2 };
                                        LinearLayout[] linearLayouts = new LinearLayout[] {alarmLayout1,alarmLayout2};
                                        alarmInput1.setText("");
                                        alarmInput2.setText("");
                                        for (int j = 0; j < input_qty; j++)
//										for (int j = 0; j < jData.length(); j++)
                                        {
                                            JSONObject jDataObj = jData.getJSONObject(j);
                                            textViews[j].setText(jDataObj.getString("message"));
                                            linearLayouts[j].setVisibility(View.VISIBLE);

                                        }
                                    }
                                } else {
                                    alarmLayout.setVisibility(View.GONE);
                                }

                                DefaultIcon = jObj.getString("img");
                                //File f = new File(AppConstants.IMAGE_LARGE_URL+jObj.getString("photo"));
                                //profileBitmap = decodeFile(800,f);
                                //photoView.setImageBitmap(profileBitmap);

//                                imageLoader.DisplayImage(jObj.getString("photo"), photoView, (ProgressBar) findViewById(R.id.progres_bar));
                                imageLoader.DisplayProfileImage(jObj.getString("photo"), photoView, (ProgressBar) findViewById(R.id.progres_bar));
                            } else {
                                errmsg = jObj.getString("error");
                                //AppUtils.showAlertDialog(context, "Error", errmsg);
                                Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }


    }

    private void SaveProfile() {
        ProfileAsyncTask = new AsyncTask<Void, Void, String>()    {
            @Override
            protected void onPreExecute() {
                //	Dialog.show();
                mDialog=ProgressDialog.show(context, null, "Uploading & Saving Profile...");
                //loading.setText("Attempting to Authenticate...");
                //Dialog=ProgressDialog.show(SplashScreen.this, "Please wait", "Loading...");
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(Void... arg0) {
                // TODO Auto-generated method stub
                // TODO Auto-generated method stub
                try{
                    ContentParser parser = new ContentParser(getBaseContext());
                    return parser.ProfileSave(context,profileBitmap);
                    //GetText();
                }
                catch(Exception ex)
                {
                    //finish();
                    //System.out.println("Error: Please uninstall old version and try again."+ex);
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if(mDialog.isShowing())
                {
                    mDialog.dismiss();
                }
                System.out.println("Result:"+result);
                if (!TextUtils.isEmpty(result)) {
                    try {
                        JSONObject jObj = new JSONObject(result);
                        if (jObj.has("status")) {
                            if (jObj.getString("status").equalsIgnoreCase("ok")) {
 //                               SettingsPreferences.setListMyTime(context, 0);  	//force refresh
                                Intent intent = new Intent(context, MyActivity.class);
                                intent.putExtra("reload",true);
                                startActivity(intent);
                                finish();
                            } else {
                            }
                        } else {
                            //	Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                            //	System.out.println("Errror.............");
                            //	System.out.println("Result"+result);
                        }
                    } catch (JSONException e) {
                        //   	loading.setText("Unable to reach Server...");
                        //		Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else {

                    finish();
                    //	AppUtils.showAlertDialog(context, "Timeout", "Unable to reach Server. Please try again!");

                    //	Toast.makeText(getApplicationContext(), "Timed Out?", Toast.LENGTH_LONG).show();
                    //	System.out.println("Timed Out?");
                }
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            ProfileAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
        else
            ProfileAsyncTask.execute((Void[])null);
        //FMAsyncTask.execute(null, null, null);
    }
}
