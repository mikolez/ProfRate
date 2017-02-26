package adilkarjauv.profrate;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.format.DateUtils;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import io.techery.properratingbar.ProperRatingBar;

public class Feed_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Comment> comments;
    LayoutInflater inflater;
    ImageLoader mImageLoader;
    RequestQueue mRequestQueue;

    public Feed_Adapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mRequestQueue = Volley.newRequestQueue(context);

        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {

            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_comment_item, null);
            viewHolder = new ViewHolder();
            viewHolder.profNameComment = (TextView) convertView.findViewById(R.id.profNameComment);
            viewHolder.commentText = (TextView) convertView.findViewById(R.id.commentText);
            viewHolder.uniName = (TextView) convertView.findViewById(R.id.uniName);
            viewHolder.timeText = (TextView) convertView.findViewById(R.id.timeText);
            viewHolder.coverNetworkImageView = (NetworkImageView) convertView.findViewById(R.id.coverNetworkImageView);
            viewHolder.commentRating = (ProperRatingBar) convertView.findViewById(R.id.commentRating);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Comment a = comments.get(comments.size() - position - 1);

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

        viewHolder.commentText.setMaxLines(5);
        viewHolder.profNameComment.setText(a.getProf());
        viewHolder.commentText.setText(a.getText());
        viewHolder.uniName.setText(WriteActivity.map.get(a.getProf()).getUniversity());
        viewHolder.timeText.setText(DateUtils.getRelativeTimeSpanString(old, now, 0));
        viewHolder.coverNetworkImageView.setImageUrl(WriteActivity.map.get(a.getProf()).getImageUrl(), mImageLoader);
        viewHolder.commentRating.setRating(Integer.parseInt(a.getRating()));

        return convertView;
    }

    private class ViewHolder {
        TextView profNameComment;
        ExpandableTextView expTv1;
        TextView commentText;
        TextView uniName;
        TextView timeText;
        NetworkImageView coverNetworkImageView;
        ProperRatingBar commentRating;
    }
}
