package comhk.musiccentric.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Created by charlton on 10/16/15.
 */

@ParseClassName("Feeds")
public class Post extends ParseObject implements Serializable {


    public static Post Builder() {
        return new Post();
    }

    // post type
    public static final int IMAGE = 0;
    public static final int AUDIO = 1;
    public static final int VIDEO = 2;


    public String getName() {
        return getString("Name");
    }
    public String getStatus() {
        return getString("Status");
    }


    public ParseFile getFile() {
        return getParseFile("File");
    }

    public int getType(){
        return  getInt("type");
    }

    public Post setName(String name) {
        put("Name", name);
        return this;
    }

    public Post setStatus(String status) {
        put("Status", status);
        return this;
    }

    public String getIcon(){
        return getString("icon");
    }

    public Post setIcon(String file){
        put("icon", file);
        return this;
    }


    public Post setFile(ParseFile file) {
        put("File", file);
        return this;
    }

    public Post setType(int type) {
        put("type", type);
        return this;


    }
}

