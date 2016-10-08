package codebrains.edufind.Utils;

import android.app.Activity;
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
    private boolean status;

    //Constructor
    public MessageCenter(Context context) {
        this.mContext = context;
        this.status = false;
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

    /**
     * Method that displayes an error message to the user informing him that there is no internet
     * connection, by pressing the ok button the application is finished (cant continue further).
     * @param mActivity The activity that called this method.
     */
    public void NoInternetConnectionErrorDialog(final Activity mActivity) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("Internet Connection Error");

        // Setting Dialog Message
        alertDialog.setMessage("Your device is not connected to internet. Please enable your device's wifi" +
                " or your transfer data and reopen the application.");
        alertDialog.setCancelable(false);
        // On pressing Settings button
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mActivity.finish();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    /**
     * Method that displays a fatal error to the user and then closes the application.
     * @param mActivity The activity that called this method.
     * @param title The title of the dialog.
     * @param message The message of the dialog.
     */
    public void FatalErrorDialogDisplay(final Activity mActivity, String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        // On pressing Settings button
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mActivity.finish();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    /**
     * Method that displays an error message to the user without closing the application.
     * @param title The title of the dialog.
     * @param message The message of the dialog.
     */
    public void DisplayErrorDialog(String title, String message) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        // On pressing Settings button
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    /**
     * Method that displays a confirmation massage of `yes` or `no` to the user.
     * @param title The title of the dialog.
     * @param message The message of the dialog.
     */
    public void DisplayConfirmationDialog(String title, String message) {

        boolean status = false;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SetStatusFlag(true);
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SetStatusFlag(false);
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }

    /**
     * Method that returns the status flag which is used in many confirmation dialogs.
     * @return Returns true or false depending on the users selection.
     */
    public boolean GetStatusFlag() {
        return this.status;
    }

    /**
     * Method that returns the status flag which is used in many confirmation dialogs.
     * @param flag The boolean value to set the flag.
     */
    public void SetStatusFlag(boolean flag) {
        this.status = flag;
    }

}
