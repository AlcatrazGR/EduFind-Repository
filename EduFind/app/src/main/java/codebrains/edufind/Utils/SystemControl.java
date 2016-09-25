package codebrains.edufind.Utils;

import android.content.Context;
import android.net.ConnectivityManager;

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


}
