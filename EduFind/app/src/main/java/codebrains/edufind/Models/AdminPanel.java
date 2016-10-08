package codebrains.edufind.Models;

import android.app.Activity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import codebrains.edufind.Utils.SystemControl;

/**
 * Class that handles the setting of data into the expandable list items and groups in order to
 * be displayed to the admins panel.
 */
public class AdminPanel {

    private HashMap<String, List<String>> listDataChild;
    private List<String> listHeader;

    //Constructor
    public AdminPanel() {
        this.listDataChild = new HashMap<String, List<String>>();
        this.listHeader = new ArrayList<String>();
    }

    /**
     * Method that processes the raw json response of the server and sets the expandable list
     * on the admins panel.
     * @param response The JSON response from the server that contains the account info.
     * @return Returns a boolean to represent result of the process.
     */
    public boolean SetExpandableListData(JSONObject response, Activity mActivity) {

        try {

            SystemControl sc = new SystemControl(mActivity);
            JSONArray usersJSONArray = (JSONArray) response.get("accounts");

            for(int i = 0; i < usersJSONArray.length(); i++) {

                JSONObject account = usersJSONArray.getJSONObject(i);
                List<String> items = new ArrayList<String>();
                items.add("Provider: " + sc.ConvertUTF8EncodedStringToReadable(account.get("provider").toString()));
                items.add("Email: " + account.get("mail").toString());
                items.add("City: " + account.get("city").toString());
                items.add("Address: " + account.get("address").toString());

                this.listHeader.add(sc.ConvertUTF8EncodedStringToReadable(account.get("username").toString()));
                this.listDataChild.put(sc.ConvertUTF8EncodedStringToReadable(account.get("username").toString()), items);
            }

        } catch (JSONException e) {
            return false;
        }

        return true;
    }

    /**
     * Method that returns the private list data that has been previously setted.
     * @return Returns the list data.
     */
    public HashMap<String, List<String>> GetListData() {
        return this.listDataChild;
    }

    /**
     * Method that returns the list of headers that will be used on the expandable list.
     * @return Returns the private header list.
     */
    public List<String> GetListHeader() {
        return this.listHeader;
    }

}
