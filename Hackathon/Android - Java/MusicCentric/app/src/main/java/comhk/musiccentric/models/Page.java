package comhk.musiccentric.models;

import android.support.v4.app.Fragment;

/**
 * Created by charlton on 10/16/15.
 */
public class Page {
    private Fragment fragment;
    private String title;
    private int icon;

    public static Page Builder(){
        return new Page();
    }

    public Fragment getFragment() {
        return fragment;
    }

    public Page setFragment(Fragment fragment) {
        this.fragment = fragment;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Page setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getIcon() {
        return icon;
    }

    public Page setIcon(int icon) {
        this.icon = icon;
        return this;
    }
}
