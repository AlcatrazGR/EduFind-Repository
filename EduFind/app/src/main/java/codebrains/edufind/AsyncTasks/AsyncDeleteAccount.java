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
import codebrains.edufind.Utils.MessageCenter;


/**
 * Asynchronous task that calls the appropriate script on the server to handle the account
 * deletion of a provider and also all the related book registries the provider previously added.
 */
public class AsyncDeleteAccount extends AsyncTask<String, String, JSONObject> {


    private Activity mActivity;
    private ProgressDialog pDialog;
    private String username;

    //Constructor
    public AsyncDeleteAccount(Activity act, String user ) {
        this.mActivity = act;
        this.username = user;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog = new ProgressDialog(this.mActivity);
        pDialog.setMessage("Deleting Account...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected JSONObject doInBackground(String... params) {

        List<NameValuePair> parameters = new ArrayList<NameValuePair>(1);
        parameters.add(new BasicNameValuePair("username", this.username));

        JSONParser jp = new JSONParser();
        JSONObject response = jp.HttpRequestPostData(parameters, "/DeleteProviderController.php");

        return response;
    }

    @Override
    protected void onPostExecute(JSONObject response) {

        // dismiss the dialog once product deleted
        pDialog.dismiss();
        MessageCenter msgCenter = new MessageCenter(mActivity);

        try {
            if(response.get("status") == 1) {

                msgCenter.FatalErrorDialogDisplay(mActivity, "Deletion Result",
                        response.get("message").toString());

            }
            else {
                msgCenter.DisplayErrorDialog("Deletion Error", response.get("message").toString());
            }
        } catch (JSONException e) {
            Log.e("Exception! ->", "JSONException : " + e);
            msgCenter.DisplayErrorDialog("Server Error", "An error occurred while trying to communicate " +
                    "with the database. Please try again later or contact the support team.");
        }





    }


}
