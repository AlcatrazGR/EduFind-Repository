package codebrains.edufind.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.json.JSONObject;
import codebrains.edufind.Controllers.CreateAccountController;
import codebrains.edufind.R;

/**
 * Fragment activity for account creation.
 */
public class CreateAccountFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_create_account_fragment, container, false);
        return view;
    }

    /**
     * Method that calls the Coordinates class to get the required geolocation data, and creates
     * a JSON object containing all of those.
     * @param activity The activity object that called this method.
     * @return Returns a json object containing all the geolocation info.
     */
    public JSONObject GetGeolocationInfo(Activity activity) {

        CreateAccountController cac = new CreateAccountController();
        JSONObject geoJson = cac.HandleGeoLocationInfo(activity);

        return geoJson;
    }

    /**
     * Method that handles the account creation of a user.
     * @param mActivity The activity that called this method.
     */
    public void CreateAccount(Activity mActivity, JSONObject geoInfo) {

        CreateAccountController cac = new CreateAccountController();

        if(cac.CreateAccount(mActivity, geoInfo)) {
            JSONObject accountJSON = cac.GetAccountJSON();
            Log.d("Account JSON : ", accountJSON.toString());
        }

    }


}
