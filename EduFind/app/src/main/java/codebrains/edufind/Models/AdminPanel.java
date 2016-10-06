package codebrains.edufind.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class that handles the setting of data into the expandable list items and groups in order to
 * be displayed to the admins panel.
 */
public class AdminPanel {

    private HashMap<String, List<String>> listDataChild;

    //Constructor
    public AdminPanel() {
        this.listDataChild = new HashMap<String, List<String>>();
    }

    /**
     * Method that processes the raw json response of the server and sets the expandable list
     * on the admins panel.
     * @param response The JSON response from the server that contains the account info.
     * @return Returns a boolean to represent result of the process.
     */
    public boolean SetExpandableListData(JSONObject response) {

        try {

            JSONArray usersJSONArray = (JSONArray) response.get("accounts");
            for(int i = 0; i <= usersJSONArray.length(); i++) {

                JSONObject account = usersJSONArray.getJSONObject(i);
                List<String> items = new ArrayList<String>();
                items.add(account.get("username").toString());
                items.add(account.get("mail").toString());
                items.add(account.get("city").toString());
                items.add(account.get("address").toString());

                listDataChild.put(account.get("provider").toString(), items);
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



}
