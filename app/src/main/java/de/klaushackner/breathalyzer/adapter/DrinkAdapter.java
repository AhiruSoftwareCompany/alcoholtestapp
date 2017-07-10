package de.klaushackner.breathalyzer.adapter;

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
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.klaushackner.breathalyzer.R;
import de.klaushackner.breathalyzer.model.Drink;
import de.klaushackner.breathalyzer.model.Mixture;

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

        TextView name = (TextView) v.findViewById(R.id.name);
        TextView takingTime = (TextView) v.findViewById(R.id.takingTime);
        TextView expireTime = (TextView) v.findViewById(R.id.expireTime);
        TextView bac = (TextView) v.findViewById(R.id.bac);
        ImageView iv = (ImageView) v.findViewById(R.id.imageView);

        Drink d = getItem(position);
        long ago = new Date().getTime() - d.getTakingTime();
        long expires = d.getExpireTime() - new Date().getTime();

        Mixture m = d.getMixture();
        if (d.getMixture().getAmount() < 100) {
            name.setText(String.format("%.2f ml %s (%.2f %%)", m.getAmount(), m.getName(), m.getPercentage() * 100.0));
            //name.setText(format.format(m.getAmount()) + " ml " + m.getName() + " (" + format.format(m.getPercentage() * 100) + " %)");
        } else {
            name.setText(String.format("%.2f l %s (%.2f %%)", m.getAmount() / 1000, m.getName(), m.getPercentage() * 100.0));
            //name.setText(format.format(m.getAmount() / 1000) + " l " + m.getName() + " (" + format.format(m.getPercentage() * 100) + " %)");
        }

        takingTime.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(ago), TimeUnit.
                MILLISECONDS.toMinutes(ago) % TimeUnit.HOURS.toMinutes(1)));
        expireTime.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(expires), TimeUnit.
                MILLISECONDS.toMinutes(expires) % TimeUnit.HOURS.toMinutes(1)));

        if (d.getMixture().getImage().compareTo("") != 0) {
            iv.setImageResource(mContext.getResources().getIdentifier(d.getMixture().getImage(), "drawable",
                    mContext.getApplicationContext().getPackageName()));
        }

        bac.setText(format.format(d.getBac()) + " ‰");
        return v;
    }
}