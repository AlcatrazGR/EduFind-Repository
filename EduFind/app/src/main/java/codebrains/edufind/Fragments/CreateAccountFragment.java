package codebrains.edufind.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import codebrains.edufind.R;
import codebrains.edufind.Utils.Coordinates;
import codebrains.edufind.Utils.MessageCenter;

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

        JSONObject geoJson;

        return geoJson;
    }

    /**
     * Method that handles the account creation of a user.
     * @param mActivity The activity that called this method.
     */
    public void CreateAccount(Activity mActivity, JSONObject geoInfo) {


    }


}
