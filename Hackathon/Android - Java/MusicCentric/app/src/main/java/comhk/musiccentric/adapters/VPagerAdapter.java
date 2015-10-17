package comhk.musiccentric.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import comhk.musiccentric.models.Page;

/**
 * Created by charlton on 10/16/15.
 */
public class VPagerAdapter extends FragmentStatePagerAdapter {

    List<Page> pageList = new ArrayList<>();

    public VPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void append(Page page){
        this.pageList.add(page);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return this.pageList.get(position).getFragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.pageList.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return this.pageList.size();
    }
}
