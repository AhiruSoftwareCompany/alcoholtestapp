package alcoholtest.com.alcoholtest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import alcoholtest.com.alcoholtest.R;
import alcoholtest.com.alcoholtest.model.Drink;
import alcoholtest.com.alcoholtest.model.User;

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
        View v = inflater.inflate(R.layout.items_users, parent, false);
        d = getItem(position);

//TODO: Show information

        return v;
    }
}