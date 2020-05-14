package marko.mitrovic.singidroid4;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import marko.mitrovic.singidroid4.fragments.news_tabs.newsTab;

public class tabsPageAdapter extends FragmentStatePagerAdapter {

    @StringRes

    private final Context mContext;
    private String[] tabArray = new String[]{"Singidunum", "Tehnicki", "FIR"};

    public tabsPageAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return newsTab.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabArray[position];
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}
