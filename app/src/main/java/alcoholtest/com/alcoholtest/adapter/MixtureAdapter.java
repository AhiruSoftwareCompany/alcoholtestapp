package alcoholtest.com.alcoholtest.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import java.util.ArrayList;

import alcoholtest.com.alcoholtest.R;
import alcoholtest.com.alcoholtest.model.Mixture;

public class MixtureAdapter extends ArrayAdapter<Mixture> {
    private MixtureAdapter ma;
    private Mixture mixture;

    public MixtureAdapter(Context context, ArrayList<Mixture> arrayList) {
        super(context, R.layout.items_mixture, arrayList);
        ma = this;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.items_mixture, parent, false);
        mixture = getItem(position);

        /*
        Button name = (Button) v.findViewById(R.id.name);
        name.setText(mixture.getName());
*/
        return v;
    }
}
