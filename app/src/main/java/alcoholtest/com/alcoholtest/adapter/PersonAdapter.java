package alcoholtest.com.alcoholtest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import alcoholtest.com.alcoholtest.R;
import alcoholtest.com.alcoholtest.model.Person;

public class PersonAdapter extends ArrayAdapter<Person> {

    private PersonAdapter pA;
    private Person p;

    PersonAdapter(Context context, ArrayList<Person> arrayList) {
        super(context, R.layout.items_persons, arrayList);
        pA = this;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.items_persons, parent, false);
        p = getItem(position);
        TextView tvName = (TextView) v.findViewById(R.id.tvName);
        TextView tvAge = (TextView) v.findViewById(R.id.tvAge);
        TextView tvWeight = (TextView) v.findViewById(R.id.tvWeight);
        TextView tvHeight = (TextView) v.findViewById(R.id.tvHeight);

        Object o = getItem(position);
        Object p = getItem(position);


        //TODO: Personeninformationen anzeigen
        return v;
    }
}
