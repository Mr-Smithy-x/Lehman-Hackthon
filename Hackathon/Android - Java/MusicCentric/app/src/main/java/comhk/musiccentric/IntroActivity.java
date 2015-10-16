package comhk.musiccentric;

import android.app.DownloadManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import comhk.musiccentric.adapters.VPagerAdapter;


public class IntroActivity extends AppCompatActivity {

    private VPagerAdapter mVPAdapter;
    private ViewPager mPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        mPager = (ViewPager) findViewById(R.id.intro_viewpager);
        mPager.setAdapter(mVPAdapter = new VPagerAdapter(getSupportFragmentManager()));
        mPager.setOffscreenPageLimit(5);
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("URL HERE").build();
            Response response = client.newCall(request).execute();
            String body = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static class IntroPage extends Fragment{

        private final String ICON = "";
        public static IntroPage instatiate(int icon, String sub){
            Bundle bundle = new Bundle();
            bundle.putInt("");
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.intro_fragment, container, false);

            return view;
        }
    }

}
