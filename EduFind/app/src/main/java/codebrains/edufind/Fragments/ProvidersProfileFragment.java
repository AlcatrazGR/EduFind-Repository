package codebrains.edufind.Fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import codebrains.edufind.AsyncTasks.AsyncUpdateProvidersProfile;
import codebrains.edufind.R;
import codebrains.edufind.Utils.Cryptography;

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


    public void UpdateProvidersProfile(Activity mActivity) {

        JSONObject profileInfo = new JSONObject();
        try {
            profileInfo.put("newProfileData", this.GetProvidersProfileData(mActivity).toString());
            profileInfo.put("oldProfileData", GetUserData());

            Log.d("-- Whole JSON --", profileInfo.toString());

            AsyncUpdateProvidersProfile aupp = new AsyncUpdateProvidersProfile(mActivity, profileInfo);
            aupp.execute();

        } catch (JSONException e) {
            Log.d("Exception! ->", "JSONException: " + e);
        }



    }

    public void DeleteProvidersProfile(Activity mActivity) {

    }

    /**
     * Method that gets all the current providers account information from the profile in order
     * to either update the account or delete it.
     * @param mActivity The activity that called this method.
     * @return Returns a json object with all the current account data.
     */
    private JSONObject GetProvidersProfileData(Activity mActivity) {

        JSONObject newProfileData = new JSONObject();
        Cryptography cpy = new Cryptography();

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

        try {
            newProfileData.put("username", usernameTv.getText().toString().trim());
            newProfileData.put("password", cpy.HashSHA256(passwordEdt.getText().toString().trim()));
            newProfileData.put("mail", emailEdt.getText().toString().trim());
            newProfileData.put("lon", longitudeTv.getText().toString());
            newProfileData.put("lat", latitudeTv.getText().toString());
            newProfileData.put("cty", cityTv.getText().toString());
            newProfileData.put("pos", postalTv.getText().toString());
            newProfileData.put("adr", addressTv.getText().toString());
            newProfileData.put("num", numberEdt.getText().toString().trim());
            newProfileData.put("name", providerEdt.getText().toString());
        } catch (JSONException e) {
            Log.d("Exception! ->", "JSONException: " + e);
        }

        return newProfileData;
    }


}
