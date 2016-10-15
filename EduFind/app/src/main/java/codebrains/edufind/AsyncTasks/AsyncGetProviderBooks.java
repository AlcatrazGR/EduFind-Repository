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
import codebrains.edufind.Utils.JSONParser;
import codebrains.edufind.Utils.MessageCenter;

/**
 * Asynchronous task ta retrieves all the book registered by the provider.
 */
public class AsyncGetProviderBooks extends AsyncTask<String, String, JSONObject> {


    private Activity mActivity;
    private ProgressDialog pDialog;
    private JSONObject data;

    public IAsyncResponse delegate; //Interface Object

    //Constructor
    public AsyncGetProviderBooks(Activity act, JSONObject data) {
        this.mActivity = act;
        this.data = data;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog = new ProgressDialog(this.mActivity);
        pDialog.setMessage("Retrieving Information...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }


    @Override
    protected JSONObject doInBackground(String... params) {

        List<NameValuePair> parameters = new ArrayList<NameValuePair>(1);
        parameters.add(new BasicNameValuePair("data", data.toString()));

        JSONParser jp = new JSONParser();
        JSONObject responseJSON = jp.HttpRequestPostData(parameters, "/GetBooksController.php");

        return responseJSON;
    }

    @Override
    protected void onPostExecute(JSONObject response) {

        // dismiss the dialog once product deleted
        pDialog.dismiss();

        try {

            int status = Integer.parseInt(response.get("status").toString());
            switch(status) {

                //An error occurred.
                case 0:
                    MessageCenter msgCent = new MessageCenter(mActivity);
                    msgCent.DisplayErrorDialog("Data Error", response.get("message").toString());
                break;

                default:
                    this.delegate.ProcessFinish(response, this.mActivity);
                break;

            }

        }
        catch (JSONException e) {
            Log.e("Exception! ->", "JSONException : " + e);
            MessageCenter msgCent = new MessageCenter(mActivity);
            msgCent.DisplayErrorDialog("Server Error", "An error occurred while trying to communicate " +
                    "with the database. Please try again later or contact the support team.");
        }


    }


}
