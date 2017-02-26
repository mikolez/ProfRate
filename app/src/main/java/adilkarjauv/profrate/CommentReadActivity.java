package adilkarjauv.profrate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import io.techery.properratingbar.ProperRatingBar;

public class CommentReadActivity extends AppCompatActivity {

    private TextView commentPage;
    private TextView readProfName;
    private TextView readUniName;
    private TextView readTimeText;
    private ProperRatingBar readCommentRating;
    private ImageView readImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_read);

        commentPage = (TextView) findViewById(R.id.commentPage);
        readProfName = (TextView) findViewById(R.id.readProfName);
        readUniName = (TextView) findViewById(R.id.readUniName);
        readTimeText = (TextView) findViewById(R.id.readTimeText);
        readCommentRating = (ProperRatingBar) findViewById(R.id.readCommentRating);
        readImageView = (ImageView) findViewById(R.id.readImageView);

        Intent j = getIntent();
        Comment a = FeedActivity.commentsArray.get(j.getExtras().getInt("key"));
        commentPage.setText(a.getText());
        commentPage.setMovementMethod(new ScrollingMovementMethod());

        readProfName.setText(a.getProf());
        readUniName.setText(WriteActivity.map.get(readProfName.getText().toString()).getUniversity());

        int year = Integer.parseInt(a.getTime().substring(0,4));
        int month = Integer.parseInt(a.getTime().substring(5, 7));
        int day = Integer.parseInt(a.getTime().substring(8, 10));
        int hour = Integer.parseInt(a.getTime().substring(11, 13));
        int minute = Integer.parseInt(a.getTime().substring(14, 16));
        int sec = Integer.parseInt(a.getTime().substring(17, 19));
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date nowDate = cal.getTime();
        long now = nowDate.getTime();
        cal.set(year, month - 1, day, hour, minute, sec);
        Date oldDate = cal.getTime();
        long old = oldDate.getTime();

        readTimeText.setText(DateUtils.getRelativeTimeSpanString(old, now, 0));
        readCommentRating.setRating(Integer.parseInt(a.getRating()));
        Picasso.with(this).load(WriteActivity.map.get(readProfName.getText().toString()).getImageUrl()).into(readImageView);
    }
}
