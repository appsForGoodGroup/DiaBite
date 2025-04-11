package com.example.myapplication;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import api.ApiClient;
import api.SpoonacularApi;
import models.Recipe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarFragment extends Fragment {

    private final String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private final HashMap<String, String[]> mealPlan = new HashMap<>();
    LinearLayout daysContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        daysContainer = view.findViewById(R.id.daysContainer);

        for (String day : days) { //this will make the buttons
            Button dayButton = new Button(getContext());
            dayButton.setText(day);
            dayButton.setAllCaps(false);
            dayButton.setPadding(24, 16, 24, 16);
            dayButton.setOnClickListener(v -> {
                makePopUp(day, mealPlan.get(day), v);
            });
            daysContainer.addView(dayButton);
        }

        getRecipeFromSpoonacular(45);

        return view;
    }

    private void makePopUp(String title, String[] meals, View view) { //https://stackoverflow.com/questions/5944987/how-to-create-a-popup-window-popupwindow-in-android
        View popupView = getLayoutInflater().inflate(R.layout.meal_plan, null);
        TextView titleTextView = popupView.findViewById(R.id.popupTitle);
        TextView mealsTextView = popupView.findViewById(R.id.popupMeals);
        Button closeButton = popupView.findViewById(R.id.closePopupButton);

        titleTextView.setText(title);
        mealsTextView.setText("Breakfast: " + meals[0] + "\nLunch: " + meals[1] + "\nDinner: " + meals[2]);

        PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.rgb(142, 176, 226)));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        closeButton.setOnClickListener(v -> popupWindow.dismiss());
    }

    private void getRecipeFromSpoonacular(int maxSugar) {
        String apiKey = "b5a159e87f9e4cf991343818c1ccf6a8";
        SpoonacularApi api = ApiClient.getApi();

        for (String day : days) {
            Call<ArrayList<Recipe>> call = api.getRecipesBySugar(maxSugar, 10, apiKey);
            call.enqueue(new Callback<ArrayList<Recipe>>() { //https://stackoverflow.com/questions/32431525/using-call-enqueue-function-in-retrofit
                @Override
                public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable t) {
                    Log.d("Debug", "Failed to get recipes");
                } //This method was suggested by the ide, so i made it log an error statement

                @Override
                public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) { // this method gets the response from the api and then sets up the meal plan for each day
                    if (response.isSuccessful() && response.body() != null) {
                        ArrayList<Recipe> recipes = response.body();
                        Collections.shuffle(recipes); //https://www.geeksforgeeks.org/collections-shuffle-method-in-java-with-examples/
                        ArrayList<String> meals = new ArrayList<>();
                        for (int i = 0; i < 3 && i < recipes.size(); i++) {
                            meals.add(recipes.get(i).getTitle());
                        }
                        mealPlan.put(day, meals.toArray(new String[meals.size()]));

                    } else {
                        mealPlan.put(day, new String[]{"Error", "Error", "Error"});
                    }
                }
            });
        }
    }
}