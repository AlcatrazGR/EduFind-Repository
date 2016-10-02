package codebrains.edufind.AsyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import codebrains.edufind.Interfaces.IAsyncResponse;
import codebrains.edufind.Utils.Cryptography;
import codebrains.edufind.Utils.JSONParser;
import codebrains.edufind.Utils.MessageCenter;

/**
 * Asynchronous task that handles the login process.
 */
public class AsyncLogin extends AsyncTask<String, String, JSONObject> {

    private Activity mActivity;
    private ProgressDialog pDialog;
    private JSONObject loginJSON;

    public IAsyncResponse delegate = null;

    //Constructor
    public AsyncLogin(Activity act, JSONObject jsonObject) {
        this.mActivity = act;
        this.loginJSON = jsonObject;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog = new ProgressDialog(this.mActivity);
        pDialog.setMessage("Login To System...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }


    @Override
    protected JSONObject doInBackground(String... params) {

        try {

            if(this.loginJSON.get("username") != "" && this.loginJSON.get("password") != "") {

                //Hash the password
                Cryptography crpy = new Cryptography();
                this.loginJSON.put("password", crpy.HashSHA256(this.loginJSON.get("password").toString()));

                List<NameValuePair> parameters = new ArrayList<NameValuePair>();
                parameters.add(new BasicNameValuePair("data", this.loginJSON.toString()));

                JSONParser jp = new JSONParser();
                JSONObject responseJSON = jp.HttpRequestPostData(parameters, "/LoginController.php");

                return responseJSON;
            }
            else {
                MessageCenter msgCenter = new MessageCenter(this.mActivity);
                msgCenter.DisplayErrorDialog("Login Error", "One or more fields in the login form" +
                        " are empty. Please fill all the necessary fields and resubmit the form.");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(JSONObject response) {

        // dismiss the dialog once product deleted
        pDialog.dismiss();

        if(response != null) {

            this.delegate.processFinish(response); //add the response to the interface object.

        }
        else {
            MessageCenter msgCenter = new MessageCenter(this.mActivity);
            msgCenter.DisplayErrorDialog("Login Error", "Error occurred while trying to retrieve " +
                    "data from database. Please try again later or contact the support.");
        }



    }


}
