package adilkarjauv.profrate;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import adilkarjauv.profrate.R;

public class ProfessorsAdapter extends BaseAdapter {


    Context context;
    ArrayList<String> Professors;
    ArrayList<String> Unis;
    LayoutInflater inflater;

    public ProfessorsAdapter(Context context, ArrayList<String> professors, ArrayList<String> unis) {
        this.context = context;
        Professors = professors;
        Unis = unis;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return Professors.size();
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
            convertView = inflater.inflate(R.layout.row_professor_item, null);
            viewHolder = new ViewHolder();
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
            viewHolder.uniTextView = (TextView) convertView.findViewById(R.id.uniTextView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.nameTextView.setText(Professors.get(position));
        viewHolder.uniTextView.setText(Unis.get(position));

        return convertView;
    }

    private class ViewHolder {
        TextView nameTextView;
        TextView uniTextView;
    }

}
