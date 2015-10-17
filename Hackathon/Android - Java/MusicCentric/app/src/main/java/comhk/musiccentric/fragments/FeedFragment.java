package comhk.musiccentric.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import comhk.musiccentric.R;
import comhk.musiccentric.VideoActivity;
import comhk.musiccentric.adapters.PostAdapter;
import comhk.musiccentric.models.Post;
import comhk.musiccentric.models.User;

/**
 * Created by charlton on 10/16/15.
 */
public class FeedFragment extends Fragment {

    RecyclerView mRecycler;
    PostAdapter mPAdapter;

    @Override
    public void onResume() {
        super.onResume();
        getInfo();
    }

    public void getInfo() {
        ParseQuery<Post> parseQuery = ParseQuery.getQuery(Post.class);
        parseQuery.findInBackground(new FindCallback<Post>() {

            @Override
            public void done(List<Post> list, ParseException e) {
                if (e != null) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return;
                }
                for (Post f : list) {
                    mPAdapter.appendItem(f);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_temp, container, false);
        mRecycler = (RecyclerView) view.findViewById(R.id.recycler_t);
        mRecycler.setLayoutManager(new GridLayoutManager(view.getContext(), 1));
        mRecycler.setAdapter(mPAdapter = new PostAdapter() {
            @Override
            public void OnClicked(View view, int position, Post post) {
                switch (post.getType()) {
                    case VIDEO:
                        Intent i = new Intent(getActivity(), VideoActivity.class);
                        i.putExtra("KEY", post.getFile().getUrl());
                        startActivity(i);
                    case IMAGE:
                        DFragment.instantiate(post).show(getFragmentManager(), "PIC");
                        break;
                }
            }
        });
        return view;
    }

    public void GrabContent() {

    }
}