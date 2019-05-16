package com.asif.followme;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.*;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.LinearLayout;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.SettingsPreferences;
import org.json.JSONException;
import org.json.JSONObject;

public class MyPreferences extends AppCompatActivity {
    private static final String TAG = MyPreferences.class.getSimpleName();
    private static Context context;
    private static AsyncTask<Void, Void, String> PreferenceAsyncTask;
    public static LinearLayout moveLayout;
 //   public static CheckBox moveSound;
 //   private static Preference mPrefLayout;
    static PreferenceScreen screen;
    private static PreferenceTask mPrefTask = null;
    public static Boolean needSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      setContentView(R.layout.movement_checkbox);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("My Preferences");
        context = this;
        needSave = false;

        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
//        moveLayout = (LinearLayout) findViewById(R.id.alertMovementLayout);
//        moveSound = (CheckBox) findViewById(R.id.moveSoundCheckBox);
}

    @Override
    public void onBackPressed() {

        super.onBackPressed();
 //       if(needSave) {
            System.out.println("Lets Save.....................................");
            String country = SettingsPreferences.getCountry(context);
            Boolean move = SettingsPreferences.getAlertMove(context);
            Boolean minor = SettingsPreferences.getAlertMinor(context);
            Boolean major = SettingsPreferences.getAlertMajor(context);
            Boolean hire = SettingsPreferences.getAlertHire(context);
            mPrefTask = new PreferenceTask(context, move, minor, major, hire);
            mPrefTask.execute((Void) null);
   //     }

    }
    public static class MainPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.my_pref);

            Preference movePref = findPreference("move_alert");
            Preference minorPref = findPreference("minor_alert");
            Preference majorPref = findPreference("major_alert");
            Preference hirePref = findPreference("hire_alert");
            Preference notificationScreen = findPreference("notification_screen");

            //Remove below after Boat hire officially launch
            /*
            PreferenceCategory mHireCat = (PreferenceCategory) findPreference("category_hire");
            if(SettingsPreferences.getBoatHireService(context)){
                mHireCat.setEnabled(true);
            } else {
                mHireCat.setEnabled(false);
            }
            */
                // mPrefLayout = findPreference("movePrefLayout");
            bindPreferenceSummaryToValue(findPreference("key_country"));
            //bindPreferenceSummaryToValue(findPreference(getString(R.string.title_key_map)));
            bindPreferenceSummaryToValue(movePref);
            bindPreferenceSummaryToValue(minorPref);
            bindPreferenceSummaryToValue(majorPref);
            bindPreferenceSummaryToValue(hirePref);
            Preference countryPref = findPreference(getString(R.string.title_key_country));
            //Preference mapPref = findPreference(getString(R.string.title_key_map));
            Preference moveSoundPref = findPreference("move_sound");
            Preference minorSoundPref = findPreference("minor_sound");
            Preference majorSoundPref = findPreference("major_sound");
            Preference hireSoundPref = findPreference("hire_sound");
         /*    Preference SaveButton = findPreference("save_pref");
            SaveButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    System.out.println("Lets Save.....................................");
                    String country = SettingsPreferences.getCountry(context);
                    Boolean move = SettingsPreferences.getAlertMove(context);
                    Boolean major = SettingsPreferences.getAlertMajor(context);
                    Boolean minor = SettingsPreferences.getAlertMinor(context);
                    mPrefTask = new PreferenceTask(context,move,major,minor);
                    mPrefTask.execute((Void) null);

                    //code for what you want it to do
                    return true;
                }
            });
*/
         //   bindPreferenceSummaryToValue(mPrefLayout);
         //   screen = getPreferenceScreen();
        //    View v = mPrefLayout.getView(null, null);
        //    moveSound = (CheckBox) v.findViewById(R.id.moveSoundCheckBox);
         //   CheckBox moveVibrate = (CheckBox) getView().findViewById(R.id.moveVibrateCheckBox);
        /*    moveSound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Check box Status ..............:");

                }


            });
*/
            moveSoundPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object o) {
                        System.out.println("Changed.......................................................................");
                        System.out.println("Check Status:"+o.toString());
                        Boolean st = o.toString().equalsIgnoreCase("true") ? true : false;
                        SettingsPreferences.setSoundMove(context, st);
                        return true;
                    }
            });
            minorSoundPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    System.out.println("Changed.......................................................................");
                    System.out.println("Check Status:"+o.toString());
                    Boolean st = o.toString().equalsIgnoreCase("true") ? true : false;
//                    Boolean st = o.toString() == "true" ? true : false;
                    SettingsPreferences.setSoundMinor(context, st);
                    return true;
                }
            });
            majorSoundPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    System.out.println("Changed.......................................................................");
                    System.out.println("Check Status:"+o.toString());
                    Boolean st = o.toString().equalsIgnoreCase("true") ? true : false;
//                    Boolean st = o.toString() == "true" ? true : false;
                    SettingsPreferences.setSoundMajor(context, st);
                    return true;
                }
            });
            hireSoundPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    System.out.println("HireSound Changed.......................................................................");
                    System.out.println("Check Status:"+o.toString());
                    Boolean st = o.toString().equalsIgnoreCase("true") ? true : false;
//                    Boolean st = o.toString() == "true" ? true : false;
                    SettingsPreferences.setSoundHire(context, st);
                    Boolean alertSound = SettingsPreferences.getSoundHire(context);
                    System.out.println("Set SOund:"+st+", Got Sound:"+alertSound);

                    return true;
                }
            });
            countryPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    System.out.println("Changed.......................................................................");
                    needSave = true;
                    String stringValue = newValue.toString();
                    if (preference instanceof ListPreference) {
                        // For list preferences, look up the correct display value in
                        // the preference's 'entries' list.
                        ListPreference listPreference = (ListPreference) preference;
                        int index = listPreference.findIndexOfValue(stringValue);

                        // Set the summary to reflect the new value.
                        preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
                        SettingsPreferences.setCountry(context, listPreference.getEntryValues()[index].toString());
                    }
                    return true;
                }

            });
        /*    mapPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    System.out.println("Map Change Listener.......................................................................");
                    String stringValue = newValue.toString();
                    if (preference instanceof ListPreference) {
                        // For list preferences, look up the correct display value in
                        // the preference's 'entries' list.
                        ListPreference listPreference = (ListPreference) preference;
                        int index = listPreference.findIndexOfValue(stringValue);

                        // Set the summary to reflect the new value.
                        preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
                        SettingsPreferences.setMap(context, Integer.parseInt(listPreference.getEntryValues()[index].toString()));
                    }
                    return true;
                }

            });
            */
            movePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String stringValue = newValue.toString();
                    needSave = true;
                    if(newValue.toString().equalsIgnoreCase("true")){
                        SettingsPreferences.setAlertMove(context,true);
                    } else {
                        SettingsPreferences.setAlertMove(context, false);
                    }

                    return true;
                }

            });
            minorPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String stringValue = newValue.toString();
                    needSave = true;
                    if(newValue.toString().equalsIgnoreCase("true")){
                        SettingsPreferences.setAlertMinor(context,true);
                    } else {
                        SettingsPreferences.setAlertMinor(context, false);
                    }

                    return true;
                }

            });
            majorPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String stringValue = newValue.toString();
                    needSave = true;
                    if(newValue.toString().equalsIgnoreCase("true")){
                        SettingsPreferences.setAlertMajor(context,true);
                    } else {
                        SettingsPreferences.setAlertMajor(context, false);
                    }

                    return true;
                }

            });
            hirePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String stringValue = newValue.toString();
                    needSave = true;
                    if(newValue.toString().equalsIgnoreCase("true")){
                        SettingsPreferences.setAlertHire(context,true);
                    } else {
                        SettingsPreferences.setAlertHire(context, false);
                    }

                    return true;
                }

            });
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        //preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
        //        PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(),
        //                ""));

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list.
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(listPreference.getValue());
            System.out.println("The INDEX ...............................: " + index);
            preference.setSummary(
                    index >= 0
                            ? listPreference.getEntries()[index]
                            : null);

        }
    /*    if(preference instanceof SwitchPreference){
            SwitchPreference checkPreference = (SwitchPreference) preference;
            Boolean d = checkPreference.isChecked();
            System.out.println("Boolean...................:"+d.toString());
            System.out.println("Layout:"+checkPreference.getKey());
            if(checkPreference.getKey().equalsIgnoreCase("switch_move_alert")){
                System.out.println("This is movePrefLayout..............................");
                if(checkPreference.isChecked()){
                    System.out.println("Lets Enable..........................................");
                    mPrefLayout.setEnabled(true);
                } else {
                    System.out.println("Lets Disabled..........................................");
                    mPrefLayout.setEnabled(false);
                }

            }
        }
        */
    }



    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
//    private static test = new Preference.OnPreferenceChangeListener() {
//        @Override
//        public boolean onPreferenceChange(Preference preference, Object newValue) {
//            String stringValue = newValue.toString();
/*            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
                System.out.println("Preference Change Listener********************");
                if(index >= 0) {
                    System.out.println("Country********************"+listPreference.getEntries()[index]);
                    //String[] keys = listPreference.getEntryValues()[index].toString().split(";");

                   // System.out.println("Country Index: "+index+" ********************"+listPreference.getEntryValues()[index]);
                    SettingsPreferences.setCountry(context, listPreference.getEntryValues()[index].toString());
                //    System.out.println("Actual: "+listPreference.getEntryValues()[index]+", Country Code: "+keys[0]+", Cords: "+keys[1]);
                 //   SettingsPreferences.setCountry(context, keys[0]);
                 //   SettingsPreferences.setCountryCoordinates(context, keys[1]);
                }

            }
     /*       else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(R.string.summary_choose_ringtone);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else if (preference instanceof EditTextPreference) {
                if (preference.getKey().equals("key_gallery_name")) {
                    // update the changed gallery name to summary filed
                    preference.setSummary(stringValue);
                }
            } else {
                preference.setSummary(stringValue);
            }
         */
//            return true;
//        }
//    };


    /**
     * Email client intent to send support mail
     * Appends the necessary device information to email body
     * useful when providing support
     */
/*    public static void sendFeedback(Context context) {
        String body = null;
        try {
            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;
        } catch (PackageManager.NameNotFoundException e) {
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact@androidhive.info"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Query from android app");
        intent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)));
    }
   */

    public static class PreferenceTask extends AsyncTask<Void, Void, String> {
        int move;
        int minor;
        int major;
        int hire;
        Context con;
//        Dialog dialog;
        PreferenceTask(Context con, Boolean move,Boolean minor,Boolean major, Boolean hire){
            this.move = move == true ? 1 : 0;
            this.minor = minor == true ? 1 : 0;
            this.major = major == true ? 1 : 0;
            this.hire = hire == true ? 1 : 0;
            this.con = con;
        }
        @Override
        protected void onPreExecute() {
         //   dialog= ProgressDialog.show(con, null, "Saving Preferences, Please wait...");
         //   dialog.show();
            super.onPreExecute();

        }


        @Override
        protected String doInBackground(Void... arg0) {
            // TODO: attempt authentication against a network service.

            try{
                ContentParser parser = new ContentParser(con);
                return parser.SavePreferences(con,move,minor,major,hire);
                //GetText();
            }
            catch(Exception ex)
            {
                //finish();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mPrefTask = null;
            //dialog.hide();
            //dialog= ProgressDialog.show(con, null, "Posting, Please wait...");
           // dialog.show();

            //showProgress(false);
            System.out.println("Result:"+result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        if (jObj.getString("status").equalsIgnoreCase("ok")) {
                            //	SettingsPreferences.setUserName(context, yourEmail);
                            //	SettingsPreferences.setPassword(context, yourPass);
                            SettingsPreferences.setCountryLat(context, jObj.getString("lat"));
                            SettingsPreferences.setCountryLon(context, jObj.getString("lon"));
                            SettingsPreferences.setCountryZoom(context,jObj.getInt("zoom"));
                          //  ((Activity)context).finish();
                           // Toast.makeText(con, "Preference Saved", Toast.LENGTH_LONG).show();
                        } else {
                            //Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    //Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                    // TODO Auto-generated catch block
                }
            } else {
                // Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mPrefTask = null;
            //showProgress(false);
        }
    }


}
