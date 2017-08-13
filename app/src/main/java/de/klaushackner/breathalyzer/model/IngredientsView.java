package de.klaushackner.breathalyzer.model;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class IngredientsView extends ListView {

    Ingredient[] ingredients;

    // Three constructors are required to inflate this extended view!

    public IngredientsView(Context context) {
        super(context);
    }

    public IngredientsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IngredientsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public Ingredient[] getIngredients() {
        if (ingredients == null) {
            return new Ingredient[0];
        }
        return ingredients;
    }

    public void removeIngredient(Ingredient ingredient) {
        if (ingredients == null) {
            return;
        }

        if (ingredients.length == 1 && ingredients[0].compareTo(ingredient)) {
            ingredients = null;
            return;
        }

        for (int i = 0; i < ingredients.length; i++) {
            if (ingredients[i] == ingredient) {
                ingredients[i] = null;
            }
        }

        boolean overTheNull = false;
        Ingredient[] newIngr = new Ingredient[ingredients.length - 1];
        for (int i = 0; i < ingredients.length; i++) {
            if (overTheNull && i + 1 < ingredients.length) {
                newIngr[i] = ingredients[i + 1];
            } else {
                if (ingredients[i] != null) {
                    newIngr[i] = ingredients[i];
                } else {
                    overTheNull = true;
                }
            }
        }
        ingredients = newIngr;
    }

    public void removeIngredient(int pos) {
        if (ingredients == null) {
            return;
        }

        if (ingredients.length == 1 && pos == 0) {
            ingredients = null;
            return;
        }

        if (pos >= ingredients.length) {
            return;
        }

        ingredients[pos] = null;

        boolean overTheNull = false;
        Ingredient[] newIngr = new Ingredient[ingredients.length - 1];
        for (int i = 0; i < ingredients.length; i++) {
            if (overTheNull && i + 1 < ingredients.length) {
                newIngr[i] = ingredients[i + 1];
            } else {
                if (ingredients[i] != null) {
                    newIngr[i] = ingredients[i];
                } else {
                    overTheNull = true;
                }
            }
        }
        ingredients = newIngr;
    }

    public void addIngredient(Ingredient ingredient) {
        if (ingredients == null) {
            ingredients = new Ingredient[1];
            ingredients[0] = ingredient;
            return;
        }

        Ingredient[] newIngr = new Ingredient[ingredients.length + 1];

        for (int i = 0; i < ingredients.length; i++) {
            newIngr[i] = ingredients[i];
        }

        newIngr[ingredients.length] = ingredient;
    }

    public Ingredient getIngredientById(int id) {
        if (ingredients == null) {
            return null;
        }
        if (id < ingredients.length - 1) {
            return ingredients[id];
        } else {
            return null;
        }
    }
}
