package de.klaushackner.breathalyzer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<User> {

    private final DecimalFormat format = new DecimalFormat();
    private final Context mContext;

    public UserAdapter(Context context, ArrayList<User> arrayList) {
        super(context, R.layout.items_users, arrayList);
        mContext = context;
        format.setDecimalSeparatorAlwaysShown(false);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.items_users, parent, false);

        TextView tvName = v.findViewById(R.id.name);
        TextView tvAge = v.findViewById(R.id.age);
        TextView tvWeight = v.findViewById(R.id.weight);
        TextView tvHeight = v.findViewById(R.id.height);

        User u = getItem(position);

        tvName.setText(u != null ? u.name : null);
        tvAge.setText(format.format(u != null ? u.age : 0) + " " + mContext.getResources().getString(R.string.years));
        tvWeight.setText(format.format(u != null ? u.weight : 0) + " kg");
        tvHeight.setText(format.format(u.height) + " cm");
        return v;
    }
}