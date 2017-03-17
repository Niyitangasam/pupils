package pupils.com.pupils;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Models.Kid;
import Models.User;


public class ListKidsActivity extends ListActivity {
    private TextView text;
    private List<String> listValues;
    private ArrayAdapter<String> myAdapter;
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mFirebaseDB;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String email,latitude,longitude,emailneeded;
    private String selectedItem;
    private String TAG="kid";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kids);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("kids");
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        email=mFirebaseUser.getEmail();

        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listValues = new ArrayList<String>();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    if(child.getValue(Kid.class).getEmail().equals(email)) {
                        listValues.add(child.getValue(Kid.class).getNames());
                    }
                }

                text = (TextView) findViewById(R.id.mainText);
                myAdapter = new ArrayAdapter <String>(getApplicationContext(), R.layout.row_layout, R.id.listText, listValues);
                setListAdapter(myAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
             Log.d(TAG,"Error occured "+databaseError.getCode());
            }
        });





    }

    @Override

    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);

         selectedItem = (String) getListView().getItemAtPosition(position);
        //String selectedItem = (String) getListAdapter().getItem(position);
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    if(child.getValue(Kid.class).getNames().equals(selectedItem)) {
                        emailneeded=child.getValue(Kid.class).getKidemail();
                        Log.d(TAG,"email "+emailneeded);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDB = mFirebaseInstance.getReference("users");
        mFirebaseDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot adress : dataSnapshot.getChildren()) {
                    if(adress.getValue(User.class).getEmail().equals(emailneeded)) {
                        latitude=adress.getValue(User.class).getLatitude();
                        longitude=adress.getValue(User.class).getLongitude();
                        //Toast.makeText(getApplicationContext(),"Latitude is "+latitude+"Longitude is "+longitude, Toast.LENGTH_LONG).show();
                    }

                }
                if(latitude!=null && !latitude.isEmpty()) {
                    Intent intent = new Intent(ListKidsActivity.this, MapsActivity.class);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    finish();
                    startActivity(intent);
                }
                else{

                    Toast.makeText(getApplicationContext(),"Unable to get your Kid's  Location", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        text.setText("You clicked " + selectedItem + " at position " + position);
    }
}
