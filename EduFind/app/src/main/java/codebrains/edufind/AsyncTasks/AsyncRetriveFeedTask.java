package codebrains.edufind.AsyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import codebrains.edufind.Utils.MessageCenter;
import codebrains.edufind.Utils.SystemControl;

/**
 * Async task that handles the check if the remote server is reachable by the application.
 */
public class AsyncRetriveFeedTask extends AsyncTask<String, Void, String> {

    private Activity mActivity;
    private ProgressDialog pDialog;

    //Constructor
    public AsyncRetriveFeedTask(Activity act) {
        this.mActivity = act;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog = new ProgressDialog(this.mActivity);
        pDialog.setMessage("Connection Check...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String[] params) {

        SystemControl sc = new SystemControl(this.mActivity);
        JSONObject jObj = new JSONObject();

        // If the server is not reachable.
        if(!sc.RemoteServerIsReachable()) {
            try {
                jObj.put("status", 1);
                jObj.put("title", "Connection Error");
                jObj.put("message", "There was an error while trying to " +
                        "connect to the server. It seems that the server is currently offline. Please " +
                        "try again later or contact the support team.");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return String.valueOf(jObj);
    }

    @Override
    protected void onPostExecute(String message) {

        // dismiss the dialog once product deleted
        pDialog.dismiss();

        try {
            JSONObject jsonObject = new JSONObject(message);

            if(jsonObject != null && jsonObject.get("status") == 1) {
                MessageCenter ms = new MessageCenter(this.mActivity);
                ms.FatalErrorDialogDisplay(this.mActivity, jsonObject.get("title").toString(),
                        jsonObject.get("message").toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
