package adilkarjauv.profrate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfessorProfileActivity extends AppCompatActivity {

    private TextView profProfileName;
    private TextView profProfileUniName;
    private TextView profProfileRating;
    private ArrayList<Comment> profProfileComments;
    private ImageView profProfileImage;
    private ListView profProfileListView;
    private ArrayList<Integer> keyStorage;
    private int key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_profile);

        profProfileName = (TextView) findViewById(R.id.profProfileName);
        profProfileUniName = (TextView) findViewById(R.id.profProfileUniName);
        profProfileRating = (TextView) findViewById(R.id.profProfileRating);
        profProfileListView = (ListView) findViewById(R.id.profProfileListView);
        profProfileImage = (ImageView) findViewById(R.id.profProfileImage);

        profProfileComments = new ArrayList<>();
        keyStorage = new ArrayList<>();

        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");

        Picasso.with(this).load(WriteActivity.map.get(name).getImageUrl()).into(profProfileImage);
        profProfileName.setText(name);
        profProfileUniName.setText(WriteActivity.map.get(name).getUniversity());

        if (WriteActivity.map.get(name).getCount() != null) {
            float x = Float.parseFloat(WriteActivity.map.get(name).getSumRating());
            float y = Float.parseFloat(WriteActivity.map.get(name).getCount());
            profProfileRating.setText("Overall Rating: " + String.format("%.2f", x / y));
        }

        for (int i = 0; i < FeedActivity.commentsArray.size(); i++) {
            if (name.equals(FeedActivity.commentsArray.get(i).getProf())) {
                profProfileComments.add(FeedActivity.commentsArray.get(i));
                keyStorage.add(i);
            }
        }

        showComments();

        profProfileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                key = keyStorage.get(profProfileComments.size() - position - 1);
                onComment();
            }
        });
    }

    private void onComment() {
        Intent s = new Intent(this, CommentReadActivity.class);
        s.putExtra("key", key);
        startActivity(s);
    }

    private void showComments() {
        Feed_Adapter adapter = new Feed_Adapter(this, profProfileComments);
        profProfileListView.setAdapter(adapter);
    }
}
