package codebrains.edufind.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import codebrains.edufind.Adapters.ProviderTabsAdapter;
import codebrains.edufind.R;

public class ProviderActivity extends ActionBarActivity implements android.support.v7.app.ActionBar.TabListener  {

    //Tab objects
    private ViewPager tabsviewPager;
    private ProviderTabsAdapter mTabsAdapter;
    private ActionBar actionBar;

    //Fragment objects

    private JSONObject userData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);

        Intent intent = getIntent();
        try {
            this.userData = new JSONObject(intent.getStringExtra("userdata"));
        } catch (JSONException e) {
            Log.e("Excepiton ! ->", "getStringExtra : " + e);
        }

        //Initializing the tab view of this activity
        tabsviewPager = (ViewPager) findViewById(R.id.tabspager);
        mTabsAdapter = new ProviderTabsAdapter(getSupportFragmentManager());
        tabsviewPager.setAdapter(mTabsAdapter);
        actionBar = getSupportActionBar();

        this.getSupportActionBar().setHomeButtonEnabled(false);
        this.getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab profileTab = getSupportActionBar().newTab()
                .setText(" Profile")
                .setTabListener(this);

        ActionBar.Tab addBookTab = getSupportActionBar().newTab()
                .setText(" Add")
                .setTabListener(this);

        ActionBar.Tab displayBookTab = getSupportActionBar().newTab()
                .setText(" Display")
                .setTabListener(this);

        getSupportActionBar().addTab(profileTab);
        getSupportActionBar().addTab(addBookTab);
        getSupportActionBar().addTab(displayBookTab);

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
