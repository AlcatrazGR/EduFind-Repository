package codebrains.edufind.AsyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import codebrains.edufind.Interfaces.IAsyncResponse;
import codebrains.edufind.Utils.JSONParser;
import codebrains.edufind.Utils.MessageCenter;

/**
 * Asynchronous task that handles the deletion of a users request.
 */
public class AsyncDeleteUsersRequest extends AsyncTask<String, String, JSONObject> {

    private Activity mActivity;
    private ProgressDialog pDialog;
    private String username;
    private JSONObject usersData;

    public IAsyncResponse delegate; //Interface Object

    //Constructor
    public AsyncDeleteUsersRequest(String usname, Activity act, JSONObject data) {
        this.mActivity = act;
        this.username = usname;
        this.usersData = data;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog = new ProgressDialog(this.mActivity);
        pDialog.setMessage("Deleting User...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }


    @Override
    protected JSONObject doInBackground(String... params) {

         List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
         parameters.add(new BasicNameValuePair("username", this.username));
         parameters.add(new BasicNameValuePair("process", "2"));

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

    }


}
