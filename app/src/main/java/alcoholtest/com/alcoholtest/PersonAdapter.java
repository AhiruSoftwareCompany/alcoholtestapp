package alcoholtest.com.alcoholtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.util.ArrayList;

public class PersonAdapter extends ArrayAdapter<Person>{

    private PersonAdapter pA;
    private Person p;

    PersonAdapter(Context context, ArrayList<Person> arrayList) {
        super(context, R.layout.persons_items, arrayList);
        pA = this;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.persons_items, parent, false);
        p = getItem(position);

        //TODO: Personeninformationen anzeigen
        return v;
    }
}
