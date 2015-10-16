package comhk.musiccentric;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import comhk.musiccentric.adapters.VPagerAdapter;
import comhk.musiccentric.callbacks.OnIntroBackListener;
import comhk.musiccentric.models.Page;
import comhk.musiccentric.models.User;


public class IntroActivity extends AppCompatActivity implements OnIntroBackListener {

    private VPagerAdapter mVPAdapter;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        mPager = (ViewPager) findViewById(R.id.intro_viewpager);
        mPager.setAdapter(mVPAdapter = new VPagerAdapter(getSupportFragmentManager()));
        mPager.setOffscreenPageLimit(5);
        mVPAdapter.append(Page.Builder().setFragment(IntroPage.instatiate(R.mipmap.ic_launcher, "Welcome")));
        mVPAdapter.append(Page.Builder().setFragment(IntroPage.instatiate(R.mipmap.ic_launcher, "How you doing")));
        mVPAdapter.append(Page.Builder().setFragment(new FinalPage()).setTitle("Hello"));
    }

    @Override
    public void OnButtonClicked(View view) {
        int position = mPager.getCurrentItem();
        if (position + 1 == mVPAdapter.getCount()) {
            //TODO: nothing
        } else {
            mPager.setCurrentItem(position + 1, true);
        }
    }

    @Override
    public void OnButtonClickedFeedBack(View view, User user) {
        if (view.getId() == R.id.final_frag_btn) {
            if(user.getUser().length() > 6 && user.getPassword().length() > 6 && user.getEmail().length() > 6){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(new Intent(this, MainActivity.class), ActivityOptionsCompat.makeScaleUpAnimation(view, 0,0,view.getWidth(),view.getHeight()).toBundle());
                }else{
                    startActivity(new Intent(this, MainActivity.class));
                }
                finish();
            }
            return;
        }
    }

    public static class IntroPage extends Fragment implements View.OnClickListener {

        private static final String ICON = "ICON", SUB = "SUB";

        public static IntroPage instatiate(int icon, String sub) {
            Bundle bundle = new Bundle();
            bundle.putInt(ICON, icon);
            bundle.putString(SUB, sub);
            IntroPage introPage = new IntroPage();
            introPage.setArguments(bundle);
            return introPage;
        }

        String sub;
        int icon;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                this.sub = getArguments().getString(SUB);
                this.icon = getArguments().getInt(ICON);
            }
        }

        private ImageView imageView;
        private AppCompatButton button;
        private AppCompatTextView textView;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.intro_fragment, container, false);
            imageView = (ImageView) view.findViewById(R.id.intro_frag_img);
            button = (AppCompatButton) view.findViewById(R.id.intro_frag_btn);
            textView = (AppCompatTextView) view.findViewById(R.id.intro_frag_title);
            textView.setText(sub);
            imageView.setImageResource(icon);
            button.setOnClickListener(this);
            return view;
        }

        public OnIntroBackListener introBackListener;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            introBackListener = (IntroActivity) context;
        }

        @Override
        public void onDetach() {
            super.onDetach();
            introBackListener = null;
        }

        @Override
        public void onClick(View v) {
            this.introBackListener.OnButtonClicked(v);
        }
    }

    public static class FinalPage extends Fragment implements View.OnClickListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }


        private ImageView mImage;
        private AppCompatEditText mEmail;
        private AppCompatEditText mPassword;
        private AppCompatEditText mUsername;
        private AppCompatButton mRegister;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.final_fragment, container, false);
            mEmail = (AppCompatEditText) view.findViewById(R.id.final_frag_email);
            mPassword = (AppCompatEditText) view.findViewById(R.id.final_frag_pass);
            mUsername = (AppCompatEditText) view.findViewById(R.id.final_frag_user);
            mRegister = (AppCompatButton) view.findViewById(R.id.final_frag_btn);
            mImage = (ImageView) view.findViewById(R.id.final_frag_img);
            mRegister.setOnClickListener(this);
            return view;
        }

        private OnIntroBackListener onIntroBackListener;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            onIntroBackListener = (IntroActivity) context;
        }

        @Override
        public void onDetach() {
            super.onDetach();
            onIntroBackListener = null;
        }

        @Override
        public void onClick(View v) {
            onIntroBackListener.OnButtonClickedFeedBack(v, User.Build()
                            .setUser(mUsername.getText().toString())
                            .setEmail(mEmail.getText().toString())
                            .setPassword(mPassword.getText().toString())
            );
        }
    }



}
