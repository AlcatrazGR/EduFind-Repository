package codebrains.edufind.AsyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import codebrains.edufind.Utils.JSONParser;
import codebrains.edufind.Utils.MessageCenter;

/**
 * Asynchronous task that handles the connection with the server to insert the new book.
 */
public class AsyncAddNewBook extends AsyncTask<String, String, JSONObject> {

    private Activity mActivity;
    private ProgressDialog pDialog;
    private JSONObject newBook;

    //Constructor
    public AsyncAddNewBook(Activity act, JSONObject nb) {
        this.mActivity = act;
        this.newBook = nb;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog = new ProgressDialog(this.mActivity);
        pDialog.setMessage("Adding Book...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }


    @Override
    protected JSONObject doInBackground(String... params) {

        JSONParser jp = new JSONParser();
        JSONObject response = jp.HttpRequestGetData("/AddBookController.php");

        return response;
    }

    @Override
    protected void onPostExecute(JSONObject response) {

        // dismiss the dialog once product deleted
        pDialog.dismiss();
        MessageCenter msgCenter = new MessageCenter(this.mActivity);

        try {
            msgCenter.DisplayErrorDialog("Data Result", response.get("message").toString());
        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "getStringExtra : " + e);
            msgCenter.DisplayErrorDialog("Server Error", "An error occurred while trying to communicate " +
                    "with the database. Please try again later or contact the support team.");
        }

    }



}
