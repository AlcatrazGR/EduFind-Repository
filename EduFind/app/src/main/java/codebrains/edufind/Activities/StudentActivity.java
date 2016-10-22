package codebrains.edufind.Activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import codebrains.edufind.Adapters.StudentTabsAdapter;
import codebrains.edufind.Fragments.BookSearchFragment;
import codebrains.edufind.R;

public class StudentActivity extends ActionBarActivity implements android.support.v7.app.ActionBar.TabListener {

    private ViewPager tabsviewPager;
    private StudentTabsAdapter mTabsAdapter;
    private ActionBar actionBar;

    //Fragments
    private BookSearchFragment bsf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

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

        //Fragment initialization
        this.bsf = new BookSearchFragment();

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



        /*
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("process", 3);
            jsonObject.put("shCode", 0);
            jsonObject.put("city", "");
        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "JSONException : " + e);
        }

        this.CallBookSearchAsyncTask(jsonObject);
        */
    }

    private void CallBookSearchAsyncTask(JSONObject data) {



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

