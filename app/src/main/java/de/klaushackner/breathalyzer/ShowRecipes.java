package de.klaushackner.breathalyzer;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import de.klaushackner.breathalyzer.adapter.MixtureAdapter;
import de.klaushackner.breathalyzer.adapter.MixtureImageAdapter;
import de.klaushackner.breathalyzer.adapter.RecipeAdapter;
import de.klaushackner.breathalyzer.model.Ingredient;
import de.klaushackner.breathalyzer.model.Mixture;
import de.klaushackner.breathalyzer.model.MixtureImage;
import de.klaushackner.breathalyzer.model.Recipe;

public class ShowRecipes extends AppCompatActivity {
    private ListView list;
    private TextView name;
    private TextView percentage;
    private TextView amount;
    private ImageView image;
    private Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipes);
        c = this;

        try {

        list = (ListView) findViewById(R.id.list);

        RecipeAdapter recipeAdapter = new RecipeAdapter(this, new ArrayList<Recipe>());
        list.setAdapter(recipeAdapter);


        JSONArray mixtures = null;
            mixtures = new JSONArray("[]");
        mixtures.put(new Mixture("Bier", 500, 0.05, MixtureImage.beer).toString());
        mixtures.put(new Mixture("Bier", 1000, 0.05, MixtureImage.morebeer).toString());
        mixtures.put(new Mixture("Goaß", 270, 0.06296296296, MixtureImage.goass).toString());
        mixtures.put(new Mixture("Goaßmaß", 540, 0.06296296296, MixtureImage.goass).toString());
        mixtures.put(new Mixture("Pils", 330, 0.048, MixtureImage.pils).toString());
        mixtures.put(new Mixture("Pils", 500, 0.048, MixtureImage.pils).toString());
        mixtures.put(new Mixture("Wein", 200, 0.10, MixtureImage.wine).toString());
        mixtures.put(new Mixture("Wodka", 20, 0.30, MixtureImage.vodka).toString());
        mixtures.put(new Mixture("Wodka", 20, 0.40, MixtureImage.vodka).toString());
        mixtures.put(new Mixture("Whisky", 20, 0.40, MixtureImage.whisky).toString());
        mixtures.put(new Mixture("Sekt", 200, 0.12, MixtureImage.sparklingwine).toString());


        Ingredient[] a = new Ingredient[2];
        a[0] = new Ingredient("Test", 0.20, 200);
        a[1] = new Ingredient("Test2", 0.30, 500);
        recipeAdapter.add(new Recipe(MixtureImage.bluebeer, "Test", a));
        recipeAdapter.add(new Recipe(MixtureImage.bluebeer, "Test", a));

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveRecipe() {
        MixtureImage.fromString(image.getTag().toString());
    }

}


