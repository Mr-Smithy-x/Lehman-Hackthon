package comhk.musiccentric;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
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
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import comhk.musiccentric.adapters.VPagerAdapter;
import comhk.musiccentric.callbacks.OnIntroBackListener;
import comhk.musiccentric.database.Global;
import comhk.musiccentric.models.Page;
import comhk.musiccentric.models.Post;
import comhk.musiccentric.models.User;


public class IntroActivity extends AppCompatActivity implements OnIntroBackListener {

    private VPagerAdapter mVPAdapter;
    private ViewPager mPager;
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK, "My wakelook");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

            Parse.enableLocalDatastore(this);
            ParseObject.registerSubclass(Post.class);
            Parse.initialize(this, "KEY HERE", "KEY HERE");

        mPager = (ViewPager) findViewById(R.id.intro_viewpager);
        mPager.setAdapter(mVPAdapter = new VPagerAdapter(getSupportFragmentManager()));
        mPager.setOffscreenPageLimit(5);
        mVPAdapter.append(Page.Builder().setFragment(new FinalPage()).setTitle("Hello"));
        wakeLock.acquire();

    }


    @Override
    public void finish() {

        wakeLock.release();
        super.finish();
    }

    public void saveUser(User user) {
        SharedPreferences sharedPreferences = getSharedPreferences("Centric", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user", user.getUser());
        editor.putString("pass", user.getPassword());
        editor.putString("email", user.getEmail());
        editor.commit();
        Global.user = user;
    }

    public User getUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("Centric", MODE_PRIVATE);
        return User.Build().setUser(sharedPreferences.getString("user", "")).setEmail(sharedPreferences.getString("email", "")).setPassword(sharedPreferences.getString("pass", ""));
    }

    @Override
    public void OnButtonClickedFeedBack(final View view, final User user, boolean visible) {
        if (view.getId() == R.id.final_frag_btn) {

            if (user.getUser().length() > 6 && user.getPassword().length() > 6) {

                ParseUser parseUser = new ParseUser();
                parseUser.setPassword(user.getPassword());
                parseUser.setUsername(user.getUser());

                if (visible && user.getEmail().length() > 6) {

                    parseUser.setEmail(user.getEmail());
                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            try {
                                if (e == null) {
                                    Toast.makeText(IntroActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    saveUser(user);
                                } else {
                                    Toast.makeText(IntroActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Toast.makeText(IntroActivity.this, "Error registering in", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                } else if (!visible) {

                    ParseUser.logInInBackground(user.getUser(), user.getPassword(), new LogInCallback() {
                        @Override
                        public void done(ParseUser use, ParseException e) {
                            try {
                                if (e == null) {
                                    Toast.makeText(IntroActivity.this, "Welcome " + use.getUsername(), Toast.LENGTH_SHORT).show();
                                    saveUser(user);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        startActivity(new Intent(IntroActivity.this, MainActivity.class), ActivityOptionsCompat.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight()).toBundle());
                                    } else {
                                        startActivity(new Intent(IntroActivity.this, MainActivity.class));
                                    }
                                    finish();
                                } else {
                                    Toast.makeText(IntroActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }

                            } catch (Exception ex) {
                                Toast.makeText(IntroActivity.this, "Error please retry", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {

                }
            }
        }
    }


    @SuppressLint("ValidFragment")
    public class FinalPage extends Fragment implements View.OnClickListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        private ImageView mImage;
        private AppCompatEditText mEmail;
        private AppCompatEditText mPassword;
        private AppCompatEditText mUsername;
        private AppCompatButton mRegister;
        private AppCompatTextView mTextView;
        public boolean visible = true;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.final_fragment, container, false);
            mEmail = (AppCompatEditText) view.findViewById(R.id.final_frag_email);
            mPassword = (AppCompatEditText) view.findViewById(R.id.final_frag_pass);
            mUsername = (AppCompatEditText) view.findViewById(R.id.final_frag_user);
            mRegister = (AppCompatButton) view.findViewById(R.id.final_frag_btn);
            mImage = (ImageView) view.findViewById(R.id.final_frag_img);
            mTextView = (AppCompatTextView) view.findViewById(R.id.final_frag_title);
            mUsername.setText(getUser().getUser());
            mEmail.setText(getUser().getEmail());
            mPassword.setText(getUser().getPassword());
            if (mUsername.length() > 0) {
                do_p();
            }
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    do_p();
                }
            });
            mRegister.setOnClickListener(this);
            return view;
        }

        private OnIntroBackListener onIntroBackListener;

        public void do_p() {
            visible = !visible;
            if (!visible) {
                mEmail.setVisibility(View.GONE);
                mTextView.setText("Signup Please");
            } else {
                mEmail.setVisibility(View.VISIBLE);
                mTextView.setText("I have an account");
            }
        }

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
                    .setPassword(mPassword.getText().toString()), visible);
        }
    }


}
