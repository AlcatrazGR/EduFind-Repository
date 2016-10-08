package codebrains.edufind.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import codebrains.edufind.Fragments.CreateAccountFragment;
import codebrains.edufind.Fragments.LoginFragment;

/**
 * Tabs adapter class for the main activity. This class handles the fragments of the main activity
 * tab which are the login fragment and the create account fragment and initializes to them an
 * integer which represents each tab.
 */
public class MainTabsAdapter extends FragmentStatePagerAdapter {

    private int TOTAL_TABS = 2;

    public MainTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                return new LoginFragment();

            case 1:
                return new CreateAccountFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return TOTAL_TABS;
    }
}
