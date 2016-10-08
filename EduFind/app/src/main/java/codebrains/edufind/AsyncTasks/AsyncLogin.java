package codebrains.edufind.AsyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import codebrains.edufind.Activities.AdminActivity;
import codebrains.edufind.Activities.ProviderActivity;
import codebrains.edufind.Utils.Cryptography;
import codebrains.edufind.Utils.JSONParser;
import codebrains.edufind.Utils.MessageCenter;

/**
 * Asynchronous task that handles the login process.
 */
public class AsyncLogin extends AsyncTask<String, String, JSONObject> {

    private Activity mActivity;
    private ProgressDialog pDialog;
    private JSONObject loginJSON;

    //Constructor
    public AsyncLogin(Activity act, JSONObject jsonObject) {
        this.mActivity = act;
        this.loginJSON = jsonObject;
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

        try {

            if(this.loginJSON.get("username") != "" && this.loginJSON.get("password") != "") {

                //Hash the password
                Cryptography crpy = new Cryptography();
                this.loginJSON.put("password", crpy.HashSHA256(this.loginJSON.get("password").toString()));

                List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
                parameters.add(new BasicNameValuePair("usname", this.loginJSON.get("username").toString()));
                parameters.add(new BasicNameValuePair("pass", this.loginJSON.get("password").toString()));

                JSONParser jp = new JSONParser();
                JSONObject responseJSON = jp.HttpRequestPostData(parameters, "/LoginController.php");

                return responseJSON;
            }
            else {
                MessageCenter msgCenter = new MessageCenter(this.mActivity);
                msgCenter.DisplayErrorDialog("Login Error", "One or more fields in the login form" +
                        " are empty. Please fill all the necessary fields and resubmit the form.");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(JSONObject response) {

        // dismiss the dialog once product deleted
        pDialog.dismiss();
        MessageCenter msgCenter = new MessageCenter(this.mActivity);

        if(response != null) {

            Log.d("Login Response : ", String.valueOf(response));

            try {
                if(response.get("status") == 1) {

                    Intent intent = null;
                    int type = (int) response.get("type");
                    switch(type) {

                        case 0 : //simple user
                            intent = new Intent(this.mActivity, ProviderActivity.class);
                            intent.putExtra("userdata", String.valueOf(response));
                            this.mActivity.finish();
                            this.mActivity.startActivity(intent);
                        break;

                        case 1 : //admin user
                            intent = new Intent(this.mActivity, AdminActivity.class);
                            this.mActivity.finish();
                            this.mActivity.startActivity(intent);
                        break;
                    }

                }
                else {
                    msgCenter.DisplayErrorDialog("Login Error", response.get("message").toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else {
            msgCenter.DisplayErrorDialog("Login Error", "Error occurred while trying to retrieve " +
                    "data from database. This can be either a malfunction or maintenance on the " +
                    "server, or hardware error on you mobile phone. To ensure that its not a hardware " +
                    "problem close the wifi, restart your mobile phone and boot the app once again. " +
                    "If this doesnt help you please contact the support team.");
        }



    }


}
