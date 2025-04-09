package com.example.myapplication;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private HashMap<String, String[]> mealPlan = new HashMap<String, String[]>() {{
        put("Monday", new String[]{"meal 1", "meal 2", "meal 3"});
        put("Tuesday", new String[]{"Meal 4", "Meal 5", "Meal 6"});
        put("Wednesday", new String[]{"meal 7", "meal 8", "meal 9"});
        put("Thursday", new String[]{"meal 12", "meal 11", "meal 10"});
        put("Friday", new String[]{"meal 13", "meal 14", "meal 15"});
        put("Saturday", new String[]{"meal 18", "meal 17", "meal 16"});
        put("Sunday", new String[]{"meal 19", "meal 20", "meal 21"});
    }};

    LinearLayout daysContainer;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
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
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        daysContainer = view.findViewById(R.id.daysContainer);
        View popupView = getLayoutInflater().inflate(R.layout.meal_plan, null);
        TextView titleTextView = popupView.findViewById(R.id.popupTitle);
        TextView mealsTextView = popupView.findViewById(R.id.popupMeals);
        Button closeButton = popupView.findViewById(R.id.closePopupButton);

        for (int i = 0; i <days.length; i++){
            Button dayButton = new Button(getContext());
            dayButton.setText(days[i]);
            dayButton.setAllCaps(false);
            dayButton.setPadding(24, 16, 24, 16);
            String[] meals = mealPlan.get(days[i]);
            dayButton.setOnClickListener(v -> {
            String message = "Breakfast: " + meals[0] + "\n" + "Lunch: " + meals[1] + "\n" + "Dinner: " + meals[2];
            mealsTextView.setText(message);

            PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.rgb(142, 176, 226)));
            popupWindow.setOutsideTouchable(true);

            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

            closeButton.setOnClickListener(view1 -> popupWindow.dismiss());
        });

            daysContainer.addView(dayButton);
        }
        return view;
    }
}