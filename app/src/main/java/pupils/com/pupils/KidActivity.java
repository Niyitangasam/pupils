package pupils.com.pupils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Models.Kid;
import Models.User;

import static android.content.ContentValues.TAG;

public class KidActivity extends AppCompatActivity {
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String userId;
    private EditText names;
    private EditText kidemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid);
        names=(EditText) findViewById(R.id.name);
        kidemail=(EditText) findViewById(R.id.kidemail);
        Button kidSave = (Button) findViewById(R.id.btn_save);
        kidSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseInstance = FirebaseDatabase.getInstance();
                mFirebaseDatabase = mFirebaseInstance.getReference("kids");
                createUser(names.toString(),kidemail.toString());
            Toast.makeText(getApplicationContext(),"Thanks, Kid Info Saved",Toast.LENGTH_LONG).show();
                finish();

            }
        });
    }

    private void createUser(String names, String kidemail) {

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        String email=mFirebaseUser.getEmail();

        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseUser.getUid();
        }

        Kid kid = new Kid(names,email,kidemail);

        mFirebaseDatabase.child(userId).setValue(kid);

        addUserChangeListener();
        Toast.makeText(getApplicationContext(),"kid saved", Toast.LENGTH_LONG).show();
    }
    private void addUserChangeListener() {
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Kid kid = dataSnapshot.getValue(Kid.class);
                if (kid == null) {
                    Log.e(TAG, "Kid data is null!");
                    return;
                }
                Log.e(TAG, "Kid data is changed!" + kid.email + ", " + kid.names+ ", " + kid.kidemail);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to read Kid");
            }
        });
    }
}
