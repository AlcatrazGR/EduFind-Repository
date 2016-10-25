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

import codebrains.edufind.Controllers.CreateAccountController;
import codebrains.edufind.Interfaces.IAsyncResponse;
import codebrains.edufind.Utils.JSONParser;
import codebrains.edufind.Utils.MessageCenter;
import static codebrains.edufind.Activities.StudentActivity.GetStudentGeolocationInfo;
import static codebrains.edufind.Activities.StudentActivity.SetSortedBookList;
import static codebrains.edufind.Activities.StudentActivity.SetStudentGeolocationInfo;


/**
 * Asynchronous task that handles the search of books by a certain sorting method.
 */
public class AsyncBookSearch extends AsyncTask<String, String, JSONObject> {

    private Activity mActivity;
    private ProgressDialog pDialog;
    private JSONObject data;
    private JSONObject studentGeoInfo;
    private boolean citySortFlag;

    public IAsyncResponse delegate; //Interface Object

    //Constructor
    public AsyncBookSearch(Activity act, JSONObject dt, boolean csf) {
        this.mActivity = act;
        this.data = dt;
        this.citySortFlag = csf;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog = new ProgressDialog(this.mActivity);
        pDialog.setMessage("Setting Book List...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        studentGeoInfo = GetStudentGeolocationInfo();
        Log.d("-- 1 Geo Info --", studentGeoInfo.toString());

        try {
            if(studentGeoInfo.get("longitude") == 0 || studentGeoInfo.get("latitude") == 0) {
                //Setting new geolocation info for the student.
                CreateAccountController cac = new CreateAccountController();
                studentGeoInfo = cac.HandleGeoLocationInfo(this.mActivity);
                SetStudentGeolocationInfo(cac.HandleGeoLocationInfo(this.mActivity));
            }
        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException : " + e);
        }

    }

    @Override
    protected JSONObject doInBackground(String... params) {

        Log.d("-- 2 Geo Info --", studentGeoInfo.toString());

        try {
            if(citySortFlag)
                this.data.put("city", studentGeoInfo.get("city"));
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
                    this.delegate.ProcessFinish(response, this.mActivity);
                break;

            }

        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException : " + e);
        }


    }

}
