package codebrains.edufind.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import codebrains.edufind.AsyncTasks.AsyncDeleteUsersRequest;
import codebrains.edufind.AsyncTasks.AsyncRetrieveNonVerifiedUsers;
import codebrains.edufind.Controllers.AdminController;
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

        this.DisplayConfirmationDialog("Do you really want to accept the `" +
                username + "` request ?", username, this, "Accept", this.userAccInfo);
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

        this.DisplayConfirmationDialog("Do you really want to delete the `" +
                username + "` request ?", username, this, "Delete", this.userAccInfo);

    }

    /**
     * Method that displays a confirmation massage of `yes` or `no` to the user.
     * @param message The message of the dialog.
     * @param username Username of the the user that requested the account creation.
     * @param aa Activity object.
     * @param process The process to follow, either deletion or acceptance of a user.
     * @param data The json object with all the users info.
     */
    private void DisplayConfirmationDialog(final String message, final String username, final AdminActivity aa,
                                           final String process, final JSONObject data) {

        boolean status = false;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirmation Message");

        // Setting Dialog Message
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                switch (process) {

                    case "Accept":
                        aa.CallAcceptAccountAsyncTask(username, data);
                        break;

                    case "Delete":
                        aa.CallDeleteAccountAsyncTask(username, data);
                        break;
                }
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // Showing Alert Message
        alertDialog.show();

    }

    /**
     * Method that calls the asynchronous task for accepting a users request.
     * @param username Username of the the user that requested the account creation.
     * @param data The json object with all the users info.
     */
    public void CallAcceptAccountAsyncTask(String username, JSONObject data) {
        AsyncAcceptUsersRequest aur = new AsyncAcceptUsersRequest(username, this, data);
        aur.delegate = this;
        aur.execute();
    }

    /**
     * Method that calls the asynchronous task for deleting a users request.
     * @param username Username of the the user that requested the account creation.
     * @param data The json object with all the users info.
     */
    public void CallDeleteAccountAsyncTask(String username, JSONObject data) {
        AsyncDeleteUsersRequest adur = new AsyncDeleteUsersRequest(username, this, data);
        adur.delegate = this;
        adur.execute();
    }

    @Override
    public void ProcessFinish(JSONObject output, Activity mActivity) {

        this.DisplayUserAccountInfoToExpandableList(output, mActivity);
    }



}
