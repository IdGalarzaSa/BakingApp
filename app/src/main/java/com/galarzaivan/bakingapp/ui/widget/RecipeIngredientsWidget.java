package com.galarzaivan.bakingapp.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import com.galarzaivan.bakingapp.R;
import com.galarzaivan.bakingapp.classes.AppConstants;
import com.galarzaivan.bakingapp.models.Ingredient;
import com.galarzaivan.bakingapp.models.Recipe;
import com.google.gson.Gson;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientsWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredients_widget);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String data = sharedPreferences.getString(AppConstants.RECIPE_WIDGET, null);
        Recipe recipe = new Gson().fromJson(data, Recipe.class);
        views.setTextViewText(R.id.appwidget_text, recipe.getName());

        StringBuilder currentText = new StringBuilder();
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            Ingredient ingredient = recipe.getIngredients().get(i);
            String line = ingredient.getIngredient() + "  "
                    + ingredient.getQuantity() + "  "
                    + ingredient.getMeasure();
            currentText.append("\n").append("-").append(line);
        }
        views.setTextViewText(R.id.appwidget_ingredients, currentText.toString());


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

