package adilkarjauv.profrate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.baoyz.widget.PullRefreshLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    private static final String TAG = "FeedActivity";
    public static AHBottomNavigation bottomNavigation;
    public static ArrayList<Comment> commentsArray;
    private DatabaseReference mDatabase;
    private ListView listOfComments;
    public static ArrayList<Professor> Profs1;
    private int key;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        final PullRefreshLayout layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        layout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        layout.setColorSchemeColors(Color.GRAY);
        layout.setRefreshing(true);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        }

        commentsArray = new ArrayList<>();
        Profs1 = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        listOfComments = (ListView) findViewById(R.id.listofComments);

        mDatabase.child("professors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.child("count").exists() == true) {
                        Professor a = new Professor(child.child("name").getValue().toString(), child.child("university").getValue().toString(), child.child("count").getValue().toString(), child.child("sumRating").getValue().toString(), child.child("imageUrl").getValue().toString());
                        Profs1.add(a);
                        WriteActivity.map.put(a.getName(), a);
                        showComments();
                    } else {
                        Professor a = new Professor(child.child("name").getValue().toString(), child.child("university").getValue().toString(), child.child("imageUrl").getValue().toString());
                        Profs1.add(a);
                        WriteActivity.map.put(a.getName(), a);
                        showComments();
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
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.child("user").exists() == false) {
                        commentsArray.add(new Comment(child.getKey(), child.child("name").getValue().toString(), child.child("text").getValue().toString(), child.child("time").getValue().toString(), child.child("rating").getValue().toString()));
                    } else {
                        commentsArray.add(new Comment(child.getKey(), child.child("name").getValue().toString(), child.child("text").getValue().toString(), child.child("time").getValue().toString(), child.child("rating").getValue().toString(), child.child("user").getValue().toString()));
                    }
                    listOfComments.invalidateViews();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
                layout.setRefreshing(false);

                layout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
                layout.setColorSchemeColors(Color.GRAY);
                layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        mDatabase.child("professors").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    if (child.child("count").exists() == true) {
                                        Professor a = new Professor(child.child("name").getValue().toString(), child.child("university").getValue().toString(), child.child("count").getValue().toString(), child.child("sumRating").getValue().toString(), child.child("imageUrl").getValue().toString());
                                        Profs1.add(a);
                                        WriteActivity.map.put(a.getName(), a);
                                        showComments();
                                    } else {
                                        Professor a = new Professor(child.child("name").getValue().toString(), child.child("university").getValue().toString(), child.child("imageUrl").getValue().toString());
                                        Profs1.add(a);
                                        WriteActivity.map.put(a.getName(), a);
                                        showComments();
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
                                commentsArray.clear();
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    if (child.child("user").exists() == false) {
                                        commentsArray.add(new Comment(child.getKey(), child.child("name").getValue().toString(), child.child("text").getValue().toString(), child.child("time").getValue().toString(), child.child("rating").getValue().toString()));
                                    } else {
                                        commentsArray.add(new Comment(child.getKey(), child.child("name").getValue().toString(), child.child("text").getValue().toString(), child.child("time").getValue().toString(), child.child("rating").getValue().toString(), child.child("user").getValue().toString()));
                                    }
                                    listOfComments.invalidateViews();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                        layout.setRefreshing(false);
                    }
                });

                if (uid.equals("BgutY2FFaNakCuL9bIRJmV0LetS2")) {

                    AHBottomNavigationItem item1 = new AHBottomNavigationItem("Publications", R.drawable.ic_action_action_home, R.color.colorPrimary);
                    AHBottomNavigationItem item2 = new AHBottomNavigationItem("Search", R.drawable.ic_action_action_pageview, R.color.colorPrimary);
                    AHBottomNavigationItem item3 = new AHBottomNavigationItem("Write", R.drawable.ic_action_image_edit, R.color.colorPrimary);
                    AHBottomNavigationItem item4 = new AHBottomNavigationItem("Add", R.drawable.ic_action_social_group_add, R.color.colorPrimary);
                    AHBottomNavigationItem item5 = new AHBottomNavigationItem("Profile", R.drawable.ic_action_action_account_box, R.color.colorPrimary);

                    bottomNavigation.addItem(item1);
                    bottomNavigation.addItem(item2);
                    bottomNavigation.addItem(item3);
                    bottomNavigation.addItem(item4);
                    bottomNavigation.addItem(item5);

                    bottomNavigation.setAccentColor(Color.parseColor("#11b2de"));
                    bottomNavigation.setForceTitlesDisplay(true);
                    bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
                        @Override
                        public boolean onTabSelected(int position, boolean wasSelected) {
                            if (position == 1) {
                                onSearchTab();
                            }

                            if (position == 2) {
                                onWriteTab();
                            }

                            if (position == 3) {
                                onAddTab();
                            }

                            if (position == 4) {
                                onProfileTab();
                            }
                            return true;
                        }
                    });
                } else {
                    AHBottomNavigationItem item1 = new AHBottomNavigationItem("Publications", R.drawable.ic_action_action_home, R.color.colorPrimary);
                    AHBottomNavigationItem item2 = new AHBottomNavigationItem("Search", R.drawable.ic_action_action_pageview, R.color.colorPrimary);
                    AHBottomNavigationItem item3 = new AHBottomNavigationItem("Write", R.drawable.ic_action_image_edit, R.color.colorPrimary);
                    AHBottomNavigationItem item5 = new AHBottomNavigationItem("Profile", R.drawable.ic_action_action_account_box, R.color.colorPrimary);

                    bottomNavigation.addItem(item1);
                    bottomNavigation.addItem(item2);
                    bottomNavigation.addItem(item3);
                    bottomNavigation.addItem(item5);

                    bottomNavigation.setAccentColor(Color.parseColor("#11b2de"));
                    bottomNavigation.setForceTitlesDisplay(true);
                    bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
                        @Override
                        public boolean onTabSelected(int position, boolean wasSelected) {
                            if (position == 1) {
                                onSearchTab();
                            }

                            if (position == 2) {
                                onWriteTab();
                            }

                            if (position == 3) {
                                onProfileTab();
                            }
                            return true;
                        }
                    });
                }

                listOfComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        key = commentsArray.size() - position - 1;
                        onComment();
                    }
                });
            }
        }, 5000);
    }

    private void onProfileTab() {
        Intent l = new Intent(this, UserProfileActivity.class);
        startActivity(l);
    }

    private void onSearchTab() {
        Intent k = new Intent(this, SearchActivity.class);
        startActivity(k);
    }

    private void onAddTab() {
        Intent j = new Intent(this, AddProfessorActivity.class);
        startActivity(j);
    }

    private void onWriteTab() {
        Intent i = new Intent(this, WriteActivity.class);
        startActivity(i);
    }

    private void showComments() {
        Feed_Adapter adapter = new Feed_Adapter(this, commentsArray);
        listOfComments.setAdapter(adapter);
    }

    private void onComment() {
        Intent s = new Intent(this, CommentReadActivity.class);
        s.putExtra("key", key);
        startActivity(s);
    }
}
