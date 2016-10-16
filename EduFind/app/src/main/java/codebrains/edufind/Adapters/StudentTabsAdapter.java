package codebrains.edufind.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import codebrains.edufind.Fragments.BookSearchFragment;
import codebrains.edufind.Fragments.MapSearchFragment;


/**
 * Adapter class that binds all the fragment that are displayed on the students activity.
 */
public class StudentTabsAdapter extends FragmentStatePagerAdapter {

    private int TOTAL_TABS = 2;

    public StudentTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                return new BookSearchFragment();

            case 1:
                return new MapSearchFragment();

        }

        return null;
    }

    @Override
    public int getCount() {
        return TOTAL_TABS;
    }

}
