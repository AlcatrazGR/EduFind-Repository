package codebrains.edufind.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
     * Method that check if the device is connected to internet.
     * @return Returns a boolean variable which represents the status of the check.
     */
    public boolean HasActiveInternetConnection() {

        ConnectivityManager cm = (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    /**
     * Method that checks if the remote server is reachable by the application.
     * @return Returns a boolean determining the result of the check.
     */
    public boolean RemoteServerIsReachable() {

        String path = "edufind.comlu.com";

        try {

            InetAddress ipAddr = InetAddress.getByName(path);
            if (ipAddr.equals("")) { return false; }
            else { return true; }

        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Method that converts a UTF-8 encoded message into a human readable string.
     * @param encoded The encoded string.
     * @return Returns the readable string after the conversion.
     */
    public String ConvertUTF8EncodedStringToReadable(String encoded) {

        byte[] byteStream;
        String result = null;

        try {

            //Converts string to UTF-8 decoded byte stream.
            byteStream = encoded.toString().getBytes("UTF-8");
            result = new String(byteStream, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            Log.e("Exception!! : ", "UnsupportedEncodingException --> " + e.toString());
        }

        return result;
    }


}
