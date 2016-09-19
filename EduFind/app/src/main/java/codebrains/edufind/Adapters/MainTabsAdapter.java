package codebrains.edufind.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Vasilhs on 9/19/2016.
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

            break;
        }


        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
