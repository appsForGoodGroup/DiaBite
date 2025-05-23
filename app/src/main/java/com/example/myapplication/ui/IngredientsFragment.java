package com.example.myapplication.ui;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.IngredientsDatabaseHelper;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IngredientsFragment#newInstance} factory method to
 * create an instance of this fragment.
 * This will display the ingredients and allow a user to add and remove ingredients to their inventories.
 */
public class IngredientsFragment extends Fragment {

    ListView listView;
    ArrayList<String> items;
    FloatingActionButton fab;
    ArrayAdapter<String> adapter;
    private TextView header;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private IngredientsDatabaseHelper dbHelper;

    public IngredientsFragment() {
        // Required empty public constructor
    }

    public static IngredientsFragment newInstance(String param1, String param2) {
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ingredients, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        header = view.findViewById(R.id.header);

        dbHelper = new IngredientsDatabaseHelper(requireContext());

        listView = view.findViewById(R.id.listview);
        fab = view.findViewById(R.id.fab);

        items = loadIngredientsFromDatabase();

        if(!items.isEmpty()){
        ViewGroup parentLayout = (ViewGroup) header.getParent();
        parentLayout.removeView(header);
        }
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        fab.setOnClickListener(v -> showAddIngredientPopUp());

        listView.setOnItemLongClickListener((adapterView, view1, position, id) -> {
            String toRemove = items.get(position);
            items.remove(position);
            removeIngredientFromDatabase(toRemove);
            adapter.notifyDataSetChanged();
            return true;
        });
    }

    /**
     * This builds a pop up that asks for an ingredients
     */
    private void showAddIngredientPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Ingredient");

        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String newIngredient = input.getText().toString().trim();
            if (!newIngredient.isEmpty()) {
                addIngredientToDatabase(newIngredient);
                items.add(newIngredient);
                adapter.notifyDataSetChanged();
                if (header.getParent() != null) {
                    ViewGroup parentLayout = (ViewGroup) header.getParent();
                    parentLayout.removeView(header);
                }
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    /**
     * This adds an ingredient to the database
     * @param ingredient the ingredient that was added from the pop up
     */
    public void addIngredientToDatabase(String ingredient){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IngredientsDatabaseHelper.COLUMN_NAME, ingredient);
        db.insert(IngredientsDatabaseHelper.TABLE_INGREDIENTS, null, values);
        db.close();
    }

    /**
     * This removes the ingredient from the database
     * @param ingredient the ingredient that will be removed from the database
     */
    public void removeIngredientFromDatabase(String ingredient){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(IngredientsDatabaseHelper.TABLE_INGREDIENTS, "name = ?", new String[]{ingredient});
        db.close();
    }

    /**
     * This will load all the ingredients that a user has into an arraylist
     * @return an arraylist full of the ingredients the user has in their inventory
     */
    private ArrayList<String> loadIngredientsFromDatabase() {
        ArrayList<String> ingredients = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                IngredientsDatabaseHelper.TABLE_INGREDIENTS,
                new String[]{IngredientsDatabaseHelper.COLUMN_NAME},
                null, null, null, null, null
        );

        while (cursor.moveToNext()) {
            ingredients.add(cursor.getString(0));
        }

        cursor.close();
        db.close();
        return ingredients;
    }

}
