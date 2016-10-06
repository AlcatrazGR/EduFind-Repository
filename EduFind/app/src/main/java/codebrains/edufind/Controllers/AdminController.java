package codebrains.edufind.Controllers;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import codebrains.edufind.Models.AdminPanel;
import codebrains.edufind.Utils.MessageCenter;

/**
 * Control class that handles the process of calling all the appropriate methods to initialize the
 * expandable list in admins panel, and also making decisions on what info to display.
 */
public class AdminController {



    //Constructor
    public AdminController() {

    }

    /**
     * Method that controls the flow of code and decides what to display to the user.
     * @param response The response from the web server.
     */
    public void SetAdminPanelProcess(JSONObject response, Activity mActivity) {

        try {

            int statusCode = (int) response.get("status");
            switch (statusCode) {

                //Error occurred
                case 0 :
                    MessageCenter msgCenter = new MessageCenter(mActivity);
                    msgCenter.FatalErrorDialogDisplay(mActivity, "Server Error",
                            response.get("message").toString());
                break;

                //All data came successfully
                case 1 :
                    AdminPanel ap = new AdminPanel();
                    if(ap.SetExpandableListData(response)) {

                    }
                    else {
                        MessageCenter msgCenter = new MessageCenter(mActivity);
                        msgCenter.FatalErrorDialogDisplay(mActivity, "Data Error",
                                "There was an error while trying to set the user account information " +
                                "to the list. Please try again by re logging into the system, or " +
                                "contact the support team.");
                    }
                break;

                //No error but no results
                case 2 :

                break;

            } //End of switch


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method that calls the appropriate code for initializing the expandable list.
     */
    private void SetExpandableListItems() {

    }



}
