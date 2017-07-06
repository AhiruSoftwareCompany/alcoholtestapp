package de.klaushackner.alcoholtest.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.klaushackner.alcoholtest.R;
import de.klaushackner.alcoholtest.model.User;

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

        TextView tvName = (TextView) v.findViewById(R.id.name);
        TextView tvAge = (TextView) v.findViewById(R.id.age);
        TextView tvWeight = (TextView) v.findViewById(R.id.weight);
        TextView tvHeight = (TextView) v.findViewById(R.id.height);

        User u = getItem(position);

        tvName.setText(u != null ? u.getName() : null);
        tvAge.setText(format.format(u != null ? u.getAge() : 0) + " " + mContext.getResources().getString(R.string.years));
        tvWeight.setText(format.format(u != null ? u.getWeight() : 0) + " kg");
        tvHeight.setText(format.format(u.getHeight()) + " cm");
        return v;
    }
}