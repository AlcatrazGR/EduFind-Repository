package codebrains.edufind.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import codebrains.edufind.AsyncTasks.AsyncCreateAccount;
import codebrains.edufind.Controllers.CreateAccountController;
import codebrains.edufind.R;
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

        CreateAccountController cac = new CreateAccountController();
        JSONObject geoJson = cac.HandleGeoLocationInfo(activity);

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

            try {
                if(accountJSON.get("password").toString().length() >= 6 &&
                        accountJSON.get("password").toString().length() <= 12 ) {

                    AsyncCreateAccount aca = new AsyncCreateAccount(mActivity, accountJSON);
                    aca.execute();
                }
                else {
                    String title = "Result";
                    String message = "The `password` field must be between 6 and 12 characters long.";
                    MessageCenter msgCent = new MessageCenter(mActivity);
                    msgCent.DisplayErrorDialog(title, message);
                }

            } catch (JSONException e) {
                Log.e("Exception! ->", "JSONException : CreateAccount ->"+e);
            }

        }

    }


}
