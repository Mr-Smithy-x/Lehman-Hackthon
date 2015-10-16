package comhk.musiccentric.views;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import comhk.musiccentric.R;
import comhk.musiccentric.models.User;

/**
 * Created by Gene on 10/16/2015.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    List<Post> posts = new ArrayList<Post>();
    //    private OnPDFRecyclerListener onPDFRecyclerListener;
    Context context;
    LayoutInflater inflater;
    public PostAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostHolder(inflater.inflate(R.layout.post_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(PostHolder holder, int position) {
        Post post = posts.get(position);

        holder.title.setText(String.format("%s",post.getFile().toUpperCase().replace(".PDF","")));
        holder.user.setText(String.format());
        holder.description.setText(String.format());

        TextDrawable td = TextDrawable.builder().buildRound(String.valueOf(post.getFile().charAt(0)).toUpperCase(), context.getResources().getColor(R.color.md_blue_500));

        if (post.type == Post.IMAGE) {
            holder.imageView.setImageDrawable(td);
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
        if(!posts.contains(override)) {
            posts.add(override);
            notifyItemInserted(posts.size());
        }
    }

    public void clear() {
        int size = posts.size();
        posts.clear();
        notifyItemRangeRemoved(0, size);
    }

    public class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        AppCompatTextView title, user, description;
        public PostHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.post_content);
            title = (AppCompatTextView) itemView.findViewById(R.id.post_title);
            user = (AppCompatTextView) itemView.findViewById(R.id.post_user);
            description = (AppCompatTextView) itemView.findViewById(R.id.post_description);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public class Post {
        User user;
        String description;
        String content;
        int type;

        public Post(User user, String description, String content, int type) {
            this.user = user;
            this.description = description;
            this.content = content;
            this.type = type;
        }

        // post type
        public static final int IMAGE = 0;
        public static final int AUDIO = 1;
        public static final int VIDEO = 2;
    }

}
