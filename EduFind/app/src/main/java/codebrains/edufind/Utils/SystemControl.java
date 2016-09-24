package codebrains.edufind.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class that contains methods appropriate for the system such as checking if the device is connected
 * to internet, or if a remote server is reachable etc.
 */
public class SystemControl {

    private Context mContext;

    //Constructor
    public SystemControl(Context context) {
        this.mContext = context;
    }

    /**
     * Method that check if the device is connected to internet. It creates a connection with a known
     * URL and expects a response from it. If the time passes and there is no response then that means
     * that either the device is not connected to internet or the URL is unavailable (the website is
     * down). Thats why google.com is used which is not commonly offline or deal with problems.
     *
     * @return Returns a boolean variable which represents the status of the check.
     */
    public boolean HasActiveInternetConnection() {

        boolean check = true;
        if (IsNetworkAvailable()) {
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
        } else {
            check = false;
        }

        return check;
    }

    /**
     * Method that checks if the device has enabled the internet service by creating a network info
     * system object. If that one is null (no network info) then the device doesn't have enable the
     * internet.
     *
     * @return Returns a boolean which represents the status of the check.
     */
    private boolean IsNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(this.mContext.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

}
