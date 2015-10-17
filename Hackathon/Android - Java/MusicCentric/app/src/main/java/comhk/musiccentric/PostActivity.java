package comhk.musiccentric;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import comhk.musiccentric.models.Post;

public class PostActivity extends AppCompatActivity {

    private static final int TAKE_PHOTO_CODE = 0;
    private static final int ACTION_TAKE_VIDEO = 1;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 3;
    private static final int REQUEST_TAKE_GALLERY_IMAGE = 4;


    ImageView imgView, video;
    ImageView fvid, fimg;
    String f = null;
    AppCompatButton appCompatButton;
    LinearLayout ll;
    VideoView v;

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
        fvid = (ImageView) findViewById(R.id.post_edit_up_fvid);
        fimg = (ImageView) findViewById(R.id.post_edit_up_fimg);
        ll = (LinearLayout) findViewById(R.id.temp);
        i = new ImageView(ll.getContext());
        v = new VideoView(ll.getContext());
        appCompatButton = (AppCompatButton) findViewById(R.id.post_user_btn);
        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (t >= 3) {

                        PostAction(t, pf);
                    } else {

                        PostAction(t, new File(dir + file_name));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        fvid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseVideo();
            }
        });

        fimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }

    public void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_TAKE_GALLERY_IMAGE);
    }


    int t = 2;
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
                PostAction(Post.VIDEO, new File("/sdcard/" + f));
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void postVideo() {
        dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/MuseMe/";
        File newdir = new File(dir);
        newdir.mkdirs();
        file_name = "/" + new Date().toLocaleString().replace(":", "").replace("/", "").replace("-", "").replace(",", "").replace(" ", "") + "videocapture.mp4";
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
            byte[] bytes = new byte[(int) parseFile.length()];

            if (type != 1) {
                FileInputStream fis = new FileInputStream(parseFile);
                fis.read(bytes, 0, (int) parseFile.length());
                fis.close();
            }

            final Post parseObject = (Post) Post.create(Post.class);
        parseObject.setName(ParseUser.getCurrentUser().getUsername());
        parseObject.setStatus(((AppCompatEditText) findViewById(R.id.post_edit_text)).getText().toString());
            parseObject.setType(type);
        parseObject.setName(ParseUser.getCurrentUser().getUsername());
        parseObject.setIcon(((ParseFile) ParseUser.getCurrentUser().get("icon")).getUrl());
            if (type != 1) {
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
                                        Toast.makeText(PostActivity.this, "failed to grab content", Toast.LENGTH_LONG).show();
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
            } else {
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Toast.makeText(PostActivity.this, "failed to grab content", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(PostActivity.this, "Posted", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        }


    public void PostAction(int type, final ParseFile parseFile) throws IOException {
        final Post parseObject = (Post) Post.create(Post.class);
        parseObject.setName(ParseUser.getCurrentUser().getUsername());
        parseObject.setStatus(((AppCompatEditText) findViewById(R.id.post_edit_text)).getText().toString());
        parseObject.setType((type == 3) ? (2) : (0));
        parseObject.setName(ParseUser.getCurrentUser().getUsername());
        parseObject.setIcon(((ParseFile) ParseUser.getCurrentUser().get("icon")).getUrl());
        parseFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    parseObject.setFile(parseFile);
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

    ImageView i;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
                ll.removeAllViews();
                t = Post.IMAGE;
                Picasso.with(this).load(new File(dir + file_name)).into(i);
                ll.addView(i);
            } else if (requestCode == ACTION_TAKE_VIDEO && resultCode == RESULT_OK) {
                ll.removeAllViews();
                t = Post.VIDEO;
                v.setVideoURI(Uri.fromFile(new File(dir + file_name)));
                v.setMediaController(new MediaController(this));
                ll.addView(v);
            }
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                    ll.removeAllViews();
                    t = 3;
                    ll.addView(v);
                    Uri uri = data.getData();

                    v.setVideoURI(uri);
                    String s = getPath(uri);
                    if (s == null) {
                        s = uri.getPath();
                    }
                    File f = new File(s);
                    Toast.makeText(this, uri.getPath(), Toast.LENGTH_SHORT).show();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        Picasso.with(i.getContext()).load(uri).into(i);
                        try {
                            FileInputStream fis = new FileInputStream(f);
                            byte[] b = new byte[(int) f.length()];
                            fis.read(b, 0, (int) f.length());
                            fis.close();
                            final ParseFile p = new ParseFile(s.replace("/", "").replace("-", "").replace("_", ""), b);
                            pf = p;

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                if (requestCode == REQUEST_TAKE_GALLERY_IMAGE) {
                    ll.removeAllViews();
                    t = 4;
                    ll.addView(i);
                    String s = null;
                    Uri uri = data.getData();
                    s = getPath(uri);
                    if (s == null) {
                        s = uri.getPath();
                    }
                    File f = new File(s);
                    Toast.makeText(this, uri.getPath(), Toast.LENGTH_SHORT).show();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        // Log.d(TAG, String.valueOf(bitmap));

                        Picasso.with(i.getContext()).load(uri).into(i);
                        try {
                            FileInputStream fis = new FileInputStream(f);
                            byte[] b = new byte[(int) f.length()];
                            fis.read(b, 0, (int) f.length());
                            fis.close();

                            ParseFile p = new ParseFile(s.replace("/", "").replace("-", "").replace("_", ""), b);
                            pf = p;

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }


    ParseFile pf;

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }
}
