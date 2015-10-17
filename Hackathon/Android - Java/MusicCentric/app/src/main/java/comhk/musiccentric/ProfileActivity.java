package comhk.musiccentric;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import comhk.musiccentric.database.Global;
import comhk.musiccentric.models.User;

/**
 * Created by charlton on 10/17/15.
 */
public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_TAKE_GALLERY_VIDEO = 100;
    AppCompatButton commit;
    AppCompatEditText old, new1, new2;
    ImageView imageView;
    private String filemanagerstring;
    private String selectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        commit = (AppCompatButton) findViewById(R.id.pro_btn);
        old = (AppCompatEditText) findViewById(R.id.pro_pass_old);
        new1 = (AppCompatEditText) findViewById(R.id.pro_pass_confirm);
        new2 = (AppCompatEditText) findViewById(R.id.pro_pass_confirm_2);
        imageView = (ImageView) findViewById(R.id.pro_icon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        commit.setOnClickListener(this);

        Picasso.with(this).load(((ParseFile) ParseUser.getCurrentUser().get("icon")).getUrl()).into(imageView);
    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), REQUEST_TAKE_GALLERY_VIDEO);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                    String s = null;
                    Uri uri = data.getData();
                    File f = new File(s = getPath(uri));
                    Toast.makeText(this, uri.getPath(), Toast.LENGTH_SHORT).show();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        // Log.d(TAG, String.valueOf(bitmap));

                        Picasso.with(imageView.getContext()).load(uri).into(imageView);
                        try {
                            FileInputStream fis = new FileInputStream(f);
                            byte[] b = new byte[(int) f.length()];
                            fis.read(b, 0, (int) f.length());
                            fis.close();

                            final ParseFile p = new ParseFile(s.replace("/", "").replace("-", "").replace("_", ""), b);
                            p.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        ParseUser.getCurrentUser().put("icon", p);
                                        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    Toast.makeText(ProfileActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(ProfileActivity.this, "Could not upload picture" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }


    // UPDATED!
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

    @Override
    public void onClick(View v) {
        if (old.getText().toString().equals(Global.user.getPassword())) {
            if (new1.getText().toString().equals(new2.getText().toString())) {
                ParseUser.getCurrentUser().setPassword(new2.getText().toString());
                Toast.makeText(this, "Password Changed!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error, passwords are not the same.", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(this, "Old password Incorrect!", Toast.LENGTH_SHORT).show();
        }
    }
}
