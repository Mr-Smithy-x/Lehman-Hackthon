package comhk.musiccentric.adapters;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import comhk.musiccentric.R;
import comhk.musiccentric.models.Post;
import comhk.musiccentric.models.User;

/**
 * Created by Gene on 10/16/2015.
 */
public abstract class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    List<Post> posts = new ArrayList<Post>();
    public final int IMAGE = 0, AUDIO = 1, VIDEO = 2;


    @Override
    public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == IMAGE){
            return new PostHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_img_tmp,parent, false), viewType);
        }else if(viewType == AUDIO){
            return new PostHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_audio_tmp, parent, false), viewType);
        }else if(viewType == VIDEO){
            return new PostHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_video_tmp,parent,false), viewType);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return getItemAtPosition(position).getType();
    }

    @Override
    public void onBindViewHolder(PostHolder holder, int position) {
        Post post = getItemAtPosition(position);
        holder.title.setText(post.getName());
        holder.desc.setText(post.getStatus());
        holder.date.setText(post.getCreatedAt().toLocaleString());

                //post.getFile().


        Picasso.with(holder.user_icon.getContext()).load(post.getIcon()).into(holder.user_icon,new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });
        switch (getItemViewType(position)){
            case IMAGE:
                    Picasso.with(holder.webop.getContext()).load(post.getFile().getUrl()).into((ImageView) holder.webop, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
                break;
            case AUDIO:

                break;
            case VIDEO:
                ((VideoView)holder.webop).setVideoURI(Uri.parse(post.getFile().getUrl()));

                break;
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public Post getItemAtPosition(int childPosition) {
        return posts.get(childPosition);
    }

    public void appendItem(Post override) {
        if(posts.contains(override)) return;
            posts.add(0, override);
            notifyItemInserted(0);

    }

    public void clear() {
        int size = posts.size();
        posts.clear();
        notifyItemRangeRemoved(0, size);
    }

    public abstract void OnClicked(View view, int position, Post post);


    public class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View webop;
        AppCompatTextView title, date, desc;
        ImageView user_icon, fav_icon;
        int type;
        public PostHolder(View itemView, int type) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.type = type;
            user_icon = (ImageView) itemView.findViewById(R.id.feed_temp_image);
            fav_icon = (ImageView) itemView.findViewById(R.id.feed_temp_fav);
            title = (AppCompatTextView) itemView.findViewById(R.id.feed_temp_title);
            desc = (AppCompatTextView) itemView.findViewById(R.id.feed_temp_desc);
            date = (AppCompatTextView)itemView.findViewById(R.id.feed_temp_date);
            user_icon.setOnClickListener(this);
            switch (type) {
                case IMAGE:
                    webop = itemView.findViewById(R.id.feed_image_temp_weboption);
                    break;
                case AUDIO:
                    break;
                case VIDEO:
                    webop = itemView.findViewById(R.id.feed_video_temp_weboption);
                    break;
            }
        }

        public int getType(){
            return type;
        }

        @Override
        public void onClick(View v) {
            OnClicked(v, getAdapterPosition(), getItemAtPosition(getAdapterPosition()));
        }
    }

}
