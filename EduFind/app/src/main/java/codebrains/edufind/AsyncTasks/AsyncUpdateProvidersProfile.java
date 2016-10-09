package codebrains.edufind.AsyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import codebrains.edufind.R;
import codebrains.edufind.Utils.JSONParser;
import codebrains.edufind.Utils.MessageCenter;

import static codebrains.edufind.Activities.ProviderActivity.GetUserData;
import static codebrains.edufind.Activities.ProviderActivity.SetUserData;

/**
 * Asynchronous task that handles the update process of the providers profile.
 */
public class AsyncUpdateProvidersProfile extends AsyncTask<String, String, JSONObject> {

    private Activity mActivity;
    private ProgressDialog pDialog;
    private JSONObject profileInfo;

    //Constructor
    public AsyncUpdateProvidersProfile(Activity act, JSONObject data) {
        this.mActivity = act;
        this.profileInfo = data;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog = new ProgressDialog(this.mActivity);
        pDialog.setMessage("Updating Profile...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected JSONObject doInBackground(String[] params) {

        List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
        try {
            parameters.add(new BasicNameValuePair("newProfileData", this.profileInfo.get("newProfileData").toString()));
            parameters.add(new BasicNameValuePair("oldProfileData", this.profileInfo.get("oldProfileData").toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONParser jp = new JSONParser();
        JSONObject responseJSON = jp.HttpRequestPostData(parameters, "/UpdateAccountController.php");


        return responseJSON;
    }

    @Override
    protected void onPostExecute(JSONObject response) {

        // dismiss the dialog once product deleted
        pDialog.dismiss();
        MessageCenter msgCenter = new MessageCenter(this.mActivity);

        if(response != null) {

            try {
                int status = (int) response.get("status");
                switch (status) {

                    case 0:
                        msgCenter.DisplayErrorDialog(response.get("title").toString(),
                                response.get("message").toString());
                        break;

                    case 1:
                        msgCenter.DisplayErrorDialog(response.get("title").toString(),
                                response.get("message").toString());
                        JSONObject newProfileData = new JSONObject(this.profileInfo.get("newProfileData").toString());
                        SetUserData(newProfileData);

                        TextView usernameTv = (TextView) this.mActivity.findViewById(R.id.provider_username);
                        EditText emailEdt = (EditText) this.mActivity.findViewById(R.id.provider_email);
                        EditText numberEdt = (EditText) this.mActivity.findViewById(R.id.provider_number);
                        EditText providerEdt = (EditText) this.mActivity.findViewById(R.id.provider_name);
                        TextView longitudeTv = (TextView) this.mActivity.findViewById(R.id.provider_longitude);
                        TextView latitudeTv = (TextView) this.mActivity.findViewById(R.id.provider_latitude);
                        TextView cityTv = (TextView) this.mActivity.findViewById(R.id.provider_city);
                        TextView postalTv = (TextView) this.mActivity.findViewById(R.id.provider_postal);
                        TextView addressTv = (TextView) this.mActivity.findViewById(R.id.provider_address);

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
                            Log.e("Exception! ->", "JSONException: " + e);
                        }


                        break;
                }

            } catch (JSONException e) {
                Log.e("Exception! ->", "JSONException: " + e);
            }
        }
        else {
            msgCenter.DisplayErrorDialog("Login Error", "Error occurred while trying to retrieve " +
                "data from database. This can be either a malfunction or maintenance on the " +
                "server, or hardware error on you mobile phone. To ensure that its not a hardware " +
                "problem close the wifi, restart your mobile phone and boot the app once again. " +
                "If this doesnt help you please contact the support team.");
        }

    }



}
