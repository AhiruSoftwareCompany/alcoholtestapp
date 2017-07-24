package de.klaushackner.breathalyzer;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import de.klaushackner.breathalyzer.adapter.RecipeAdapter;
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


        list = (ListView) findViewById(R.id.list);

        RecipeAdapter recipeAdapter = new RecipeAdapter(this, new ArrayList<Recipe>());
        list.setAdapter(recipeAdapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(c, position, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void saveRecipe() {
        MixtureImage.fromString(image.getTag().toString());
    }

}


