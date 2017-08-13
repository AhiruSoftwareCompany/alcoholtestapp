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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.klaushackner.breathalyzer.adapter.IngredientAdapter;
import de.klaushackner.breathalyzer.adapter.MixtureImageAdapter;
import de.klaushackner.breathalyzer.adapter.RecipeAdapter;
import de.klaushackner.breathalyzer.model.Ingredient;
import de.klaushackner.breathalyzer.model.IngredientsView;
import de.klaushackner.breathalyzer.model.Mixture;
import de.klaushackner.breathalyzer.model.MixtureImage;
import de.klaushackner.breathalyzer.model.Recipe;
import de.klaushackner.breathalyzer.model.User;

public class ShowRecipes extends AppCompatActivity {
    private final DecimalFormat format = new DecimalFormat();
    private ListView list;
    private Context c;
    private User currentUser;
    private RecipeAdapter rA;

    public ShowRecipes() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipes);
        c = this;
        currentUser = User.getUserByCreated(c, getIntent().getLongExtra("currentUser", 0));

        if (getIntent().getLongExtra("currentUser", 0) == 0) {
            Toast.makeText(c, "Fehler", Toast.LENGTH_SHORT).show();
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

        final ImageView image = (ImageView) dialog.findViewById(R.id.image);
        final TextView name = (TextView) dialog.findViewById(R.id.name);
        final TextView text = (TextView) dialog.findViewById(R.id.text);
        final IngredientsView ingr = (IngredientsView) dialog.findViewById(R.id.ingredients);
        Button addIngr = (Button) dialog.findViewById(R.id.addIngr);
        Button add = (Button) dialog.findViewById(R.id.add);

        MixtureImage currentImage = MixtureImage.beer;
        image.setImageResource(getResources().getIdentifier(currentImage.toString(), "mipmap",
                getApplicationContext().getPackageName()));
        image.setTag(currentImage);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(c);
                dialog.setContentView(R.layout.dialog_switch_mixtureimage);

                dialog.setTitle(R.string.switch_mixtureimage);
                final TextView title = (TextView) dialog.findViewById(android.R.id.title);
                if (title != null) {
                    title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    title.setPadding(0, 20, 0, 20);
                }

                final MixtureImageAdapter mA = new MixtureImageAdapter(c, new ArrayList<MixtureImage>());
                GridView mixtureList = (GridView) dialog.findViewById(R.id.mixtureList);
                mixtureList.setAdapter(mA);

                final MixtureImage[] mixtureItemArray = MixtureImage.values();

                for (MixtureImage aMixtureItemArray : mixtureItemArray) {
                    mA.add(aMixtureItemArray);
                }

                mixtureList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        MixtureImage m = mixtureItemArray[position];

                        image.setTag(m); //Updates tag if necessary
                        image.setImageResource(getResources().getIdentifier(m.toString(), "mipmap",
                                getApplicationContext().getPackageName()));
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        final IngredientAdapter ingrAdapt = new IngredientAdapter(this, new ArrayList<Ingredient>());
        ingr.setAdapter(ingrAdapt);

        addIngr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog d = new Dialog(c);

                d.setContentView(R.layout.dialog_add_ingredient);

                d.setTitle(R.string.add_recipe);
                final TextView title = (TextView) d.findViewById(android.R.id.title);
                if (title != null) {
                    title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    title.setPadding(0, 20, 0, 20);
                }

                Button add = (Button) d.findViewById(R.id.add);

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String name = ((EditText) d.findViewById(R.id.name)).getText().toString();
                        final Double percentage = Double.parseDouble("0" + ((EditText) d.findViewById(R.id.percentage)).getText().toString());
                        final Double amount = Double.parseDouble("0" + ((EditText) d.findViewById(R.id.amount)).getText().toString());

                        if (isValidIngredient(name, percentage, amount)) {
                            ingrAdapt.add(new Ingredient(name, percentage, amount));
                            ingr.addIngredient(new Ingredient(name, percentage, amount));
                            d.dismiss();
                        } else {
                            Toast.makeText(c, c.getResources().getString(R.string.wronginput).toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                d.show();
            }
        });

/*
        ingr.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ingrAdapt.remove(ingr.getIngredientById(position));
                ingr.removeIngredient(position);
                ingr.deferNotifyDataSetChanged();
                ingrAdapt.notifyDataSetChanged();
                return false;
            }
        });
*/
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ingr.getIngredients().length > 0) {
                    Recipe.addCustomRecipe(c, new Recipe(MixtureImage.fromString(image.getTag().toString()),
                            name.getText().toString(), text.getText().toString(), ingr.getIngredients()));
                    dialog.dismiss();
                } else {
                    Toast.makeText(c, "Es wurden keine Zutaten angegeben", Toast.LENGTH_SHORT).show();
                }
                updateRecipes();
            }
        });
        dialog.show();
    }

    private boolean isValidIngredient(String name, double percentage, double amount) {
        return name.length() > 3 && percentage >= 0 && amount > 0;
    }

    /**
     * Layout-Stuff
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_recipes, menu);
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


