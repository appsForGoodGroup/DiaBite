package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.*;

import api.ApiClient;
import api.SpoonacularApi;
import models.RecipeByIngredients;
import models.RecipeDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class CalendarFragment extends Fragment {

    private TextView tvMonthYear;
    private LinearLayout daysContainer;
    private LinearLayout recipesContainer;
    private LinearLayout mealPopup;

    private EditText sugarInput;
    private Button refresh;
    private Calendar currentCalendar;
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

        sugarInput = view.findViewById(R.id.SugarAmount);
        refresh = view.findViewById(R.id.Refresh);

        mealPopup = view.findViewById(R.id.mealPopup);
        Button closePopupButton = view.findViewById(R.id.closePopupButton);
        Button btnPrevMonth = view.findViewById(R.id.btnPrevMonth);
        Button btnNextMonth = view.findViewById(R.id.btnNextMonth);

        // Initialize calendar
        currentCalendar = Calendar.getInstance();

        // Initialize data
        getRecipeFromSpoonacular(45, false); //this will get recipes using a max sugar, currently it's 45

        // Setup UI
        updateMonthYear();
        setupDaysOfWeek();

        btnPrevMonth.setOnClickListener(v -> navigateMonth(-1));
        btnNextMonth.setOnClickListener(v -> navigateMonth(1));
        closePopupButton.setOnClickListener(v -> mealPopup.setVisibility(View.GONE));

        return view;
    }

    private String getTodayDayName() {
        Calendar today = Calendar.getInstance();
        int dayOfWeek = today.get(Calendar.DAY_OF_WEEK); // 1 (Sun) to 7 (Sat)
        return days[dayOfWeek - 1];
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText sugarInput = view.findViewById(R.id.SugarAmount);
        Button refreshButton = view.findViewById(R.id.Refresh);

        refreshButton.setOnClickListener(v -> {
            int sugarValue = Integer.parseInt(sugarInput.getText().toString().trim());
            getRecipeFromSpoonacular(sugarValue,true);
        });
    }



    /**
     * This requests a recipe from spoonacular using a maximum sugar.
     * @param maxSugar The maximum sugar a person wants in their meal
     * This was made using many resources and troubleshooting
     *                 https://spoonacular.com/food-api/docs#Get-Ingredient-Information
     *                 Chat GPT
     *                 https://reintech.io/blog/building-recipe-app-android-spoonacular-api
     *                 A LOT of messing around
     */
    private void getRecipeFromSpoonacular(int maxSugar, boolean refresh) {
        if(refresh){
            mealPlan.clear();
        }
        if (!mealPlan.isEmpty()) {
            Log.d("Debug", "Meal plan already loaded. Skipping API call.");
            return;
        }

        String apiKey = "b5a159e87f9e4cf991343818c1ccf6a8";
        SpoonacularApi api = ApiClient.getApi();
        HashSet<String> userIngredients = IngredientUtils.getUserIngredients(requireContext());

        Call<List<RecipeByIngredients>> call = api.getRecipesByIngredients(
                String.join(",", userIngredients),
                20,
                1,
                apiKey
        );

        call.enqueue(new Callback<List<RecipeByIngredients>>() {
            @Override
            public void onFailure(@NonNull Call<List<RecipeByIngredients>> call, @NonNull Throwable t) {
                Log.d("Debug", "Failed to get recipes");
            }

            @Override
            public void onResponse(@NonNull Call<List<RecipeByIngredients>> call, @NonNull Response<List<RecipeByIngredients>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.d("Debug", "Empty or failed recipe list");
                    return;
                }

                List<RecipeByIngredients> initialRecipes = response.body();
                List<RecipeDetail> validRecipes = Collections.synchronizedList(new ArrayList<>());
                Map<Integer, RecipeByIngredients> idToRecipe = new HashMap<>();

                for (RecipeByIngredients r : initialRecipes) {
                    idToRecipe.put(r.getID(), r);
                }

                final int[] remaining = {initialRecipes.size()};

                for (RecipeByIngredients recipe : initialRecipes) {
                    api.getRecipeInformation(recipe.getID(), true, apiKey).enqueue(new Callback<RecipeDetail>() {
                        @Override
                        public void onResponse(@NonNull Call<RecipeDetail> call, @NonNull Response<RecipeDetail> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                RecipeDetail detail = response.body();

                                Double sugarAmount = detail.getNutrition().getNutrients().stream()
                                        .filter(n -> "Sugar".equalsIgnoreCase(n.getName()))
                                        .map(RecipeDetail.Nutrient::getAmount)
                                        .findFirst()
                                        .orElse(null);

                                if (sugarAmount == null || sugarAmount > maxSugar) {
                                    finishOne();
                                    return;
                                }

                                int matchCount = 0; // matching with the user's inventory
                                for (RecipeDetail.ExtendedIngredient ing : detail.getExtendedIngredients()) {
                                    String ingName = ing.getName().toLowerCase();
                                    for (String userIng : userIngredients) {
                                        if (ingName.contains(userIng) || userIng.contains(ingName)) {
                                            matchCount++;
                                            break;
                                        }
                                    }
                                }

                                double matchRatio = (double) matchCount / detail.getExtendedIngredients().size();
                                if (matchRatio >= 0.4) {
                                    validRecipes.add(detail);
                                }
                            }
                            finishOne();
                        }

                        @Override
                        public void onFailure(@NonNull Call<RecipeDetail> call, @NonNull Throwable t) {
                            finishOne();
                        }

                        private void finishOne() {
                            remaining[0]--;
                            if (remaining[0] == 0) {
                                assignRecipesToDays(validRecipes);
                            }
                        }
                    });
                }
            }
        });
    }


    private void assignRecipesToDays(List<RecipeDetail> validRecipes) {
        Collections.shuffle(validRecipes);
        int index = 0;

        for (String day : days) {
            ArrayList<String> meals = new ArrayList<>();
            ArrayList<Integer> ids = new ArrayList<>();

            for (int i = 0; i < 3; i++) {
                if (index >= validRecipes.size()) break;

                RecipeDetail recipe = validRecipes.get(index++);
                meals.add(recipe.getTitle());
                ids.add(recipe.getId());
            }

            // Fill in blanks if needed
            while (meals.size() < 3) meals.add("No match");
            while (ids.size() < 3) ids.add(-1);

            mealPlan.put(day, meals.toArray(new String[0]));
            mealPlanIDs.put(day, ids.stream().mapToInt(Integer::intValue).toArray());
            if(day.equals(getTodayDayName())){
                RecipeFragment.setIDs(mealPlanIDs.get(day)); //sets the recipe page to be on today by default
            }
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
     * This makes a meals pop up with 3 meals per day
     * @param dayName this takes in the name of the day the user clicked on
     * @param view this is the view that triggered the pop up
     */
    private void showMealsPopup(String dayName, View view) { //https://stackoverflow.com/questions/5944987/how-to-create-a-popup-window-popupwindow-in-android
        String[] meals = mealPlan.get(dayName);
        int[] ids = mealPlanIDs.get(dayName);

        // Check if meals or ids are null or empty
        if (meals == null || meals.length == 0) {
            meals = new String[]{"No meals available", "No meals available", "No meals available"};
        }

        if (ids == null || ids.length < 3) {
            ids = new int[]{-1, -1, -1};  // Default fallback to invalid IDs
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
            Log.d("Button Click", "Button was clicked");
            try {
                // Your existing logic here
            } catch (Exception e) {
                Log.e("Button Error", "Error on button click", e);
            }

            RecipeFragment.setIDs(finalIds);
            if (getActivity() != null) {
                BottomNavigationView navBar = getActivity().findViewById(R.id.bottomNavigationView);
                navBar.setSelectedItemId(R.id.recipes);
            }

            popupWindow.dismiss();
        });
    }
}