package codebrains.edufind.Activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import codebrains.edufind.Adapters.AdminExpandableListAdapter;
import codebrains.edufind.AsyncTasks.AsyncAcceptUsersRequest;
import codebrains.edufind.AsyncTasks.AsyncRetrieveNonVerifiedUsers;
import codebrains.edufind.Controllers.AdminController;
import codebrains.edufind.Interfaces.IAsyncResponse;
import codebrains.edufind.R;
import codebrains.edufind.Utils.MessageCenter;


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

        this.expListView = (ExpandableListView) findViewById(R.id.admin_expanded_menu);
        GetListData();
    }

    /**
     * Method that handles the call to server in order to retrieve the account info for the
     * initialization of the admin panel.
     */
    public void GetListData() {

        AsyncRetrieveNonVerifiedUsers arnvu = new AsyncRetrieveNonVerifiedUsers(this);
        arnvu.delegate = this;
        arnvu.execute();
    }

    /**
     * Method that handles the display process of user account info the the expandable list.
     * @param response The response from the web server.
     */
    private void DisplayUserAccountInfoToExpandableList(JSONObject response, Activity mActivity) {

        AdminController ac = new AdminController();
        this.listDataChild = ac.SetAdminPanelProcess(response, mActivity);

        if(listDataChild != null) {

            this.userAccInfo = response;
            this.listDataHeader = ac.GetListHeader();
            this.listAdapter = new AdminExpandableListAdapter(this, this.listDataHeader, this.listDataChild);
            this.expListView.setAdapter(this.listAdapter);
        }

    }

    /**
     * Event method that handles the accept of a user's request to create an account.
     * @param view The view of the activity that fired this event
     */
    public void AcceptUserRequest(View view) {

        LinearLayout l1 = (LinearLayout) view.getParent();
        RelativeLayout r1 = (RelativeLayout) l1.getParent();
        TextView usernameTv = (TextView) r1.findViewById(R.id.lblListHeader);
        String username = usernameTv.getText().toString().trim();

        Log.d("The username : ", username);

        MessageCenter msgCenter = new MessageCenter(this);
        msgCenter.DisplayConfirmationDialog("User Accept", "Do you really want to accept the `" +
            username + "` request ?");

        Log.d("---- check ---- : ", "HERE");

        if(msgCenter.GetStatusFlag()) {
            AsyncAcceptUsersRequest aur = new AsyncAcceptUsersRequest(username, this, this.userAccInfo);
            aur.delegate = this;
            aur.execute();
        }


    }

    /**
     * Event method that handles the deletion of a user's request to create an account.
     * @param view The view of the activity that fired this event
     */
    public void DeleteUserRequest(View view) {

        LinearLayout l1 = (LinearLayout) view.getParent();
        RelativeLayout r1 = (RelativeLayout) l1.getParent();
        TextView usernameTv = (TextView) r1.findViewById(R.id.lblListHeader);
        String username = usernameTv.getText().toString().trim();

        MessageCenter msgCenter = new MessageCenter(this);
        msgCenter.DisplayConfirmationDialog("User Deletion", "Do you really want to delete the `" +
                username + "` request ?");

        if(msgCenter.GetStatusFlag()) {

        }

    }

    @Override
    public void ProcessFinish(JSONObject output, Activity mActivity) {

        this.DisplayUserAccountInfoToExpandableList(output, mActivity);
    }



}
