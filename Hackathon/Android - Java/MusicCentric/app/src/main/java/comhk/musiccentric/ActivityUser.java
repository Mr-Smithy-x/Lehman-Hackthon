package comhk.musiccentric;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by charlton on 10/17/15.
 */
public class ActivityUser extends AppCompatActivity {

    ImageView img;
    CollapsingToolbarLayout col;
    String title, link, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
        img = (ImageView) findViewById(R.id.prof_img);
        col = (CollapsingToolbarLayout) findViewById(R.id.prof_coll);
        setSupportActionBar((Toolbar) findViewById(R.id.prof_tool));
        if (getIntent() != null) {
            title = getIntent().getStringExtra("title");
            link = getIntent().getStringExtra("link");
            date = getIntent().getStringExtra("date");
        }
        ((TextView) findViewById(R.id.prof_join)).setText(date);
        col.setTitleEnabled(true);
        col.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        col.setExpandedTitleColor(getResources().getColor(android.R.color.white));
        Picasso.with(this).load(link).into(img, new Callback() {
            @Override
            public void onSuccess() {
                Palette p = Palette.from(((BitmapDrawable) img.getDrawable()).getBitmap()).generate();
                col.setContentScrimColor(p.getMutedColor(getResources().getColor(R.color.colorPrimary)));
                col.setStatusBarScrimColor(p.getDarkMutedColor(getResources().getColor(R.color.colorPrimary)));
            }

            @Override
            public void onError() {

            }
        });
    }

}
