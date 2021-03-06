package codebrains.edufind.Models;

import android.app.Activity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;
import codebrains.edufind.R;
import codebrains.edufind.Utils.Coordinates;
import codebrains.edufind.Utils.MessageCenter;

/**
 * Class that handles processes associated with account creation.
 */
public class CreateAccount {

    //Constructor
    public CreateAccount() {

    }

    /**
     * Method that gets the current geolocation info of a user's position (address, longitude, latitude,
     * postal code etc).
     * @param mActivity The activity that called this method.
     * @return Returns a JSON object that contains all the geolocation info.
     */
    public JSONObject GetCurrentGeoLocationInfo(Activity mActivity) {

        JSONObject geoJson;
        Coordinates coordinates = new Coordinates(mActivity);
        geoJson = coordinates.GetLocationInfoFromCoordinates();

        try {
            geoJson.put("longitude", coordinates.GetLongitude());
            geoJson.put("latitude", coordinates.GetLatitude());

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return geoJson;
    }

    /**
     * Method that displays an error message to the user if the terms and conditions are not agreed.
     * @param mActivity The activity that called this method.
     * @return Returns a boolean representing the status of the check.
     */
    public boolean CheckIfTermsAreAgreed(Activity mActivity) {

        boolean check = true;
        CheckBox termsChkbx = (CheckBox) mActivity.findViewById(R.id.checkBox);

        if(!termsChkbx.isChecked()) {
            String title = "Terms & Conditions Error";
            String message = "In order to create a new account you need to agree to our" +
                    " terms & conditions. Please check the checkbox on the account creation" +
                    " form to proceed.";
            check = false;
            MessageCenter msgCent = new MessageCenter(mActivity);
            msgCent.DisplayErrorDialog(title, message);

        }

        return check;
    }


    /**
     * Method that checks if the geolocation info object is empty (happens if the user didn't press
     * the render geolocation info button or if an error occurred and the object wasnt initialized
     * correctly).
     * @param mActivity The activity that called this method.
     * @param geoInfo The JSON that holds all the geolocation info.
     * @return Returns a boolean variable that represents the status of the check.
     */
    public boolean CheckGeoLocationInfoJSON(Activity mActivity, JSONObject geoInfo) {

        boolean check = true;
        MessageCenter msgCent = new MessageCenter(mActivity);
        if(geoInfo.length() == 0) {
            String title = "Error Occurred";
            String message = "The geolocation information are not initialized! Please press the button" +
                    " with the gps icon on the form to initialize geolocation info.";
            msgCent.DisplayErrorDialog(title, message);
            check = false;
        }

        return check;
    }

    /**
     * Method that after a successfull rendering of the geolocation info it creates a full JSON object
     * holding all the data of the new account to be created.
     * @param mActivity The activity that called the method.
     * @param geoInfo The JSON that holds all the geolocation info.
     * @return Returns a JSON object with all the new account data.
     */
    public JSONObject CreateNewAccountJSONObject(Activity mActivity, JSONObject geoInfo) {

        JSONObject accountJSON = geoInfo;

        EditText usernameEdt = (EditText) mActivity.findViewById(R.id.editText);
        EditText passwordEdt = (EditText) mActivity.findViewById(R.id.editText2);
        EditText rePassedt = (EditText) mActivity.findViewById(R.id.editText3);
        EditText emailEdt = (EditText) mActivity.findViewById(R.id.editText4);
        EditText numberEdt = (EditText) mActivity.findViewById(R.id.editText5);
        EditText providerEdt = (EditText) mActivity.findViewById(R.id.editText6);

        try {
            accountJSON.put("username", usernameEdt.getText().toString().trim());
            accountJSON.put("password", passwordEdt.getText().toString().trim());
            accountJSON.put("repass", rePassedt.getText().toString().trim());
            accountJSON.put("email", emailEdt.getText().toString().trim());
            accountJSON.put("number", numberEdt.getText().toString().trim());
            accountJSON.put("provider", providerEdt.getText().toString().trim());
        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException : CreateNewAccountJSONObject->" + e);
        }

        return accountJSON;
    }



}
