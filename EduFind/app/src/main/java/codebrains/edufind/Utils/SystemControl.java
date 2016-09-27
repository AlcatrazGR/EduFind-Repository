package codebrains.edufind.Utils;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.IOException;
import java.net.InetAddress;

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


    public boolean RemoteServerIsReachable(String path) {

        try {
            if(!InetAddress.getByName(path).isReachable(2000))
                return false;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


}
