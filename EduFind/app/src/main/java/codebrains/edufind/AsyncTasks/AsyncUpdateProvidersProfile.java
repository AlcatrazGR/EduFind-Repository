package codebrains.edufind.AsyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import codebrains.edufind.Utils.JSONParser;

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


        Log.d("Server Resp : ----- ", response.toString());

    }



}
