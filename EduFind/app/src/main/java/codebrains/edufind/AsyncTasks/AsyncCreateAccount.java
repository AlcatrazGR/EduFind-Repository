package codebrains.edufind.AsyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.provider.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import codebrains.edufind.Utils.Cryptography;
import codebrains.edufind.Utils.JSONParser;

/**
 * Asynchronous task for communicating with the remote server to create a new account. There are three
 * different methods inherited by the AsyncTask class, the OnPreExecute which runs before the the main
 * thread, doInBackground which is the main functionality of the thread and lastly the OnPostExecute
 * that runs when the main thread is finished.
 */
public class AsyncCreateAccount extends AsyncTask<String, String, String> {

    private ProgressDialog pDialog;
    private Activity mActivity;
    private JSONObject accountJSON;

    //Constructor
    public AsyncCreateAccount(Activity activity, JSONObject account) {
        this.mActivity = activity;
        this.accountJSON = account;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog = new ProgressDialog(this.mActivity);
        pDialog.setMessage("Creating Account...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        Cryptography crpy = new Cryptography();
        try {
            //Create a hash of the password fields.
            this.accountJSON.put("password", crpy.HashSHA256((String) this.accountJSON.get("password")));
            this.accountJSON.put("repass", crpy.HashSHA256((String) this.accountJSON.get("repass")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONParser jp = new JSONParser();
        try {
            jp.HttpRequest("http://www.edufind.comlu.com/Android/test.php", "POST");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(String response) {

        // dismiss the dialog once product deleted
        pDialog.dismiss();


    }


}