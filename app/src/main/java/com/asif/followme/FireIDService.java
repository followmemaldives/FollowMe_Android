package com.asif.followme;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.asif.followme.manager.ContentParser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 1/1/2018.
 */

public class FireIDService extends FirebaseInstanceIdService {
    Context context;

    @Override
    public void onTokenRefresh() {
        String tkn = FirebaseInstanceId.getInstance().getToken();
        context = this;
        try{
            ContentParser parser = new ContentParser(getBaseContext());
            String result = parser.saveFireID(context,tkn);

            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            //AppUtils.saveMyVessel(jObj, context);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        Log.d("FireIDService","Token ["+tkn+"]");
    }

}
