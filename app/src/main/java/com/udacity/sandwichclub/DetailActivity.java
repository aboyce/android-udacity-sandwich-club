package com.udacity.sandwichclub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.Arrays;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private ImageView mIngredientsImageView;
    private TextView mDescriptionTextView;
    private TextView mOriginTextView;
    private TextView mIngredientsTextView;
    private TextView mAlsoKnownAsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mIngredientsImageView = findViewById(R.id.image_iv);
        mDescriptionTextView = findViewById(R.id.description_tv);
        mOriginTextView = findViewById(R.id.origin_tv);
        mIngredientsTextView = findViewById(R.id.ingredients_tv);
        mAlsoKnownAsTextView = findViewById(R.id.also_known_tv);

        Intent intent = getIntent();
        if (intent == null) {
            Log.e(TAG, "Intent from getIntent() was null.");
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            Log.e(TAG, "EXTRA_POSITION: " + EXTRA_POSITION + " could not be found in the Intent.");
            closeOnError();
            return;
        }
        Log.d(TAG, "Located the position " + position + " from the Intent.");

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            Log.e(TAG, "Could not get the Sandwich object for position " + position + ".");
            closeOnError();
            return;
        }

        populateUI(sandwich);
        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        Picasso.with(this)
                .load(sandwich.getImage())
                .into(mIngredientsImageView);

        // If there is no value, leave it to default.
        if (sandwich.getPlaceOfOrigin() != null && !(sandwich.getPlaceOfOrigin().isEmpty())) {
            mOriginTextView.setText(sandwich.getPlaceOfOrigin());
        }

        if (sandwich.getAlsoKnownAs().size() == 0) {
            mAlsoKnownAsTextView.setText(R.string.detail_default_value);
        } else {
            for (String alsoKnownAs : sandwich.getAlsoKnownAs()) {
                mAlsoKnownAsTextView.append(" " + alsoKnownAs + " |");
                mAlsoKnownAsTextView.setMovementMethod(new ScrollingMovementMethod());
            }
        }

        if (sandwich.getIngredients().size() == 0) {
            mIngredientsTextView.setText(R.string.detail_default_value);
        } else {
            for (String ingredient : sandwich.getIngredients()) {
                mIngredientsTextView.append(" " + ingredient + " |");
            }
        }

        mDescriptionTextView.setText(sandwich.getDescription());
    }
}
