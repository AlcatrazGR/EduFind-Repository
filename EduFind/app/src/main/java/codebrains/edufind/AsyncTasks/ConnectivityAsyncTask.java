package codebrains.edufind.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Vasilhs on 9/25/2016.
 */
public class ConnectivityAsyncTask extends AsyncTask<boolean, String, boolean>{

    @Override
    protected boolean doInBackground(boolean... params) {
        boolean check = true;
        try {
            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(1500);
            urlc.connect();
            //return (urlc.getResponseCode() == 200);
        } catch (IOException e) {
            Log.e("Error", "Error checking internet connection", e);
            check = false;
        }

        return check;
    }

}
