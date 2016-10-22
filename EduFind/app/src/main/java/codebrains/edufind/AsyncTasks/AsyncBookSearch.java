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
 * Asynchronous task that handles the search of books by a certain sorting method.
 */
public class AsyncBookSearch extends AsyncTask<String, String, JSONObject> {

    private Activity mActivity;
    private ProgressDialog pDialog;
    private JSONObject data;

    //Constructor
    public AsyncBookSearch(Activity act, JSONObject dt) {
        this.mActivity = act;
        this.data = dt;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog = new ProgressDialog(this.mActivity);
        pDialog.setMessage("Setting Book List...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected JSONObject doInBackground(String... params) {






        //List<NameValuePair> parameters = new ArrayList<NameValuePair>(1);
       // parameters.add(new BasicNameValuePair("data", this.newBook.toString()));

        //JSONParser jp = new JSONParser();
        //JSONObject response = jp.HttpRequestPostData(parameters, "/AddBookController.php");

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject response) {

        // dismiss the dialog once product deleted
        pDialog.dismiss();


    }

}
