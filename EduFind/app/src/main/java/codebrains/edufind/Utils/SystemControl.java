package codebrains.edufind.Utils;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.IOException;
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


}
