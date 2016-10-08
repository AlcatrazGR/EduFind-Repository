package codebrains.edufind.Controllers;

import android.app.Activity;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import codebrains.edufind.Models.AdminPanel;
import codebrains.edufind.Utils.MessageCenter;

/**
 * Control class that handles the process of calling all the appropriate methods to initialize the
 * expandable list in admins panel, and also making decisions on what info to display.
 */
public class AdminController {

    private List<String> listHeader;

    //Constructor
    public AdminController() {
        this.listHeader = new ArrayList<String>();
    }

    /**
     * Method that controls the flow of code and decides what to display to the user.
     * @param response The response from the web server.
     */
    public HashMap<String, List<String>> SetAdminPanelProcess(JSONObject response, Activity mActivity) {

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
                    if(ap.SetExpandableListData(response, mActivity)) {
                        HashMap<String, List<String>> listDataChild = ap.GetListData();
                        this.listHeader = ap.GetListHeader();
                        return listDataChild;
                    }
                    else {
                        MessageCenter msgCent = new MessageCenter(mActivity);
                        msgCent.FatalErrorDialogDisplay(mActivity, "Data Error",
                                "There was an error while trying to set the user account information " +
                                "to the list. Please try again by re logging into the system, or " +
                                "contact the support team.");
                    }
                break;

                //No error but no results
                case 2 :
                    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
                    List<String> items = new ArrayList<String>();
                    items.add("Currently there are no account creation requests!");
                    listDataChild.put("No pending requests found", items);
                    this.listHeader.add("No pending requests found");
                    return listDataChild;

            } //End of switch


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Method that returns the list header for the expandable list.
     * @return Returns the expandable list header.
     */
    public List<String> GetListHeader() {
        return this.listHeader;
    }

}
