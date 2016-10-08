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
import codebrains.edufind.Interfaces.IAsyncResponse;
import codebrains.edufind.Models.AdminPanel;
import codebrains.edufind.Utils.JSONParser;
import codebrains.edufind.Utils.MessageCenter;

/**
 * Asynchronous task that handles the accept of a users request.
 */
public class AsyncAcceptUsersRequest extends AsyncTask<String, String, JSONObject> {

    private Activity mActivity;
    private ProgressDialog pDialog;
    private String username;
    private JSONObject usersData;

    public IAsyncResponse delegate; //Interface Object

    //Constructor
    public AsyncAcceptUsersRequest(String usname, Activity act, JSONObject data) {
        this.mActivity = act;
        this.username = usname;
        this.usersData = data;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog = new ProgressDialog(this.mActivity);
        pDialog.setMessage("Accepting User...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }


    @Override
    protected JSONObject doInBackground(String... params) {

        List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
        parameters.add(new BasicNameValuePair("username", this.username));
        parameters.add(new BasicNameValuePair("process", "1"));

        JSONParser jp = new JSONParser();
        JSONObject responseJSON = jp.HttpRequestPostData(parameters, "/AdminController.php");

        return responseJSON;
    }

    @Override
    protected void onPostExecute(JSONObject response) {

        // dismiss the dialog once product deleted
        pDialog.dismiss();

        if(response != null) {
            this.delegate.ProcessFinish(response, this.mActivity);
        }
        else {
            MessageCenter msgCenter = new MessageCenter(this.mActivity);
            msgCenter.DisplayErrorDialog("Server Error", "An error occurred while trying to communicate " +
                    "with the database. Please try again later or contact the support team.");
        }




        /*
        if(response != null) {

            try {
                if(response.get("status") == 1) {
                    AdminPanel ap = new AdminPanel();
                    JSONObject result = ap.RemoveUserFromUsersJson(this.usersData, this.username);
                    this.delegate.ProcessFinish(result, this.mActivity);
                }
                else {
                    MessageCenter msgCenter = new MessageCenter(this.mActivity);
                    msgCenter.DisplayErrorDialog("User Accept Error",
                            response.get("message").toString());
                }

            } catch (JSONException e) {
                Log.e("Exception ! ->", "JSONException :" + e);
            }
        }
        else {
            MessageCenter msgCenter = new MessageCenter(this.mActivity);
            msgCenter.DisplayErrorDialog("User Accept Error", "Error occurred while trying to retrieve " +
                    "data from database. This can be either a malfunction or maintenance on the " +
                    "server, or hardware error on you mobile phone. To ensure that its not a hardware " +
                    "problem close the wifi, restart your mobile phone and boot the app once again. " +
                    "If this doesnt help you please contact the support team.");
        }
        */

    }

}
