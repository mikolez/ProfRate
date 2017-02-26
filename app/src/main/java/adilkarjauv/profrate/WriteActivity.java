package adilkarjauv.profrate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import io.techery.properratingbar.ProperRatingBar;

public class WriteActivity extends AppCompatActivity {

    private static final String TAG = "WriteActivity";

    static public Map<String, Professor> map = new HashMap<>();

    private Button sendButton;

    private EditText comment;

    private DatabaseReference mDatabase;

    private ArrayList<Professor> Profs;
    private ArrayList<String> ProfNames;

    private AutoCompleteTextView professorName;
    private ProperRatingBar properRatingBar;
    private int count;
    private int sumRating;
    private int checker = 0;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        FeedActivity.bottomNavigation.setCurrentItem(0);

        sendButton = (Button) findViewById(R.id.sendButton);

        comment = (EditText) findViewById(R.id.comment);
        professorName = (AutoCompleteTextView) findViewById(R.id.professorName);
        properRatingBar = (ProperRatingBar) findViewById(R.id.properRatingBar);
        properRatingBar.toggleClickable();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Profs = new ArrayList<>();
        ProfNames = new ArrayList<>();

        mDatabase.child("professors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    if (child.child("count").exists() == true) {
                        Professor a = new Professor(child.child("name").getValue().toString(), child.child("university").getValue().toString(), child.child("count").getValue().toString(), child.child("sumRating").getValue().toString(), child.child("imageUrl").getValue().toString());
                        Profs.add(a);
                        ProfNames.add(child.child("name").getValue().toString());
                    } else {
                        Professor a = new Professor(child.child("name").getValue().toString(), child.child("university").getValue().toString(), child.child("imageUrl").getValue().toString());
                        Profs.add(a);
                        ProfNames.add(child.child("name").getValue().toString());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, ProfNames);
        professorName.setThreshold(0);
        professorName.setAdapter(adapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendButton();
            }
        });
    }

    private void onSendButton() {
        if (professorName.getText().toString().length() == 0) {
            Toast.makeText(this, "Insert course's or professor's name", Toast.LENGTH_SHORT).show();
        } else if (comment.getText().toString().length() == 0) {
            Toast.makeText(this, "Insert comment", Toast.LENGTH_SHORT).show();
        } else if (ProfNames.contains(professorName.getText().toString()) == true && comment.getText().toString().length() > 0) {
            Toast toast = Toast.makeText(this, "Your comment is successfully sent!", Toast.LENGTH_SHORT);
            toast.show();
            String commentString = String.valueOf(comment.getText());

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                uid = user.getUid();
            }
            DatabaseReference a = mDatabase.child("comments").push();
            a.child("name").setValue(professorName.getText().toString());
            a.child("text").setValue(comment.getText().toString());
            a.child("rating").setValue(properRatingBar.getRating());
            a.child("user").setValue(uid);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            String currentDateandTime = sdf.format(new Date());
            a.child("time").setValue(currentDateandTime);

            mDatabase.child("professors").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        if (child.child("name").getValue().toString().equals(professorName.getText().toString()) && child.child("count").exists() == true) {
                            checker = 1;
                        }
                        if (child.child("name").getValue().toString().equals(professorName.getText().toString())) {
                            if (checker == 1) {
                                count = Integer.parseInt(child.child("count").getValue().toString());
                                sumRating = Integer.parseInt(child.child("sumRating").getValue().toString());
                                mDatabase.child("professors").child(professorName.getText().toString()).child("count").setValue(count + 1);
                                mDatabase.child("professors").child(professorName.getText().toString()).child("sumRating").setValue(sumRating + properRatingBar.getRating());
                            } else {
                                mDatabase.child("professors").child(professorName.getText().toString()).child("count").setValue(1);
                                mDatabase.child("professors").child(professorName.getText().toString()).child("sumRating").setValue(properRatingBar.getRating());
                            }
                            comment.setText("");
                            properRatingBar.setRating(0);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
