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

import de.klaushackner.breathalyzer.R;
import de.klaushackner.breathalyzer.model.Recipe;

public class RecipeAdapter extends ArrayAdapter<Recipe> {
    private final Context mContext;
    private final DecimalFormat format = new DecimalFormat();

    public RecipeAdapter(Context context, ArrayList<Recipe> arrayList) {
        super(context, R.layout.items_mixture, arrayList);
        mContext = context;
        format.setDecimalSeparatorAlwaysShown(false);
        format.setMaximumFractionDigits(2);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.items_recipe, parent, false);
        final Recipe recipe = getItem(position);

        TextView name = (TextView) v.findViewById(R.id.name);
        TextView amount = (TextView) v.findViewById(R.id.amount);
        TextView percentage = (TextView) v.findViewById(R.id.percentage);
        final ImageView image = (ImageView) v.findViewById(R.id.image);

        name.setText(recipe.getName());
        if (recipe.getAmount() < 200) {
            amount.setText(format.format(recipe.getAmount()) + " ml");
        } else {
            amount.setText(format.format(recipe.getAmount() / 1000) + " l");
        }

        percentage.setText(format.format(recipe.getPercentage() * 100) + " %");

        image.setImageResource(mContext.getResources().getIdentifier(recipe.getImage().toString(), "mipmap",
                mContext.getApplicationContext().getPackageName()));

        return v;
    }
}
