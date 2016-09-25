package codebrains.edufind.Controllers;

import android.app.Activity;
import org.json.JSONObject;
import codebrains.edufind.Models.CreateAccount;

/**
 * Controller class that handles the calls to the model class for the process of creating a new
 * account.
 */
public class CreateAccountController {

    private JSONObject accountJSON;

    //Constructor
    public CreateAccountController(){
        this.accountJSON = new JSONObject();
    }

    /**
     * Method that calls the appropriate methods to handle the geolocation info rendering.
     * @param mActivity The activity that called this method.
     * @return Returns a JSON object that holds all the geolocation info of the user.
     */
    public JSONObject HandleGeoLocationInfo(Activity mActivity) {

        CreateAccount ca = new CreateAccount();
        JSONObject geoInfo = ca.GetCurrentGeoLocationInfo(mActivity);
        ca.DisplayGeolocationInfo(mActivity, geoInfo);

        return geoInfo;
    }

    /**
     * Method that calls the appropriate methods of the model to validate the account creation.
     * @param mActivity The activity that called this method.
     * @param geoInfo The JSON that holds all the current geolocation info of the user.
     * @return Returns a boolean variable that shows the status of the check process.
     */
    public boolean CreateAccount(Activity mActivity, JSONObject geoInfo) {

        boolean check = false;
        CreateAccount ca = new CreateAccount();

        //If the geolocation info have been initialized successfully.
        if(ca.CheckGeoLocationInfoJSON(mActivity, geoInfo)) {

            this.accountJSON = ca.CreateNewAccountJSONObject(mActivity, geoInfo);
            check = true;
        }

        return check;
    }

    /**
     * Method that returns the private attribute JSON.
     * @return Returns a JSON with all the new account data needed for creation.
     */
    public JSONObject GetAccountJSON() {
        return this.accountJSON;
    }

}
