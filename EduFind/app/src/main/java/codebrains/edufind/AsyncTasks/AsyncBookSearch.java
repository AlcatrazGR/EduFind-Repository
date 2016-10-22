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
import static codebrains.edufind.Activities.StudentActivity.GetStudentGeolocationInfo;
import static codebrains.edufind.Activities.StudentActivity.SetSortedBookList;

/**
 * Asynchronous task that handles the search of books by a certain sorting method.
 */
public class AsyncBookSearch extends AsyncTask<String, String, JSONObject> {

    private Activity mActivity;
    private ProgressDialog pDialog;
    private JSONObject data;

    public IAsyncResponse delegate; //Interface Object

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

        Log.d("-- Geo Info --", GetStudentGeolocationInfo().toString());

        try {
            this.data.put("city", GetStudentGeolocationInfo().get("city"));
        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException : " + e);
        }

        Log.d("-- Data Sent --", this.data.toString());

        List<NameValuePair> parameters = new ArrayList<NameValuePair>(1);
        parameters.add(new BasicNameValuePair("data", this.data.toString()));

        JSONParser jp = new JSONParser();
        JSONObject response = jp.HttpRequestPostData(parameters, "/GetBooksController.php");

        return response;
    }

    @Override
    protected void onPostExecute(JSONObject response) {

        // dismiss the dialog once product deleted
        pDialog.dismiss();

        try {
            int status = Integer.parseInt(response.get("status").toString());

            switch (status) {

                //case error
                case 0 :
                    MessageCenter msgCent = new MessageCenter(this.mActivity);
                    msgCent.FatalErrorDialogDisplay(this.mActivity, "Data Error",
                            response.get("message").toString());
                break;

                //case everything ok and data came or everything ok but no data.
                case 1:
                case 2:
                    SetSortedBookList(response);
                    this.delegate.ProcessFinish(response, this.mActivity);
                break;

            }

        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException : " + e);
        }


    }

}
