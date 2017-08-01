package de.klaushackner.breathalyzer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.klaushackner.breathalyzer.R;
import de.klaushackner.breathalyzer.model.Ingredient;

public class IngredientAdapter extends ArrayAdapter<Ingredient> {

    public IngredientAdapter(Context context, ArrayList<Ingredient> arrayList) {
        super(context, R.layout.items_ingredient, arrayList);
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
        percentage.setText(i.getPercentage()+ " %");
        amount.setText(i.getAmount() + " ml");


        return v;
    }
}
