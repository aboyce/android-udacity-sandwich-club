package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    private static final String KEY_NAME = "name";
    private static final String KEY_MAIN_NAME = "mainName";
    private static final String KEY_PLACE_OF_ORIGIN = "placeOfOrigin";
    private static final String KEY_ALSO_KNOWN_AS = "alsoKnownAs";
    private static final String KEY_INGREDIENTS = "ingredients";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";

    public static Sandwich parseSandwichJson(String json) {

        Log.d(TAG, "Entered parseSandwichJson");

        if (json == null) {
            String message = "The JSON provided is null, cannot parse.";
            Log.e(TAG, message);
            throw new IllegalArgumentException(message);
        }
        Log.v(TAG, "JSON data is: " + json);

        // The Sandwich as parsed JSON.
        JSONObject sandwichJson = null;

        // The separated components of the Sandwich.
        JSONObject sandwichNameJson = null;
        String sandwichName = null;
        List<String> sandwichAlsoKnownAs = null;
        String sandwichPlaceOfOrigin = null;
        String sandwichDescription = null;
        String sandwichImage = null;
        List<String> sandwichIngredients = null;

        try {
            sandwichJson = new JSONObject(json);

            // Get the separated components from the parent object.
            sandwichNameJson = sandwichJson.getJSONObject(KEY_NAME);
            sandwichName = sandwichNameJson.optString(KEY_MAIN_NAME);
            sandwichPlaceOfOrigin = sandwichJson.optString(KEY_PLACE_OF_ORIGIN);
            sandwichDescription = sandwichJson.optString(KEY_DESCRIPTION);
            sandwichImage = sandwichJson.optString(KEY_IMAGE);

            // Convert the 'Also Known As' JSON Array to an ArrayList for Sandwich.
            JSONArray sandwichAlsoKnownAsJson = sandwichNameJson.getJSONArray(KEY_ALSO_KNOWN_AS);
            if (sandwichAlsoKnownAsJson != null) {
                sandwichAlsoKnownAs = new ArrayList<>();
                for (int i = 0; i < sandwichAlsoKnownAsJson.length(); i++) {
                    sandwichAlsoKnownAs.add(sandwichAlsoKnownAsJson.optString(i));
                }
            }

            // Convert the 'Ingredients' JSON Array to an ArrayList for Sandwich.
            JSONArray sandwichIngredientsJson = sandwichJson.getJSONArray(KEY_INGREDIENTS);
            if (sandwichAlsoKnownAsJson != null) {
                sandwichIngredients = new ArrayList<>();
                for (int i = 0; i < sandwichIngredientsJson.length(); i++) {
                    sandwichIngredients.add(sandwichIngredientsJson.optString(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Could not parse the JSON '" + json + "', reason: '" + e.getMessage() + "'.");
            return null;
        }

        Log.d(TAG, "Parsed the JSON successfully");

        return new Sandwich(sandwichName, sandwichAlsoKnownAs, sandwichPlaceOfOrigin, sandwichDescription, sandwichImage, sandwichIngredients);
    }
}
