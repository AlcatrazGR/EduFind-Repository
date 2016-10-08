package codebrains.edufind.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import codebrains.edufind.Fragments.DisplayProvidersBooks;
import codebrains.edufind.Fragments.InsertBookFragment;
import codebrains.edufind.Fragments.ProvidersProfileFragment;

/**
 * Tabs adapter class for the providers activity. This class handles the fragments of the
 * providers activity tab which are the login fragment and the create account fragment and initializes to them an
 * integer which represents each tab.
 */
public class ProviderTabsAdapter extends FragmentStatePagerAdapter {

    private int TOTAL_TABS = 3;

    public ProviderTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                return new ProvidersProfileFragment();

            case 1:
                return new InsertBookFragment();

            case 2:
                return new DisplayProvidersBooks();

        }

        return null;
    }

    @Override
    public int getCount() {
        return TOTAL_TABS;
    }
}
