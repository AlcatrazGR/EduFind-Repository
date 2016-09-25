package codebrains.edufind.Models;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONObject;

import codebrains.edufind.Utils.MessageCenter;

/**
 * Created by Vasilhs on 9/25/2016.
 */
public class CreateAccount {

    //Constructor
    public CreateAccount() {

    }

    public boolean CheckGeoLocationInfoJSON(Activity mActivity, JSONObject geoInfo) {

        boolean check = true;
        MessageCenter msgCent = new MessageCenter(mActivity);
        if(geoInfo.length() == 0) {
            String title = "Error Occurred";
            String message = "The geolocation information are not initialized! Please press the button" +
                    " with the gps icon on the form to initialize geolocation info.";
            msgCent.DisplayErrorDialog(mActivity, title, message);
            check = false;
        }

        return check;
    }





}
