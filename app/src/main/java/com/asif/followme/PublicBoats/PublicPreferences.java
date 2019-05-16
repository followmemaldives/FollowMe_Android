package com.asif.followme.PublicBoats;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;

import com.asif.followme.R;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.SettingsPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class PublicPreferences extends AppCompatActivity {
    private static final String TAG = PublicPreferences.class.getSimpleName();
    private static Context context;
    private static AsyncTask<Void, Void, String> PreferenceAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;

        // load settings fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }

    public static class MainPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.public_pref);
            bindPreferenceSummaryToValue(findPreference(getString(R.string.title_key_country)));
            //bindPreferenceSummaryToValue(findPreference(getString(R.string.title_key_map)));
            Preference countryPref = findPreference(getString(R.string.title_key_country));
            //Preference mapPref = findPreference(getString(R.string.title_key_map));
            countryPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    System.out.println("Changed.......................................................................");
                    String stringValue = newValue.toString();
                    if (preference instanceof ListPreference) {
                        // For list preferences, look up the correct display value in
                        // the preference's 'entries' list.
                        ListPreference listPreference = (ListPreference) preference;
                        int index = listPreference.findIndexOfValue(stringValue);

                        // Set the summary to reflect the new value.
                        preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
                        SettingsPreferences.setCountry(context, listPreference.getEntryValues()[index].toString());
                        PreferenceTask(listPreference.getEntryValues()[index].toString());
                        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                        SettingsPreferences.setSelectedAtoll(context,0);
                    }
                    return true;
                }

            });
        /*    mapPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    System.out.println("Changed.......................................................................");
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
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra("action", "public_pref");
        setResult(RESULT_OK,intent );
        super.onBackPressed();

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

    public static void PreferenceTask(final String country) {
        PreferenceAsyncTask = new AsyncTask<Void, Void, String>()    {
            @Override
            protected void onPreExecute() {
                //Dialog= ProgressDialog.show(context, "Please Wait", "Signing Up...");
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(Void... arg0) {
                try{
                    ContentParser parser = new ContentParser(context);
    				return parser.SavePublicPreferences(context,country);
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
                System.out.println("Result:"+result);
                if (!TextUtils.isEmpty(result)) {
                    try {
                        JSONObject jObj = new JSONObject(result);
                        if (jObj.has("status")) {
                            if (jObj.getString("status").equalsIgnoreCase("ok")) {
                                //	SettingsPreferences.setUserName(context, yourEmail);
                                //	SettingsPreferences.setPassword(context, yourPass);
                                String lat = jObj.getString("lat");
                                String lon = jObj.getString("lon");
                                String zoom = jObj.getString("zoom");
                                SettingsPreferences.setCountryLat(context, jObj.getString("lat"));
                                SettingsPreferences.setCountryLon(context, jObj.getString("lon"));
                                SettingsPreferences.setCountryZoom(context,jObj.getInt("zoom"));
                            } else {
                                //Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_LONG).show();
                            }
                        } else {
                           // Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        //Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                    }

                } else {
                   // Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

                }
            }
        };
        PreferenceAsyncTask.execute(null, null, null);

    }


}
