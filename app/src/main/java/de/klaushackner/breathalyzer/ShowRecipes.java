package de.klaushackner.breathalyzer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.klaushackner.breathalyzer.model.Content;

public class ShowRecipes extends AppCompatActivity {
    private User currentUser;
    private final DecimalFormat format = new DecimalFormat();
    private ListView list;
    private MixtureAdapter mA;
    private Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipes);
        currentUser = User.getUserByCreated(this, getIntent().getLongExtra("currentUser", 0));
        list = findViewById(R.id.list);
        c = this;

        if (getIntent().getLongExtra("currentUser", 0) == 0) {
            Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
            finish();
        }

        mA = new MixtureAdapter(this, new ArrayList<Mixture>());
        list.setAdapter(mA);
        updateMixtures();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                final Mixture m = Mixture.getRecipeArray(c, currentUser).get(position);
                final Dialog dialog = new Dialog(c);
                dialog.setContentView(R.layout.dialog_show_recipe);

                TextView title = dialog.findViewById(R.id.title);
                title.setText(m.name);

                Button add = dialog.findViewById(R.id.add);
                Button cancel = dialog.findViewById(R.id.cancel);
                TextView description = dialog.findViewById(R.id.description);


                String s = m.description;
                s += "\n\n" + getResources().getString(R.string.ingredients) + "\n";
                for (Content c : m.content) {
                    if (c.amount < 10) {
                        s += format.format(c.amount) + "\t\t\t\t\t\t\t" + c.name + "\n";
                    } else {
                        if (c.amount < 99){
                            //less than 100 ml
                            s += format.format(c.amount) + " ml\t\t\t" + c.name + "\n";
                        } else {
                            //more than 99 ml
                            s += format.format(c.amount) + " ml\t\t" + c.name + "\n";
                        }
                    }
                }
                description.setText(s);
                description.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentUser.consumeDrink(m);
                        currentUser.saveUser(c);
                        startActivity(new Intent(c, MainActivity.class).putExtra("fromShowRecipes", true).putExtra("currentUser", currentUser.created));
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
/*
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;

                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setTitle(R.string.remove_custom_Mixture);
                builder.setPositiveButton(R.string.remove_Mixture, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Mixture m = Mixture.getMixtureArray(c)[pos];
                        Mixture.removeCustomMixture(c, r);
                        dialog.dismiss();
                        updateMixtures();
                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        updateMixtures();
                    }
                });

                builder.create().show();
                return true;
            }
        });
        */
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMixtures();
    }

    private void updateMixtures() {
        mA.clear();
        mA.notifyDataSetChanged();
        for (Mixture m : Mixture.getRecipeArray(c, currentUser)) {
            mA.add(m);
        }
    }

    public void addCustomMixture() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_create_recipe);

/*
        dialog.setTitle(R.string.add_Mixture);
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

                d.setTitle(R.string.add_ingredient);
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
                        final Double percentage = Double.parseDouble("0" + ((EditText) d.findViewById(R.id.percentage)).getText().toString()) / 100;
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


        ingr.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ingrAdapt.clear();
                ingrAdapt.notifyDataSetChanged();
                ingrAdapt.remove(ingr.getIngredientById(position));
                ingr.removeIngredient(position);

                for (int i = 0; i < ingr.getIngredients().length; i++) {
                    ingrAdapt.add(ingr.getIngredientById(i));
                }

                ingrAdapt.notifyDataSetChanged();
                return true;
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ingr.getIngredients().length > 0) {
                    Mixture.addCustomMixture(c, new Mixture(MixtureImage.fromString(image.getTag().toString()),
                            name.getText().toString(), text.getText().toString(), ingr.getIngredients()));
                    dialog.dismiss();
                } else {
                    Toast.makeText(c, "Es wurden keine Zutaten angegeben", Toast.LENGTH_SHORT).show();
                }
                updateMixtures();
            }
        });

*/
        dialog.show();
    }

    private boolean isValidIngredient(String name, double percentage, double amount) {
        return name.length() > 3 && percentage >= 0 && percentage <= 98 && amount > 0 && amount <= 2000;
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
                addCustomMixture();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}