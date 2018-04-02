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

public class MixtureAdapter extends ArrayAdapter<Mixture> {
    private final Context mContext;

    public MixtureAdapter(Context context, ArrayList<Mixture> arrayList) {
        super(context, R.layout.items_mixture, arrayList);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.items_mixture, parent, false);
        Mixture mixture = getItem(position);


        TextView name = v.findViewById(R.id.name);
        TextView amount = v.findViewById(R.id.amount);
        TextView percentage = v.findViewById(R.id.percentage);
        ImageView image = v.findViewById(R.id.image);

        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(false);

        name.setText(mixture.name);
        if (mixture.getAmount() < 100) {
            amount.setText(format.format(mixture.getAmount()) + " ml");
        } else {
            amount.setText(format.format(mixture.getAmount() / 1000) + " l");
        }
        percentage.setText(format.format(mixture.getAlcContent() * 100) + " %");

        if (mixture.image != null) {
            image.setImageResource(mContext.getResources().getIdentifier(mixture.image.toString(), "mipmap",
                    mContext.getApplicationContext().getPackageName()));
        }
        return v;
    }
}
