package de.klaushackner.breathalyzer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.klaushackner.breathalyzer.R;

public class IngredientAdapter extends ArrayAdapter<Ingredient> {

    private final DecimalFormat format = new DecimalFormat();

    public IngredientAdapter(Context context, ArrayList<Ingredient> arrayList) {
        super(context, R.layout.items_ingredient, arrayList);
        format.setDecimalSeparatorAlwaysShown(false);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.items_ingredient, parent, false);
        Ingredient i = getItem(position);

        TextView name = (TextView) v.findViewById(R.id.name);
        TextView percentage = (TextView) v.findViewById(R.id.percentage);
        TextView amount = (TextView) v.findViewById(R.id.amount);

        name.setText(i.getName());
        percentage.setText(format.format(i.getPercentage()) + " %");
        amount.setText(format.format(i.getAmount()) + " ml");


        return v;
    }
}
