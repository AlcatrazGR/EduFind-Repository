package codebrains.edufind.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import codebrains.edufind.AsyncTasks.AsyncDeleteAccount;
import codebrains.edufind.AsyncTasks.AsyncUpdateProvidersProfile;
import codebrains.edufind.Controllers.CreateAccountController;
import codebrains.edufind.R;
import codebrains.edufind.Utils.Cryptography;
import codebrains.edufind.Utils.SystemControl;

import static codebrains.edufind.Activities.ProviderActivity.GetUserData;

public class ProvidersProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_providers_profile_fragment, container, false);

        TextView usernameTv = (TextView) view.findViewById(R.id.provider_username);
        EditText emailEdt = (EditText) view.findViewById(R.id.provider_email);
        EditText numberEdt = (EditText) view.findViewById(R.id.provider_number);
        EditText providerEdt = (EditText) view.findViewById(R.id.provider_name);
        TextView longitudeTv = (TextView) view.findViewById(R.id.provider_longitude);
        TextView latitudeTv = (TextView) view.findViewById(R.id.provider_latitude);
        TextView cityTv = (TextView) view.findViewById(R.id.provider_city);
        TextView postalTv = (TextView) view.findViewById(R.id.provider_postal);
        TextView addressTv = (TextView) view.findViewById(R.id.provider_address);

        JSONObject userData = GetUserData();

        try {
            usernameTv.setText(userData.get("username").toString());
            emailEdt.setText(userData.get("mail").toString());
            numberEdt.setText(userData.get("num").toString());
            providerEdt.setText(userData.get("name").toString());

            longitudeTv.setText("Longitude: " + userData.get("lon").toString());
            latitudeTv.setText("Latitude: " + userData.get("lat").toString());
            cityTv.setText("City: " + userData.get("cty").toString());
            postalTv.setText("Postal: " + userData.get("pos").toString());
            addressTv.setText("Address: " + userData.get("adr").toString());
        } catch (JSONException e) {
            Log.d("Exception! ->", "JSONException: " + e);
        }


        return view;
    }


    /**
     * Method that calls the async task to update the account of the provider.
     * @param mActivity The activity that called the method.
     */
    public void UpdateProvidersProfile(Activity mActivity) {

        JSONObject profileInfo = new JSONObject();
        try {
            profileInfo.put("newProfileData", this.GetProvidersProfileData(mActivity).toString());
            profileInfo.put("oldProfileData", GetUserData());

            this.DisplayConfirmationDialog("Do you really want to update your account ?", mActivity,
                    "Update", profileInfo);

        } catch (JSONException e) {
            Log.e("Exception! ->", "JSONException: " + e);
        }

        Log.d("New account --- ", String.valueOf(GetUserData()));

    }

    /**
     * Method that handles the deletion of an provider account through the providers profile.
     * @param mActivity The activity that called the method.
     */
    public void DeleteProvidersProfile(Activity mActivity) {
        this.DisplayConfirmationDialog("Do you really want to delete your account ?", mActivity,
                "Delete", GetUserData());
    }

    /**
     * Method that displays a confirmation massage of `yes` or `no` to the user.
     * @param message The message of the dialog.
     * @param actObj Activity object.
     * @param process The process to follow, either deletion or acceptance of a user.
     * @param data The json object with all the users info.
     */
    private void DisplayConfirmationDialog(final String message, final Activity actObj,
        final String process, final JSONObject data) {

        boolean status = false;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(actObj);

        // Setting Dialog Title
        alertDialog.setTitle("Confirmation Message");

        // Setting Dialog Message
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                switch (process) {

                    case "Update":
                        AsyncUpdateProvidersProfile aupp = new AsyncUpdateProvidersProfile(actObj, data);
                        aupp.execute();
                    break;

                    case "Delete":
                        String username = null;
                        try {
                            username = data.get("username").toString();
                            AsyncDeleteAccount ada = new AsyncDeleteAccount(actObj, username);
                            ada.execute();
                        } catch (JSONException e) {
                            Log.e("Exception! ->", "JSONException: " + e);
                        }
                     break;
                }
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // Showing Alert Message
        alertDialog.show();

    }


    /**
     * Method that retrieves the geolocation info for update of the account and then it sets the
     * results to the UI.
     * @param mActivity The activity that called this method.
     */
    public void GetGeolocationOfUser(Activity mActivity) {

        CreateAccountController cac = new CreateAccountController();
        JSONObject geoJson = cac.HandleGeoLocationInfo(mActivity);

        TextView longitudeTv = (TextView) mActivity.findViewById(R.id.provider_longitude);
        TextView latitudeTv = (TextView) mActivity.findViewById(R.id.provider_latitude);
        TextView cityTv = (TextView) mActivity.findViewById(R.id.provider_city);
        TextView postalTv = (TextView) mActivity.findViewById(R.id.provider_postal);
        TextView addressTv = (TextView) mActivity.findViewById(R.id.provider_address);

        try {
            longitudeTv.setText("Longitude: " + geoJson.get("longitude").toString());
            latitudeTv.setText("Latitude: " + geoJson.get("latitude").toString());
            cityTv.setText("City: " + geoJson.get("city").toString());
            postalTv.setText("Postal: " + geoJson.get("postal").toString());
            addressTv.setText("Address: " + geoJson.get("address").toString());
        } catch (JSONException e) {
            Log.e("Exception! ->", "JSONException: " + e);
        }
    }

    /**

    /**
     * Method that gets all the current providers account information from the profile in order
     * to either update the account or delete it.
     * @param mActivity The activity that called this method.
     * @return Returns a json object with all the current account data.
     */
    private JSONObject GetProvidersProfileData(Activity mActivity) {

        JSONObject newProfileData = new JSONObject();
        Cryptography cpy = new Cryptography();
        SystemControl sc = new SystemControl(mActivity);

        TextView usernameTv = (TextView) mActivity.findViewById(R.id.provider_username);
        EditText passwordEdt = (EditText) mActivity.findViewById(R.id.provider_password);
        EditText emailEdt = (EditText) mActivity.findViewById(R.id.provider_email);
        EditText numberEdt = (EditText) mActivity.findViewById(R.id.provider_number);
        EditText providerEdt = (EditText) mActivity.findViewById(R.id.provider_name);
        TextView longitudeTv = (TextView) mActivity.findViewById(R.id.provider_longitude);
        TextView latitudeTv = (TextView) mActivity.findViewById(R.id.provider_latitude);
        TextView cityTv = (TextView) mActivity.findViewById(R.id.provider_city);
        TextView postalTv = (TextView) mActivity.findViewById(R.id.provider_postal);
        TextView addressTv = (TextView) mActivity.findViewById(R.id.provider_address);

        double longitude = sc.ConvertStringToDouble(sc.GetValuePartFromGeolocationDisplay(longitudeTv.getText().toString()));
        double latitude = sc.ConvertStringToDouble(sc.GetValuePartFromGeolocationDisplay(latitudeTv.getText().toString()));

        try {
            newProfileData.put("username", usernameTv.getText().toString().trim());
            newProfileData.put("password", cpy.HashSHA256(passwordEdt.getText().toString().trim()));
            newProfileData.put("mail", emailEdt.getText().toString().trim());
            newProfileData.put("lon", longitude);
            newProfileData.put("lat", latitude);
            newProfileData.put("cty", sc.GetValuePartFromGeolocationDisplay(cityTv.getText().toString()));
            newProfileData.put("pos", sc.GetValuePartFromGeolocationDisplay(postalTv.getText().toString()));
            newProfileData.put("adr", sc.GetValuePartFromGeolocationDisplay(addressTv.getText().toString()));
            newProfileData.put("num", numberEdt.getText().toString().trim());
            newProfileData.put("name", providerEdt.getText().toString());
        } catch (JSONException e) {
            Log.e("Exception! ->", "JSONException: " + e);
        }

        return newProfileData;
    }


}
