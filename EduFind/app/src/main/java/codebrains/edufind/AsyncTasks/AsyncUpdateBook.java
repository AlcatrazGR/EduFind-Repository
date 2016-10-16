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
 * Asynchronous task that handles the update of a book registry.
 */
public class AsyncUpdateBook extends AsyncTask<String, String, JSONObject> {

    private Activity mActivity;
    private ProgressDialog pDialog;
    private JSONObject oldData;
    private JSONObject newData;

    //Constructor
    public AsyncUpdateBook(Activity act, JSONObject oldD, JSONObject newD) {
        this.mActivity = act;
        this.oldData = oldD;
        this.newData = newD;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog = new ProgressDialog(this.mActivity);
        pDialog.setMessage("Updating Book...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected JSONObject doInBackground(String[] params) {

        List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
        parameters.add(new BasicNameValuePair("newBookData", this.newData.toString()));
        parameters.add(new BasicNameValuePair("oldBookData", this.oldData.toString()));

        JSONParser jp = new JSONParser();
        JSONObject responseJSON = jp.HttpRequestPostData(parameters, "/UpdateBookData.php");

        return responseJSON;
    }

    @Override
    protected void onPostExecute(JSONObject response) {

        // dismiss the dialog once product deleted
        pDialog.dismiss();

        MessageCenter msgcenter = new MessageCenter(this.mActivity);
        try {
            msgcenter.DisplayErrorDialog("Update Result", response.get("message").toString());
        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException->onPostExecute : " + e);
        }

    }



}
