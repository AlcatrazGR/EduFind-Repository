package codebrains.edufind.Models;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import codebrains.edufind.R;
import codebrains.edufind.Utils.Coordinates;
import codebrains.edufind.Utils.MessageCenter;

/**
 * Created by Vasilhs on 9/25/2016.
 */
public class CreateAccount {

    //Constructor
    public CreateAccount() {

    }


    public JSONObject GetCurrentGeoLocationInfo(Activity mActivity) {

        JSONObject geoJson;
        Coordinates coordinates = new Coordinates(mActivity);
        geoJson = coordinates.GetLocationInfoFromCoordinates();

        try {
            geoJson.put("longitude", coordinates.GetLongitude());
            geoJson.put("latitude", coordinates.GetLatitude());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return geoJson;
    }

    public void DisplayGeolocationInfo(Activity activity, JSONObject geoJson) {

        //Displaying results
        TextView longitudeTxt = (TextView) activity.findViewById(R.id.textView6);
        TextView latitudeTxt = (TextView) activity.findViewById(R.id.textView7);
        TextView cityTxt = (TextView) activity.findViewById(R.id.textView8);
        TextView postalTxt = (TextView) activity.findViewById(R.id.textView9);
        TextView addressTxt = (TextView) activity.findViewById(R.id.textView10);

        try {
            longitudeTxt.setText("Longitude :" + geoJson.get("longitude").toString());
            latitudeTxt.setText("Latitude :" + geoJson.get("latitude").toString());
            cityTxt.setText("City :" + geoJson.get("city").toString());
            postalTxt.setText("Postal Code :" + geoJson.get("postal").toString());
            addressTxt.setText("Address :" + geoJson.get("address").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    public JSONObject CreateNewAccountJSONObject(Activity mActivity, JSONObject geoInfo) {

        JSONObject accountJSON = geoInfo;

        EditText usernameEdt = (EditText) mActivity.findViewById(R.id.editText);
        EditText passwordEdt = (EditText) mActivity.findViewById(R.id.editText2);
        EditText rePassedt = (EditText) mActivity.findViewById(R.id.editText3);
        EditText emailEdt = (EditText) mActivity.findViewById(R.id.editText4);
        EditText numberEdt = (EditText) mActivity.findViewById(R.id.editText5);
        EditText providerEdt = (EditText) mActivity.findViewById(R.id.editText6);

        try {
            accountJSON.put("username", usernameEdt.getText().toString());
            accountJSON.put("password", passwordEdt.getText().toString());
            accountJSON.put("repass", rePassedt.getText().toString());
            accountJSON.put("email", emailEdt.getText().toString());
            accountJSON.put("number", numberEdt.getText().toString());
            accountJSON.put("provider", providerEdt.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return accountJSON;
    }



}
