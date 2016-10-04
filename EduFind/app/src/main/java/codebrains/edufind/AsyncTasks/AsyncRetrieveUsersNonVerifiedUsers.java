package codebrains.edufind.AsyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import org.json.JSONObject;

/**
 * Asynchronous task that handles the process of retrieving all the user account requests in
 * order to initialize the admins panel.
 */
public class AsyncRetrieveUsersNonVerifiedUsers extends AsyncTask<String, String, JSONObject> {

    private Activity mActivity;
    private ProgressDialog pDialog;

    //Constructor
    public AsyncRetrieveUsersNonVerifiedUsers(Activity act) {
        this.mActivity = act;
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

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject response) {

        // dismiss the dialog once product deleted
        pDialog.dismiss();

    }



}
