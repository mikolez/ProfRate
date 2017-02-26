package adilkarjauv.profrate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private AutoCompleteTextView searchBar;
    private DatabaseReference mDatabase;
    private ArrayList<String> ProfNames;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        FeedActivity.bottomNavigation.setCurrentItem(0);

        searchBar = (AutoCompleteTextView) findViewById(R.id.searchBar);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        ProfNames = new ArrayList<>();

        mDatabase.child("professors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.child("count").exists() == true) {
                        Professor a = new Professor(child.child("name").getValue().toString(), child.child("university").getValue().toString(), child.child("count").getValue().toString(), child.child("sumRating").getValue().toString(), child.child("imageUrl").getValue().toString());
                        FeedActivity.Profs1.add(a);
                        WriteActivity.map.put(a.getName(), a);
                    } else {
                        Professor a = new Professor(child.child("name").getValue().toString(), child.child("university").getValue().toString(), child.child("imageUrl").getValue().toString());
                        FeedActivity.Profs1.add(a);
                        WriteActivity.map.put(a.getName(), a);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase.child("comments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FeedActivity.commentsArray.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.child("user").exists() == false) {
                        FeedActivity.commentsArray.add(new Comment(child.getKey(), child.child("name").getValue().toString(), child.child("text").getValue().toString(), child.child("time").getValue().toString(), child.child("rating").getValue().toString()));
                    } else {
                        FeedActivity.commentsArray.add(new Comment(child.getKey(), child.child("name").getValue().toString(), child.child("text").getValue().toString(), child.child("time").getValue().toString(), child.child("rating").getValue().toString(), child.child("user").getValue().toString()));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase.child("professors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    ProfNames.add(child.child("name").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, ProfNames);
        searchBar.setThreshold(0);
        searchBar.setAdapter(adapter);
        searchBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                name = String.valueOf(searchBar.getText());
                onSearchBar();
            }
        });
    }

    private void onSearchBar() {
        Intent i = new Intent(this, ProfessorProfileActivity.class);
        i.putExtra("name", name);
        startActivity(i);
    }
}
