package codebrains.edufind.Controllers;

import android.app.Activity;
import org.json.JSONObject;
import codebrains.edufind.Models.CreateAccount;

/**
 * Created by Vasilhs on 9/25/2016.
 */
public class CreateAccountController {

    private JSONObject accountJSON;

    public CreateAccountController(){
        this.accountJSON = new JSONObject();
    }

    public JSONObject HandleGeoLocationInfo(Activity mActivity) {

        CreateAccount ca = new CreateAccount();
        JSONObject geoInfo = ca.GetCurrentGeoLocationInfo(mActivity);
        ca.DisplayGeolocationInfo(mActivity, geoInfo);

        return geoInfo;
    }

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

    public JSONObject GetAccountJSON() {
        return this.accountJSON;
    }

}
