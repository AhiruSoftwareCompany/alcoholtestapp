package de.klaushackner.alcoholtest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.klaushackner.alcoholtest.R;
import de.klaushackner.alcoholtest.model.Drink;

public class DrinkAdapter extends ArrayAdapter<Drink> {

    private Drink d;
    private DecimalFormat format = new DecimalFormat();
    private final Context mContext;

    public DrinkAdapter(Context context, ArrayList<Drink> arrayList) {
        super(context, R.layout.items_drink, arrayList);
        mContext = context;
        format.setDecimalSeparatorAlwaysShown(false);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.items_drink, parent, false);

        TextView name = (TextView) v.findViewById(R.id.name);
        TextView timestamp = (TextView) v.findViewById(R.id.timestamp);
        TextView runsout = (TextView) v.findViewById(R.id.runsout);
        TextView promille = (TextView) v.findViewById(R.id.promille);
        ImageView iv = (ImageView) v.findViewById(R.id.imageView);

        d = getItem(position);
        long ago = new Date().getTime() - d.getTime();

        //TODO: StringBuilder or something else than this crap
        if (d.getMixture().getAmount() < 100) {
            name.setText(format.format(d.getMixture().getAmount()) + " ml " + d.getMixture().getName() + " (" + format.format(d.getMixture().getPercentage() * 100) + " %)");
        } else {
            name.setText(format.format(d.getMixture().getAmount() / 1000) + " l " + d.getMixture().getName() + " (" + format.format(d.getMixture().getPercentage() * 100) + " %)");
        }

        timestamp.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(ago), TimeUnit.
                MILLISECONDS.toMinutes(ago) % TimeUnit.HOURS.toMinutes(1)) + " ago");

        if (d.getMixture().getImage().compareTo("") != 0) {
            iv.setImageResource(mContext.getResources().getIdentifier(d.getMixture().getImage(), "drawable",
                    mContext.getApplicationContext().getPackageName()));
        }


        return v;
    }
}