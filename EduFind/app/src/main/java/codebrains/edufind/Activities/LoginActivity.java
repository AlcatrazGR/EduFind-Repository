package codebrains.edufind.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import org.json.JSONException;
import org.json.JSONObject;
import codebrains.edufind.Adapters.MainTabsAdapter;
import codebrains.edufind.AsyncTasks.AsyncRetriveFeedTask;
import codebrains.edufind.Fragments.CreateAccountFragment;
import codebrains.edufind.Fragments.LoginFragment;
import codebrains.edufind.R;
import codebrains.edufind.Utils.MessageCenter;
import codebrains.edufind.Utils.SystemControl;

/**
 * Main activity of the application, its the first that is fired after boot.
 */
public class LoginActivity extends ActionBarActivity implements android.support.v7.app.ActionBar.TabListener {

    private ViewPager tabsviewPager;
    private MainTabsAdapter mTabsAdapter;
    private ActionBar actionBar;

    //Fragment objects
    private CreateAccountFragment caf;
    private LoginFragment lof;

    private JSONObject geoInfo;

    //Constructor
    public LoginActivity() {
        this.geoInfo = new JSONObject();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DeviceIsConnectedToInternet(); //Check if connected to internet.

        //Initialize fragment objects
        this.caf = new CreateAccountFragment();
        this.lof = new LoginFragment();

        //Initializing the tab view of this activity
        tabsviewPager = (ViewPager) findViewById(R.id.tabspager);
        mTabsAdapter = new MainTabsAdapter(getSupportFragmentManager());
        tabsviewPager.setAdapter(mTabsAdapter);
        actionBar = getSupportActionBar();

        this.getSupportActionBar().setHomeButtonEnabled(false);
        this.getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab loginTab = getSupportActionBar().newTab()
                .setText(" Login")
                .setTabListener(this);

        ActionBar.Tab createAccountTab = getSupportActionBar().newTab()
                .setText(" Create Account")
                .setTabListener(this);

        getSupportActionBar().addTab(loginTab);
        getSupportActionBar().addTab(createAccountTab);

        //Event listener that runs whenever the tabs are swiped, it is used to make enable the
        //respective tab.
        tabsviewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position); //Enable the respective tab.
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        new AsyncRetriveFeedTask(this).execute();


    }

    /**
     * Method that calls the appropriate code to check whether the device is connected to internet.
     */
    private void DeviceIsConnectedToInternet() {

        SystemControl sc = new SystemControl(this);
        if(!sc.HasActiveInternetConnection()) {
            MessageCenter msgCent = new MessageCenter(this);
            msgCent.NoInternetConnectionErrorDialog(this);
        }

    }

    /**
     * Method that calls the block of code to render the geolocation info of a certain user.
     * @param view The view object that called this event.
     */
    public void GetGeolocationOfUser(View view) {
        this.geoInfo = this.caf.GetGeolocationInfo(this);
    }

    /**
     * Event listener method that is fired whenever the submit button on the account creation form
     * is pressed.
     * @param view The view that called that fired the event.
     */
    public void CreateNewAccountProcess(View view) {
        this.caf.CreateAccount(this, this.geoInfo);
    }

    /**
     * Event on click listener for the login button in the login fragment.
     * @param view The view object that called this event.
     */
    public void LoginProcess(View view) {

        try {
            this.lof.LoginToSystem(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method that opens the activity of the student.
     * @param view The view of the activity that called this listener.
     */
    public void EnterAsAStudentProcess(View view) {

        SystemControl sc = new SystemControl(this);

        if(sc.HasActiveGPSService()) {
            Intent intent = new Intent(this, StudentActivity.class);
            this.finish();
            this.startActivity(intent);
        }
        else {
            MessageCenter msgCent = new MessageCenter(this);
            msgCent.GPSNotEnabledErrorDialog();
        }

    }

    /**
     * Event listener for the information button, which opens on the web browser the web page
     * of the application.
     * @param view The view of the activity that called this event.
     */
    public void OpenInformationWebPage(View view) {
        Uri uri = Uri.parse("http://edufind.hol.es");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        tabsviewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        tabsviewPager.setCurrentItem(tab.getPosition());
    }

}
