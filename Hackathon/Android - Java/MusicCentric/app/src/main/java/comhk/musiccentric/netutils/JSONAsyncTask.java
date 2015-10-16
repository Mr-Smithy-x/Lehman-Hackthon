package comhk.musiccentric.netutils;

import android.os.AsyncTask;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Gene on 10/16/2015.
 */
public abstract class JSONAsyncTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String url = params[0];

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        try {
            Response response = client.newCall(request).execute();
            String body = response.body().string();

            return body;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public abstract void onJsonCallback(JSONObject obj);

    public abstract void onJsonError(String str);

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (s != null) {
            // convert to json
            try {
                JSONObject obj = new JSONObject(s);
                onJsonCallback(obj);

                return;
            } catch (JSONException e) {
                e.printStackTrace();
                onJsonError(e.getMessage());
            }
        }
    }
}
