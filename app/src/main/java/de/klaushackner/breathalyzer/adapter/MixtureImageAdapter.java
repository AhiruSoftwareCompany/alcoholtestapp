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
import de.klaushackner.breathalyzer.model.Mixture;
import de.klaushackner.breathalyzer.model.MixtureImage;

public class MixtureImageAdapter extends ArrayAdapter<MixtureImage> {
    private final Context mContext;

    public MixtureImageAdapter(Context context, ArrayList<MixtureImage> arrayList) {
        super(context, R.layout.items_mixtureimage, arrayList);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.items_mixtureimage, parent, false);
        MixtureImage m = getItem(position);
        ImageView image = (ImageView) v.findViewById(R.id.image);

        if (m != null) {
            image.setImageResource(mContext.getResources().getIdentifier(m.toString(), "mipmap",
                    mContext.getApplicationContext().getPackageName()));
        }
        return v;
    }
}
