package codebrains.edufind.Controllers;

import android.app.Activity;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import codebrains.edufind.Models.CreateAccount;
import codebrains.edufind.R;
import codebrains.edufind.Utils.Coordinates;

/**
 * Created by Vasilhs on 9/25/2016.
 */
public class CreateAccountController {

    JSONObject accountJSON;

    public CreateAccountController(){
        this.accountJSON = new JSONObject();
    }

    public JSONObject DisplayGeoLocationInfo(Activity mActivity) {

        CreateAccount ca = new CreateAccount();







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

}
