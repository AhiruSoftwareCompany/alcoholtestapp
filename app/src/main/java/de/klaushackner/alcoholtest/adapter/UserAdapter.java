package de.klaushackner.alcoholtest.adapter;

import android.content.Context;
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

    private User p;
    private DecimalFormat format = new DecimalFormat();
    private final Context mContext;

    public UserAdapter(Context context, ArrayList<User> arrayList) {
        super(context, R.layout.items_users, arrayList);
        mContext = context;
        format.setDecimalSeparatorAlwaysShown(false);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.items_users, parent, false);
        p = getItem(position);
        TextView tvName = (TextView) v.findViewById(R.id.tvName);
        TextView tvAge = (TextView) v.findViewById(R.id.tvAge);
        TextView tvWeight = (TextView) v.findViewById(R.id.tvWeight);
        TextView tvHeight = (TextView) v.findViewById(R.id.tvHeight);

        User o = getItem(position);

        tvName.setText(o.getName());
        tvAge.setText(format.format(o.getAge()) + " " + mContext.getResources().getString(R.string.years));
        tvWeight.setText(format.format(o.getWeight()) + " kg");
        tvHeight.setText(format.format(o.getHeight()) + " cm");


        //TODO: user informationen anzeigen
        return v;
    }
}