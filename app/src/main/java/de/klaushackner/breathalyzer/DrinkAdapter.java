package de.klaushackner.breathalyzer;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class DrinkAdapter extends ArrayAdapter<Drink> {

    private final DecimalFormat format = new DecimalFormat();
    private final Context mContext;

    public DrinkAdapter(Context context, ArrayList<Drink> arrayList) {
        super(context, R.layout.items_drink, arrayList);
        mContext = context;
        format.setDecimalSeparatorAlwaysShown(false);
        format.setMaximumFractionDigits(2);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.items_drink, parent, false);

        TextView name = v.findViewById(R.id.name);
        TextView takingTime = v.findViewById(R.id.takingTime);
        TextView expireTime = v.findViewById(R.id.expireTime);
        TextView bac = v.findViewById(R.id.bac);
        ImageView iv = v.findViewById(R.id.imageView);

        Drink d = getItem(position);
        long ago = System.currentTimeMillis() - d.consumePoint;
        long expires = 0; //d.getExpireTime() - new Date().getTime();

        if (d.getAmount() < 100) {
            name.setText(String.format(Locale.GERMAN, "%.2g ml %s (%.2g %%)", d.getAmount(), d.name, d.getAlcContent() * 100.0));
            // name.setText(format.format(m.getAmount()) + " ml " + m.getName() + " (" + format.format(m.getPercentage() * 100) + " %)");
        } else {
            name.setText(String.format(Locale.GERMAN, "%.2g l %s (%.2g %%)", d.getAmount() / 1000, d.name, d.getAlcContent() * 100.0));
            // name.setText(format.format(m.getAmount() / 1000) + " l " + m.getName() + " (" + format.format(m.getPercentage() * 100) + " %)");
        }

        takingTime.setText(String.format(Locale.GERMAN, "%02d:%02d", TimeUnit.MILLISECONDS.toHours(ago), TimeUnit.
                MILLISECONDS.toMinutes(ago) % TimeUnit.HOURS.toMinutes(1)));
        expireTime.setText(String.format(Locale.GERMAN, "%02d:%02d", TimeUnit.MILLISECONDS.toHours(expires), TimeUnit.
                MILLISECONDS.toMinutes(expires) % TimeUnit.HOURS.toMinutes(1)));
/*
        if (d.getMixture().getImage() != null) {
            iv.setImageResource(mContext.getResources().getIdentifier(d.getMixture().getImageString(), "mipmap",
                    mContext.getApplicationContext().getPackageName()));
        }
*/

        bac.setText(format.format(d.getBac()) + " ‰");
        //bac.setText(format.format(d.getRelativeBac()) + " ‰");
        return v;
    }
}