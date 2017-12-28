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
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.klaushackner.breathalyzer.R;
import de.klaushackner.breathalyzer.model.Drink;
import de.klaushackner.breathalyzer.model.Recipe;

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
        long ago = new Date().getTime() - d.takingTime;
        //long expires = d.expireTime(current User) - new Date().getTime();

        Recipe r = d.r;
        if (r.amount() < 100) {
            name.setText(String.format(Locale.GERMAN, "%.2g ml %s (%.2g %%)", r.amount(), r.name, r.alcContent() * 100.0));
        } else {
            name.setText(String.format(Locale.GERMAN, "%.2g l %s (%.2g %%)", r.amount() / 1000, r.name, r.alcContent() * 100.0));
        }

        takingTime.setText(String.format(Locale.GERMAN, "%02d:%02d", TimeUnit.MILLISECONDS.toHours(ago), TimeUnit.
                MILLISECONDS.toMinutes(ago) % TimeUnit.HOURS.toMinutes(1)));
        // expireTime.setText(String.format(Locale.GERMAN, "%02d:%02d", TimeUnit.MILLISECONDS.toHours(expires), TimeUnit.
        //        MILLISECONDS.toMinutes(expires) % TimeUnit.HOURS.toMinutes(1)));

        if (d.r.imgString != null) {
            iv.setImageResource(mContext.getResources().getIdentifier(d.r.imgString.toString(), "mipmap",
                    mContext.getApplicationContext().getPackageName()));
        }

        //bac.setText(format.format(d.r.getBac(User)) + " ‰");
        return v;
    }
}