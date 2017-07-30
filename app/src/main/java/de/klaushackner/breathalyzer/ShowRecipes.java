package de.klaushackner.breathalyzer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.klaushackner.breathalyzer.adapter.RecipeAdapter;
import de.klaushackner.breathalyzer.model.Ingredient;
import de.klaushackner.breathalyzer.model.Mixture;
import de.klaushackner.breathalyzer.model.Recipe;
import de.klaushackner.breathalyzer.model.User;

public class ShowRecipes extends AppCompatActivity {
    private ListView list;
    private Context c;
    private User currentUser;
    private RecipeAdapter rA;
    private final DecimalFormat format = new DecimalFormat();

    public ShowRecipes() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipes);
        c = this;
        currentUser = User.getUserByCreated(c, getIntent().getLongExtra("currentUser", 0));

        if (getIntent().getLongExtra("currentUser", 0) == 0) {
            Toast.makeText(c, "MEH", Toast.LENGTH_SHORT).show();
            finish();
        }

        list = (ListView) findViewById(R.id.list);

        rA = new RecipeAdapter(this, new ArrayList<Recipe>());
        list.setAdapter(rA);
        updateRecipes();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                final Recipe r = Recipe.getRecipeArray(c)[position];
                final Dialog dialog = new Dialog(c);
                dialog.setContentView(R.layout.dialog_show_recipe);

                dialog.setTitle(r.getName());
                final TextView title = (TextView) dialog.findViewById(android.R.id.title);
                title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                title.setPadding(0, 20, 0, 20);

                Button add = (Button) dialog.findViewById(R.id.add);
                Button cancel = (Button) dialog.findViewById(R.id.cancel);

                TextView text = (TextView) dialog.findViewById(R.id.text);

                String s = r.getText();
                s += "\n\n" + getResources().getString(R.string.ingredience) + "\n";
                for (Ingredient i : r.getIngredients()) {
                    if (i.getAmount() < 10) {
                        s += format.format(i.getAmount()) + "\t\t\t" + i.getName() + "\n";
                    } else {
                        s += format.format(i.getAmount()) + " ml\t\t" + i.getName() + "\n";
                    }
                }
                text.setText(s);
                text.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentUser.addDrink(new Mixture(r.getName(), r.getAmount(), r.getPercentage(), r.getImage()));
                        currentUser.saveUser(c);
                        startActivity(new Intent(c, MainActivity.class).putExtra("fromShowRecipes", true).putExtra("currentUser", currentUser.getCreated()));
                        finish();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;

                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setTitle(R.string.remove_custom_recipe);
                builder.setPositiveButton(R.string.remove_recipe, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Recipe r = Recipe.getRecipeArray(c)[pos];
                        Recipe.removeCustomRecipe(c, r);
                        dialog.dismiss();
                        updateRecipes();
                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        updateRecipes();
                    }
                });

                builder.create().show();
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateRecipes();
    }

    private void updateRecipes() {
        rA.clear();
        rA.notifyDataSetChanged();
        for (Recipe r : Recipe.getRecipeArray(c)) {
            rA.add(r);
        }
    }

    public void addCustomRecipe() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_create_recipe);

        dialog.setTitle(R.string.add_recipe);
        final TextView title = (TextView) dialog.findViewById(android.R.id.title);
        if (title != null) {
            title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            title.setPadding(0, 20, 0, 20);
        }

        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        TextView name = (TextView) dialog.findViewById(R.id.name);
        TextView text = (TextView) dialog.findViewById(R.id.text);
        ListView ingredients = (ListView) dialog.findViewById(R.id.ingredients);
        Button add = (Button) dialog.findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                //Recipe.addCustomRecipe(c, new Recipe(MixtureImage.fromString(image.getTag().toString()), name.getText(), text.getText(), new Ingredient[]));
            }
        });
        dialog.show();
    }

    /**
     * Layout-Stuff
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_recipes, menu);
        MenuItem m = menu.getItem(0);
        m.setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                addCustomRecipe();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}


