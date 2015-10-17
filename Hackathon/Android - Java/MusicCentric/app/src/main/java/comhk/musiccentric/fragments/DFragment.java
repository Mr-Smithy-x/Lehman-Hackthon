package comhk.musiccentric.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import comhk.musiccentric.R;
import comhk.musiccentric.models.Post;

/**
 * Created by charlton on 10/17/15.
 */
public class DFragment extends AppCompatDialogFragment {


    public static DFragment instantiate(Post p){
        DFragment f = new DFragment();;
        Bundle b = new Bundle();
        b.putSerializable("KEY", p);
        f.setArguments(b);
        return f;
    }

    Post p;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            p = (Post) getArguments().getSerializable("KEY");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.di_img, container, false);
        ImageView img = (ImageView) view.findViewById(R.id.di_img_t);

        if(p != null){
            Picasso.with(view.getContext()).load(p.getFile().getUrl()).into(img, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
        }
        return view;
    }


}
