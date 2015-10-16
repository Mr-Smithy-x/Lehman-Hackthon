package comhk.musiccentric;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.parse.Parse;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "IbOUEiXtaeKBYQ4jM30rljvZUw7u7PjsX6YrJnIZ", "8SBb8LHMA8HG3HTqur13hFEN1A2gVNnYGEpFUYkY");
    }
}
