package codebrains.edufind.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import org.json.JSONException;
import org.json.JSONObject;
import codebrains.edufind.Adapters.StudentTabsAdapter;
import codebrains.edufind.AsyncTasks.AsyncBookSearch;
import codebrains.edufind.Controllers.CreateAccountController;
import codebrains.edufind.Fragments.BookSearchFragment;
import codebrains.edufind.Fragments.MapSearchFragment;
import codebrains.edufind.Interfaces.IAsyncResponse;
import codebrains.edufind.R;

public class StudentActivity extends ActionBarActivity implements android.support.v7.app.ActionBar.TabListener,
        IAsyncResponse{

    private ViewPager tabsviewPager;
    private StudentTabsAdapter mTabsAdapter;
    private ActionBar actionBar;

    private String sortingValue;
    private int sortingMethod;
    /* 0 -> by area
     * 1 -> by sector
     * 2 -> by publisher
     * 3 -> by title
     */

    //Fragments
    public BookSearchFragment bsf;
    public MapSearchFragment msf;

    private static JSONObject studentGeo;
    private static JSONObject sortedList; //It can either be 1 or 2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        //Setting new geolocation info for the student.
        CreateAccountController cac = new CreateAccountController();
        studentGeo = cac.HandleGeoLocationInfo(this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("process", 3);
            jsonObject.put("shCode", 0);
        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException : " + e);
        }
        this.CallBookSearchAsyncTask(jsonObject, true);

        //Initializing the tab view of this activity
        tabsviewPager = (ViewPager) findViewById(R.id.tabspager);
        mTabsAdapter = new StudentTabsAdapter(getSupportFragmentManager());
        tabsviewPager.setAdapter(mTabsAdapter);
        actionBar = getSupportActionBar();

        this.getSupportActionBar().setHomeButtonEnabled(false);
        this.getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab searchTab = getSupportActionBar().newTab()
                .setText(" Search")
                .setTabListener(this);

        ActionBar.Tab mapTab = getSupportActionBar().newTab()
                .setText(" Map")
                .setTabListener(this);

        getSupportActionBar().addTab(searchTab);
        getSupportActionBar().addTab(mapTab);

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

        this.sortingMethod = 0;
        try {
            this.sortingValue = this.studentGeo.get("city").toString();
        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException : OnCreate->" + e);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            this.sortingValue = data.getStringExtra("sortingValue");
            String sortingKey = data.getStringExtra("sortingKey");
            try {
                this.sortingMethod = Integer.parseInt(data.getStringExtra("sortingMethod"));
            }
            catch(NumberFormatException ex) {
                Log.e("Exception! ->", "NumberFormatException : onActivityResult ->"+ex);
                this.sortingMethod = 0;
            }

            //Build json for service call.
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("process", 3);
                jsonObject.put("shCode", this.sortingMethod);
                jsonObject.put(sortingKey, this.sortingValue);
            } catch (JSONException e) {
                Log.e("Excepiton ! ->", "JSONException : onActivityResult->" + e);
            }
            this.CallBookSearchAsyncTask(jsonObject, false);

        }

    }

    /**
     * Method that opens the activity search of books.
     * @param view The view of the activity that called this method.
     */
    public void BookSortingProcess(View view) {
        Intent sortingIntent = new Intent(this, SortingActivity.class);
        startActivityForResult(sortingIntent, 2);
    }

    /**
     * Method that calls the asynchronous task that sets the new sorted book data.
     * @param data A json object containing the necessary data and sorting codes to determine the process to follow.
     */
    private void CallBookSearchAsyncTask(JSONObject data, boolean citySortFlag) {
        AsyncBookSearch abs = new AsyncBookSearch(this, data, citySortFlag);
        abs.delegate = this;
        abs.execute();
    }












    /**
     * Method that sets the static book list json object.
     * @param newList The new json sorted book list that will replace the old one.
     */
    public static void SetSortedBookList(JSONObject newList) {
        sortedList = newList;
    }

    /**
     * Method that gets the static json book list object.
     * @return Returns a json tha contains all the info for the sorted book list.
     */
    public static JSONObject GetSortedBookList() {
        return sortedList;
    }

    /**
     * Method that sets the static student geolocation info JSON Object
     * @param newGeo The new geo json that will replace the current.
     */
    public static void SetStudentGeolocationInfo(JSONObject newGeo) {
        studentGeo = newGeo;
    }

    /**
     * Method that retrieves the static student's geolocation info
     * @return Returns the student geolocation information.
     */
    public static JSONObject GetStudentGeolocationInfo() {
        return studentGeo;
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

    @Override
    public void ProcessFinish(JSONObject output, Activity mActivity) {

        SetSortedBookList(output); //sets the new sorted book list.

        if(this.bsf == null || this.msf == null) {

            Log.d("-- Phase --", "Initializing Fragments ");
            //Fragment initialization
            this.bsf = new BookSearchFragment();
            this.msf = new MapSearchFragment();
        }

        this.bsf.SetExpandableListContent(mActivity);

    }


}

