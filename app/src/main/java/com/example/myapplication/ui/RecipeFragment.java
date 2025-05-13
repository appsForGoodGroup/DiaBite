package com.example.myapplication.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.myapplication.CallbackFunction;
import com.example.myapplication.R;

import java.util.ArrayList;

import api.ApiClient;
import api.SpoonacularApi;
import models.RecipeDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView breakfastTag, lunchTag, dinnerTag;

    private static int[] ids;

    ArrayList<String> meal1 = new ArrayList<>();
    ArrayList<String> meal2 = new ArrayList<>();
    ArrayList<String> meal3 = new ArrayList<>();

    public RecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeFragment newInstance(String param1, String param2) {
        RecipeFragment fragment = new RecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This was made with the same resources as the method in Calendar Fragment. It uses the api from Spoonacular to look up IDs of recipes and generate the instructions and ingredients.
     * @param id the id of the meal
     * @param callback A function to be called with the recipe details once they're available.
     */
    private void getRecipeDetails(int id, CallbackFunction callback) {
        String apiKey = "0e9415e3ce654eaabbb4559411e9904c";
        SpoonacularApi api = ApiClient.getApi();
        Call<RecipeDetail> call = api.getRecipeInformation(id, true,apiKey);
        call.enqueue(new Callback<RecipeDetail>() {
            @Override
            public void onFailure(@NonNull Call<RecipeDetail> call, @NonNull Throwable t) {
                Log.d("Debug", "Failed to get recipe details: " + t.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call<RecipeDetail> call, @NonNull Response<RecipeDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RecipeDetail details = response.body();
                    ArrayList<String> info = new ArrayList<>();
                    info.add(details.getTitle());
                    info.add(details.getIngredients());
                    info.add(details.getInstructions());
                    callback.onCallback(info);

                    if (call.request().url().toString().contains(String.valueOf(ids[0]))) {
                        meal1 = info;
                        if (breakfastTag != null) {

                            //https://abhiandroid.com/ui/html#gsc.tab=0
                            String text = "<b>Breakfast:</b><br>" +
                                    "<b>Title:</b> " + meal1.get(0) + "<br>" +
                                    "<b>Ingredients:</b><br>" + meal1.get(1).replaceAll("\n", "<br>") + "<br>" +
                                    "<b>Instructions:</b>" + meal1.get(2).replaceAll("\n", "<br>");
                            breakfastTag.setText(Html.fromHtml(text));

                        }
                    } else if (call.request().url().toString().contains(String.valueOf(ids[1]))) {
                        meal2 = info;
                        if (lunchTag != null) {
                            String text = "<b>Lunch:</b><br>" +
                                    "<b>Title:</b> " + meal2.get(0) + "<br>" +
                                    "<b>Ingredients:</b><br>" + meal2.get(1).replaceAll("\n", "<br>") + "<br>" +
                                    "<b>Instructions:</b>" + meal2.get(2).replaceAll("\n", "<br>");
                            lunchTag.setText(Html.fromHtml(text));
                        }
                    } else if (call.request().url().toString().contains(String.valueOf(ids[2]))) {
                        meal3 = info;
                        if (dinnerTag != null) {
                            String text = "<b>Dinner:</b><br>" +
                                    "<b>Title:</b> " + meal3.get(0) + "<br>" +
                                    "<b>Ingredients:</b><br>" + meal3.get(1).replaceAll("\n", "<br>") + "<br>" +
                                    "<b>Instructions:</b>" + meal3.get(2).replaceAll("\n", "<br>");
                            dinnerTag.setText(Html.fromHtml(text));
                        }
                    }
                }
            }

        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if (ids != null && ids.length >= 3) {
            getRecipeDetails(ids[0], result -> meal1 = result);
            getRecipeDetails(ids[1], result -> meal2 = result);
            getRecipeDetails(ids[2], result -> meal3 = result);
        }
        else {
            Log.e("RecipeFragment", "IDs are not set yet!");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        breakfastTag = view.findViewById(R.id.breakfast);
        lunchTag = view.findViewById(R.id.lunch);
        dinnerTag = view.findViewById(R.id.dinner);

        breakfastTag.setText("Loading breakfast...");
        lunchTag.setText("Loading lunch...");
        dinnerTag.setText("Loading dinner...");

        return view;
    }

    /**
     * This sets the ids of the meals that have been fetched by the calendar fragment
     * @param iDsOfMeals these are the IDs of the meals, retrieved from the calendar fragment
     */
    public static void setIDs(int[] iDsOfMeals){
        ids = iDsOfMeals;
    }
}