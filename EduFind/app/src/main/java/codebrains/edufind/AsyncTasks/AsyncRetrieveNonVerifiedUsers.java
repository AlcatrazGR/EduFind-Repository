package codebrains.edufind.AsyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import org.json.JSONObject;

import codebrains.edufind.Interfaces.IAsyncResponse;
import codebrains.edufind.Utils.JSONParser;
import codebrains.edufind.Utils.MessageCenter;

/**
 * Asynchronous task that handles the process of retrieving all the user account requests in
 * order to initialize the admins panel.
 */
public class AsyncRetrieveNonVerifiedUsers extends AsyncTask<String, String, JSONObject> {

    private Activity mActivity;
    private ProgressDialog pDialog;

    public IAsyncResponse delegate; //Interface Object

    //Constructor
    public AsyncRetrieveNonVerifiedUsers(Activity act) {
        this.mActivity = act;
        this.delegate = null;
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

        JSONParser jp = new JSONParser();
        JSONObject userAccInfo = jp.HttpRequestGetData("/AccountInfoController.php");

        return userAccInfo;
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


    }



}
