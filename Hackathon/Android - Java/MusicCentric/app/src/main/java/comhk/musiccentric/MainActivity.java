package comhk.musiccentric;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import comhk.musiccentric.adapters.VPagerAdapter;
import comhk.musiccentric.fragments.FeedFragment;
import comhk.musiccentric.fragments.SearchUserFragment;
import comhk.musiccentric.models.Page;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    NavigationView mNav;
    CoordinatorLayout mCoords;
    DrawerLayout mDrawer;
    AppBarLayout mAppBar;
    ActionBarDrawerToggle mToggle;
    Toolbar mToolbar;
    FloatingActionButton mFab;
    TabLayout mTab;
    ViewPager mPager;
    VPagerAdapter mVPAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        attach();
    }

    android.support.v7.widget.SearchView searchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mPager.setCurrentItem(1,true);
                ((SearchUserFragment)mVPAdapter.getItem(1)).search(query);
                searchView.onActionViewCollapsed();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void attach() {
        setSupportActionBar(mToolbar);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.app_name, R.string.app_name);
        mDrawer.setDrawerListener(mToggle);
        mDrawer.post(new Runnable() {
            @Override
            public void run() {
                mToggle.syncState();
            }
        });
        mPager.setOffscreenPageLimit(3);
        mPager.setAdapter(mVPAdapter = new VPagerAdapter(getSupportFragmentManager()));
        mVPAdapter.append(Page.Builder().setTitle("Feed").setFragment(new FeedFragment()));
        mVPAdapter.append(Page.Builder().setTitle("Search").setFragment(new SearchUserFragment()));
        mTab.setupWithViewPager(mPager);
        mFab.setOnClickListener(this);
    }

    private void init(){
        this.mFab = (FloatingActionButton) findViewById(R.id.main_fab);
        this.mCoords = (CoordinatorLayout) findViewById(R.id.main_coords);
        this.mNav = (NavigationView) findViewById(R.id.main_nav);
        this.mAppBar = (AppBarLayout) findViewById(R.id.main_app_bar);
        this.mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        this.mTab = (TabLayout) findViewById(R.id.main_tab);
        this.mDrawer = (DrawerLayout) findViewById(R.id.main_drawer);
        this.mPager = (ViewPager) findViewById(R.id.main_pager);
        mNav.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, PostActivity.class));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_profile:
                    Intent i = new Intent(this, ProfileActivity.class);
                    startActivity(i);
                break;
        }
        return false;
    }
}
