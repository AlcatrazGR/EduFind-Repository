package codebrains.edufind.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

/**
 * Class that contains methods for displaying messages to the user. The messages can be either error
 * messages, information messages or even settings messages.
 */
public class MessageCenter {

    private Context mContext;

    //Constructor
    public MessageCenter(Context context) {
        this.mContext = context;
    }

    /**
     * Error method that is displayed to the user when the GPS is not enabled and can redirect to
     * the device settings.
     * */
    public void GPSNotEnabledErrorDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS Settings");

        // Setting Dialog Message
        alertDialog.setMessage("Your GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


}
