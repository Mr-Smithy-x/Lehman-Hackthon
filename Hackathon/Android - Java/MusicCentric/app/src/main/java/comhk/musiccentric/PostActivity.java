package comhk.musiccentric;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import comhk.musiccentric.models.Post;

public class PostActivity extends AppCompatActivity {

    private static final int TAKE_PHOTO_CODE = 0;
    private static final int ACTION_TAKE_VIDEO = 1;
    ImageView imgView, video;
    String f = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        imgView = (ImageView) findViewById(R.id.post_edit_up_img);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postPhoto();
            }
        });
        video = (ImageView) findViewById(R.id.post_edit_up_vid);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postVideo();
            }
        });
      /*  video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_rec = !is_rec;
                Intent i = new Intent(PostActivity.this, RecordVideoActivity.class);
                i.putExtra(RecordVideoActivity.FILE, f = new Date().toLocaleString().replace(":", "").replace("/", "").replace("-", "").replace(",", "").replace(" ", "") + ".3gp");
                startActivity(i);
            }
        });*/

    }

    String dir;
    String file_name;
    boolean is_rec = false;

    @Override
    protected void onResume() {
        super.onResume();
        if (is_rec == true) {
            Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
            is_rec = !is_rec;
            try {
                Toast.makeText(this, "hi2", Toast.LENGTH_SHORT).show();
                PostAction(Post.VIDEO, new File("/sdcard/"+f));
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void postVideo(){
        dir =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/MuseMe/";
        File newdir = new File(dir);
        newdir.mkdirs();
        file_name = "/"+ new Date().toLocaleString().replace(":", "").replace("/", "").replace("-", "").replace(",", "").replace(" ", "") + "videocapture.mp4";
        File newfile = new File(dir + file_name);
        try {
            newfile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newfile));
        takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 7);
        takeVideoIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 12);
        takeVideoIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        startActivityForResult(takeVideoIntent, ACTION_TAKE_VIDEO);

    }


    public void postPhoto() {
        dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/MuseMe/";
        File newdir = new File(dir);
        newdir.mkdirs();
        file_name = new Date().toLocaleString().replace(":", "").replace("/", "").replace("-", "").replace(",", "").replace(" ", "") + ".jpg";
        File newfile = new File(dir, file_name);
        try {
            newfile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri outputFileUri = Uri.fromFile(newfile);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
    }

    public void PostAction(int type, File parseFile) throws IOException {
        Toast.makeText(this, String.valueOf(parseFile.exists()), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, String.valueOf(parseFile.canRead()), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, parseFile.getName(), Toast.LENGTH_SHORT).show();

        FileInputStream fis = new FileInputStream(parseFile);
        final Post parseObject = (Post) Post.create(Post.class);
        parseObject.setName(ParseUser.getCurrentUser().getUsername());
        parseObject.setStatus(((AppCompatEditText) findViewById(R.id.post_edit_text)).getText().toString());
        parseObject.setType(type);
        parseObject.setName(ParseUser.getCurrentUser().getUsername());
        parseObject.setIcon("http://www.indianagrown.org/wp-content/uploads/2015/08/cow1.png");

        byte[] bytes = new byte[(int) parseFile.length()];
        fis.read(bytes, 0, (int) parseFile.length());
        fis.close();
        final ParseFile p = new ParseFile(file_name, bytes);
        p.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    parseObject.setFile(p);
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Toast.makeText(PostActivity.this, String.format("%s : %s", e.getCode(), e.getMessage()), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(PostActivity.this, "Posted", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });

                } else {
                    Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    ParseFile p;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            try {
                PostAction(Post.IMAGE, new File(dir + file_name));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (requestCode == ACTION_TAKE_VIDEO && resultCode == RESULT_OK) {
            try {
                PostAction(Post.VIDEO, new File(dir + file_name));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
