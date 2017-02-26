package adilkarjauv.profrate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {

    private Button logOutButton;
    private ListView userProfileListView;
    private ArrayList<Comment> userProfileArray;
    private ArrayList<Integer> keyStorage;
    private String uid = "";
    private int key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        FeedActivity.bottomNavigation.setCurrentItem(0);

        userProfileArray = new ArrayList<>();
        keyStorage = new ArrayList<>();

        logOutButton = (Button) findViewById(R.id.logOutButton);
        userProfileListView = (ListView) findViewById(R.id.userProfileListView);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        }

        for (int i = 0; i < FeedActivity.commentsArray.size(); i++) {
            if (FeedActivity.commentsArray.get(i).getUser() != null ) {
                if (uid.equals(FeedActivity.commentsArray.get(i).getUser())) {
                    userProfileArray.add(FeedActivity.commentsArray.get(i));
                    keyStorage.add(i);
                }
            }
        }

        showComments();

        userProfileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                key = keyStorage.get(userProfileArray.size() - position - 1);
                onComment();
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogOut();
            }
        });
    }

    private void onComment() {
        Intent s = new Intent(this, CommentReadActivity.class);
        s.putExtra("key", key);
        startActivity(s);
    }
    private void showComments() {
        Feed_Adapter adapter = new Feed_Adapter(this, userProfileArray);
        userProfileListView.setAdapter(adapter);
    }

    private void onLogOut() {
        FirebaseAuth.getInstance().signOut();
        Intent launchNextActivity = new Intent(this, LogInActivity.class);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(launchNextActivity);
    }
}
