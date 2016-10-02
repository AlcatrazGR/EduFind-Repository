package codebrains.edufind.AsyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * Created by Vasilhs on 10/2/2016.
 */
public class AsyncLogin extends AsyncTask<String, String, String> {

    private Activity mActivity;
    private ProgressDialog pDialog;


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
    protected String doInBackground(String... params) {
        return null;
    }

    @Override
    protected void onPostExecute(String message) {

        // dismiss the dialog once product deleted
        pDialog.dismiss();
    }


}
