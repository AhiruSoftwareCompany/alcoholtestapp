package de.klaushackner.breathalyzer;


import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class DrinkAdapter extends ArrayAdapter<Drink> {

    private final DecimalFormat format = new DecimalFormat("#.##");
    private final Context mContext;

    public DrinkAdapter(Context context, ArrayList<Drink> arrayList) {
        super(context, R.layout.items_drink, arrayList);
        mContext = context;
        format.setDecimalSeparatorAlwaysShown(false);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);


        Drink d = getItem(position);
        View v;

        // If the drink is depleted, set an other layout
        if (d.getDepletionPoint() - System.currentTimeMillis() < 0) {
            v = inflater.inflate(R.layout.items_depleted_drink, parent, false);


            TextView name = v.findViewById(R.id.name);
            TextView drinkingBeginning = v.findViewById(R.id.drinking_beginning);
            TextView depletedSince = v.findViewById(R.id.depleted_since);
            ImageView iv = v.findViewById(R.id.imageView);

            SimpleDateFormat sDF = new SimpleDateFormat("dd.MM.yy HH:mm");


            if (d.getAmount() < 100) {
                name.setText(String.format(Locale.GERMAN, "%.2g ml %s (%.2g %%)", d.getAmount(), d.getName(), d.getAlcContent() * 100.0));
                // name.setText(format.format(m.getAmount()) + " ml " + m.getName() + " (" + format.format(m.getPercentage() * 100) + " %)");
            } else {
                name.setText(String.format(Locale.GERMAN, "%.2g l %s (%.2g %%)", d.getAmount() / 1000, d.getName(), d.getAlcContent() * 100.0));
                // name.setText(format.format(m.getAmount() / 1000) + " l " + m.getName() + " (" + format.format(m.getPercentage() * 100) + " %)");
            }

            //takingTime.setText(String.format(Locale.GERMAN, "%02d:%02d", TimeUnit.MILLISECONDS.toHours(ago), TimeUnit.MILLISECONDS.toMinutes(ago) % TimeUnit.HOURS.toMinutes(1)));
            drinkingBeginning.setText(String.format(Locale.GERMAN, "%s", sDF.format(new Date(d.getConsumePoint()))));
            depletedSince.setText(String.format(Locale.GERMAN, "%s", sDF.format(new Date(d.getConsumePoint() + d.getDepletingDuration()))));

            if (d.getMixtureImage() != null) {
                iv.setImageResource(mContext.getResources().getIdentifier(d.getMixtureImage().toString(), "mipmap",
                        mContext.getApplicationContext().getPackageName()));
            }

        } else {
            v = inflater.inflate(R.layout.items_drink, parent, false);

            TextView alcoholLevel = v.findViewById(R.id.alcoholLevel);
            TextView elap = v.findViewById(R.id.elap);
            TextView left = v.findViewById(R.id.left);

            TextView name = v.findViewById(R.id.name);
            TextView takingTime = v.findViewById(R.id.takingTime);
            TextView expireTime = v.findViewById(R.id.expireTime);
            TextView bac = v.findViewById(R.id.bac);
            ImageView iv = v.findViewById(R.id.imageView);

            long ago = System.currentTimeMillis() - d.getConsumePoint();
            long expires = d.getDepletionPoint() - System.currentTimeMillis();

            if (expires < 0) {
                alcoholLevel.setAlpha(0.5f);
                elap.setAlpha(0.5f);
                left.setAlpha(0.5f);
                name.setAlpha(0.5f);
                takingTime.setAlpha(0.5f);
                expireTime.setAlpha(0.5f);
                bac.setAlpha(0.5f);
                expires = 0;
            }

            if (position == 0) {
                bac.setText(format.format(d.getRelativeBac()) + " ‰");
            } else {
                bac.setText(format.format(d.getBac()) + " ‰");
            }

            if (d.getAmount() < 100) {
                name.setText(String.format(Locale.GERMAN, "%.2g ml %s (%.2g %%)", d.getAmount(), d.getName(), d.getAlcContent() * 100.0));
                // name.setText(format.format(m.getAmount()) + " ml " + m.getName() + " (" + format.format(m.getPercentage() * 100) + " %)");
            } else {
                name.setText(String.format(Locale.GERMAN, "%.2g l %s (%.2g %%)", d.getAmount() / 1000, d.getName(), d.getAlcContent() * 100.0));
                // name.setText(format.format(m.getAmount() / 1000) + " l " + m.getName() + " (" + format.format(m.getPercentage() * 100) + " %)");
            }

            takingTime.setText(String.format(Locale.GERMAN, "%02d:%02d", TimeUnit.MILLISECONDS.toHours(ago), TimeUnit.
                    MILLISECONDS.toMinutes(ago) % TimeUnit.HOURS.toMinutes(1)));

            expireTime.setText(String.format(Locale.GERMAN, "%02d:%02d", TimeUnit.MILLISECONDS.toHours(expires), TimeUnit.
                    MILLISECONDS.toMinutes(expires) % TimeUnit.HOURS.toMinutes(1)));

            if (d.getMixtureImage() != null) {
                iv.setImageResource(mContext.getResources().getIdentifier(d.getMixtureImage().toString(), "mipmap",
                        mContext.getApplicationContext().getPackageName()));
            }
        }


        return v;
    }
}