package comhk.musiccentric;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import comhk.musiccentric.models.Post;

/**
 * Created by charlton on 10/17/15.
 */
public class VideoActivity extends AppCompatActivity {

    VideoView videoView;
    String p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        if(getIntent() != null){
            p =  getIntent().getStringExtra("KEY");
            System.out.print(p);
            Toast.makeText(this,p,Toast.LENGTH_SHORT).show();
            videoView = (VideoView) findViewById(R.id.video_video);
            videoView.setMediaController(new MediaController(this));
            videoView.setVideoURI(Uri.parse(p));
            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {

                    return false;
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.seekTo(0);
                    mp.start();
                }
            });

        }

    }
}
