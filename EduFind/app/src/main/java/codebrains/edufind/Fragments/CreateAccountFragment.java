package codebrains.edufind.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import codebrains.edufind.R;
import codebrains.edufind.Utils.Coordinates;

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

        JSONObject geoJson = new JSONObject();
        Coordinates coordinates = new Coordinates(activity);

        JSONObject locationInfoJSON = coordinates.GetLocationInfoFromCoordinates();
        try {
            locationInfoJSON.put("longitude", coordinates.GetLongitude());
            locationInfoJSON.put("latitude", coordinates.GetLatitude());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Displaying results
        TextView longitudeTxt = (TextView) activity.findViewById(R.id.textView6);
        TextView latitudeTxt = (TextView) activity.findViewById(R.id.textView7);
        TextView cityTxt = (TextView) activity.findViewById(R.id.textView8);
        TextView postalTxt = (TextView) activity.findViewById(R.id.textView9);
        TextView addressTxt = (TextView) activity.findViewById(R.id.textView10);

        try {
            longitudeTxt.setText("Longitude :" + locationInfoJSON.get("longitude").toString());
            latitudeTxt.setText("Latitude :" + locationInfoJSON.get("latitude").toString());
            cityTxt.setText("City :" + locationInfoJSON.get("city").toString());
            postalTxt.setText("Postal Code :" + locationInfoJSON.get("postal").toString());
            addressTxt.setText("Address" + locationInfoJSON.get("address").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return geoJson;
    }


}
