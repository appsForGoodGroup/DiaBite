package com.example.myapplication;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.*;

import api.ApiClient;
import api.SpoonacularApi;
import models.Recipe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class CalendarFragment extends Fragment {

    private TextView tvMonthYear;
    private LinearLayout daysContainer;
    private LinearLayout recipesContainer;
    private LinearLayout mealPopup;
    private TextView popupDayTitle, popupMeals;

    private Calendar currentCalendar;
    private HashMap<String, String[]> savedRecipes = new HashMap<>();
    private static final HashMap<String, String[]> mealPlan = new HashMap<>();
    private static final HashMap<String, int[]> mealPlanIDs= new HashMap<>();

    private final String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    public CalendarFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Initialize views
        tvMonthYear = view.findViewById(R.id.tvMonthYear);
        daysContainer = view.findViewById(R.id.daysContainer);
        recipesContainer = view.findViewById(R.id.recipesContainer);
        mealPopup = view.findViewById(R.id.mealPopup);
        popupDayTitle = view.findViewById(R.id.popupDayTitle);
        popupMeals = view.findViewById(R.id.popupMeals);
        Button closePopupButton = view.findViewById(R.id.closePopupButton);
        Button btnPrevMonth = view.findViewById(R.id.btnPrevMonth);
        Button btnNextMonth = view.findViewById(R.id.btnNextMonth);

        // Initialize calendar
        currentCalendar = Calendar.getInstance();

        // Initialize data
        initializeRecipeData();
        getRecipeFromSpoonacular(45); //this will get recipes using a max sugar, currently it's 45

        // Setup UI
        updateMonthYear();
        setupDaysOfWeek();
        setupRecipes();

        btnPrevMonth.setOnClickListener(v -> navigateMonth(-1));
        btnNextMonth.setOnClickListener(v -> navigateMonth(1));
        closePopupButton.setOnClickListener(v -> mealPopup.setVisibility(View.GONE));

        return view;
    }

    private void initializeRecipeData() {
        savedRecipes.put("Recipe 1", new String[]{"Ingredient A", "Ingredient B"});
        savedRecipes.put("Recipe 2", new String[]{"Ingredient C", "Ingredient D"});
        savedRecipes.put("Recipe 3", new String[]{"Ingredient E", "Ingredient F"});
        savedRecipes.put("Recipe 4", new String[]{"Ingredient G", "Ingredient H"});
    }

    private String getTodayDayName() {
        Calendar today = Calendar.getInstance();
        int dayOfWeek = today.get(Calendar.DAY_OF_WEEK); // 1 (Sun) to 7 (Sat)
        return days[dayOfWeek - 1];
    }


    /**
     * This requests a recipe from spoonacular using a maximum sugar.
     * @param maxSugar The maximum sugar a person wants in their meal
     */
    private void getRecipeFromSpoonacular(int maxSugar) {
        if (!mealPlan.isEmpty()) {
            Log.d("Debug", "Meal plan already loaded. Skipping API call.");
            return;
        }
        String apiKey = "1180357e69a34e54b905d57dae60db01";
        SpoonacularApi api = ApiClient.getApi();

        for (String day : days) {
            Call<ArrayList<Recipe>> call = api.getRecipesBySugar(maxSugar, 10, apiKey);
            call.enqueue(new Callback<ArrayList<Recipe>>() { //https://stackoverflow.com/questions/32431525/using-call-enqueue-function-in-retrofit
                @Override
                public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable t) {
                    Log.d("Debug", "Failed to get recipes");
                }

                @Override
                public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ArrayList<Recipe> recipes = response.body();
                        Collections.shuffle(recipes); //https://www.geeksforgeeks.org/collections-shuffle-method-in-java-with-examples/
                        ArrayList<String> meals = new ArrayList<>();
                        ArrayList<Integer> mealIDs = new ArrayList<Integer>();
                        for (int i = 0; i < 3 && i < recipes.size(); i++) {
                            meals.add(recipes.get(i).getTitle());
                            mealIDs.add(recipes.get(i).getID());
                        }
                        mealPlan.put(day, meals.toArray(new String[0]));
                        mealPlanIDs.put(day, mealIDs.stream().mapToInt(Integer::intValue).toArray());
                        if (day.equals(getTodayDayName())) {
                            mealPlan.put(day, meals.toArray(new String[0]));
                            mealPlanIDs.put(day, mealIDs.stream().mapToInt(Integer::intValue).toArray());

                        }
                    } else {
                        mealPlan.put(day, new String[]{"Error", "Error", "Error"});
                    }
                }
            });
        }
    }

    /**
     * This allows you to navigate to a different month
     * @param months the month that will be navigated to
     */
    private void navigateMonth(int months) {
        currentCalendar.add(Calendar.MONTH, months);
        updateCalendar();
    }

    /**
     * This updates the month and the week
     */
    private void updateCalendar() {
        updateMonthYear();
        setupDaysOfWeek();
    }

    /**
     * This updates the month
     */
    private void updateMonthYear() {
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        tvMonthYear.setText(monthFormat.format(currentCalendar.getTime()));
    }

    /**
     * This adds a button to the container
     */
    private void setupDaysOfWeek() {
        daysContainer.removeAllViews();
        Calendar tempCalendar = (Calendar) currentCalendar.clone();
        tempCalendar.set(Calendar.DAY_OF_WEEK, tempCalendar.getFirstDayOfWeek());

        for (int i = 0; i < 7; i++) {
            Button dayButton = new Button(getContext());
            String dayName = days[i];

            styleDayButton(dayButton, dayName, tempCalendar);
            daysContainer.addView(dayButton);
            tempCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    /**
     * This styles the container of the days button.
     */
    private void styleDayButton(Button button, String dayName, Calendar calendar) {
        button.setText(dayName);
        button.setAllCaps(false);
        button.setPadding(32, 16, 32, 16);
        button.setTextSize(14);
        button.setTextColor(Color.BLACK);

        if (isToday(calendar)) {
            button.setBackgroundResource(R.drawable.today_button_bg);
        } else {
            button.setBackgroundResource(R.drawable.day_button_bg);
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(4, 0, 4, 0);
        button.setLayoutParams(params);

        button.setOnClickListener(v -> showMealsPopup(dayName, v));
    }

    /**
     * This checks to see if a button contains the same day at today
     * @param calendar The calendar the user is on
     * @return if this calendar contains the day we are on
     */
    private boolean isToday(Calendar calendar) {
        Calendar today = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * This sets up saved recipes for the future
     */
    private void setupRecipes() {
        recipesContainer.removeAllViews();

        for (String recipeName : savedRecipes.keySet()) {
            TextView recipeView = new TextView(getContext());
            recipeView.setText(recipeName);
            recipeView.setTextSize(16);
            recipeView.setPadding(16, 12, 16, 12);

            recipeView.setOnClickListener(v -> {
                String[] ingredients = savedRecipes.get(recipeName);
                showIngredientsPopup(recipeName, ingredients);
            });

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 8);
            recipeView.setLayoutParams(params);

            recipesContainer.addView(recipeView);
        }
    }

    /**
     * This makes a meals pop up with 3 meals per day
     * @param dayName this takes in the name of the day the user clicked on
     * @param view this is the view that triggered the pop up
     */
    private void showMealsPopup(String dayName, View view) { //https://stackoverflow.com/questions/5944987/how-to-create-a-popup-window-popupwindow-in-android
        String[] meals = mealPlan.get(dayName);
        int[] ids = mealPlanIDs.get(dayName);
        if (meals == null) {
            meals = new String[]{"Loading...", "Loading...", "Loading..."};
        }

        View popupView = getLayoutInflater().inflate(R.layout.meal_plan, null);
        TextView titleTextView = popupView.findViewById(R.id.popupTitle);
        TextView mealsTextView = popupView.findViewById(R.id.popupMeals);
        Button closeButton = popupView.findViewById(R.id.closePopupButton);
        String title;
        if(dayName.equals("Wed")){
            title = "Wednesday's Meals";
        } else if (dayName.equals("Thu")){
            title = "Thursday's Meals";
        } else if (dayName.equals("Sat")){
            title = "Saturday's Meals";
        }else if (dayName.equals("Tue")){
            title = "Tuesday's Meals";
        }else{
            title = dayName + "day's Meals";
        }
        titleTextView.setText(title);
        mealsTextView.setText("Breakfast: " + meals[0] + "\nLunch: " + meals[1] + "\nDinner: " + meals[2]);

        PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.rgb(142, 176, 226)));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 300);

        closeButton.setOnClickListener(v -> popupWindow.dismiss());

        //https://developer.android.com/guide/fragments/transactions
        int[] finalIds = ids;
        mealsTextView.setOnClickListener(v -> {
            RecipeFragment.setIDs(finalIds);
            if (getActivity() != null) {
                BottomNavigationView navBar = getActivity().findViewById(R.id.bottomNavigationView);
                navBar.setSelectedItemId(R.id.recipes);
            }

            popupWindow.dismiss();
        });
    }

    private void showIngredientsPopup(String recipeName, String[] ingredients) {
        popupDayTitle.setText(recipeName + " Ingredients");
        popupMeals.setText(String.join("\n", ingredients));
        mealPopup.setVisibility(View.VISIBLE);
    }
}