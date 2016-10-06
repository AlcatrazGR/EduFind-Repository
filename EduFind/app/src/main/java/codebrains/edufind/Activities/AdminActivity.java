package codebrains.edufind.Activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import codebrains.edufind.Adapters.AdminExpandableListAdapter;
import codebrains.edufind.AsyncTasks.AsyncRetrieveNonVerifiedUsers;
import codebrains.edufind.Interfaces.IAsyncResponse;
import codebrains.edufind.R;


/**
 * Activity class for the admin panel.
 */
public class AdminActivity extends AppCompatActivity implements IAsyncResponse {

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    private JSONObject userAccInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //this.expListView = (ExpandableListView) findViewById(R.id.admin_expanded_menu);
        GetListData();

        //this.listAdapter = new AdminExpandableListAdapter(this, this.listDataHeader, this.listDataChild);
        //this.expListView.setAdapter(this.listAdapter);
    }

    /**
     * Method that handles the call to server in order to retrieve the account info for the
     * initialization of the admin panel.
     */
    private void GetListData() {

        AsyncRetrieveNonVerifiedUsers arnvu = new AsyncRetrieveNonVerifiedUsers(this);
        arnvu.delegate = this;
        arnvu.execute();


    }

    /**
     * Method that handles the display process of user account info the the expandable list.
     * @param response The response from the web server.
     */
    private void DisplayUserAccountInfoToExpandableList(JSONObject response) {


    }

    @Override
    public void ProcessFinish(JSONObject output, Activity mActivity) {

        Log.d("Server To Interface : ", String.valueOf(output));
        this.DisplayUserAccountInfoToExpandableList(output);
    }


}
