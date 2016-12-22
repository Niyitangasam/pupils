package pupils.com.pupils;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by miller on 12/21/16.
 */

public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //global configuration for package will goes here
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
