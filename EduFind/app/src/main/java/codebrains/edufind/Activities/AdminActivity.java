package codebrains.edufind.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import java.util.HashMap;
import java.util.List;
import codebrains.edufind.Adapters.AdminExpandableListAdapter;
import codebrains.edufind.R;


/**
 * Activity class for the admin panel.
 */
public class AdminActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        this.expListView = (ExpandableListView) findViewById(R.id.admin_expanded_menu);
        GetListData();

        this.listAdapter = new AdminExpandableListAdapter(this, this.listDataHeader, this.listDataChild);
        this.expListView.setAdapter(this.listAdapter);
    }

    /**
     * Method that handles the call to server in order to retrieve the account info for the
     * initialization of the admin panel.
     */
    private void GetListData() {


    }


}
