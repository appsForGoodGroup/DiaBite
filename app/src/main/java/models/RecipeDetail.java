package models;
 //imports
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

//We used this website to guide us: https://reintech.io/blog/building-recipe-app-android-spoonacular-api
public class RecipeDetail {
    @SerializedName("title")
    private String title;

    @SerializedName("id")
    private int id;


    @SerializedName("instructions")
    private String instructions;

    @SerializedName("nutrition")
    private Nutrition nutrition;

    /**
     * This returns the id of the recipe
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * This returns the nutrition class
     * @return nutrition instance
     */
    public Nutrition getNutrition() {
        return nutrition;
    }

    /**
     * This class contains nutritional info of a recipe
     */
    public static class Nutrition {
        @SerializedName("nutrients")
        private ArrayList<Nutrient> nutrients;

        /**
         * This returns an arraylist of the nutrients
         * @return nutrients a recipe has
         */
        public ArrayList<Nutrient> getNutrients() {
            return nutrients;
        }
    }

    public static class Nutrient {
        @SerializedName("name")
        private String name;

        @SerializedName("amount")
        private double amount;

        /**
         * @return the name of the nutrient (e.g., "Calories", "Protein")
         */
        public String getName() {
            return name;
        }

        /**
         * @return the amount of the nutrient
         */
        public double getAmount() {
            return amount;
        }
    }



    @SerializedName("extendedIngredients")
    private ArrayList<ExtendedIngredient> extendedIngredients;

    /**
     * @return the name of the recipe
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the instructions of the recipe
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * @return The list of ingredients
     */
    public ArrayList<ExtendedIngredient> getExtendedIngredients() {
        return extendedIngredients;
    }

    /**
     * @return instead of an arraylist, this returs ingredients as a string
     */
    public String getIngredients(){
        String listOfIngredients = "";
        ArrayList<ExtendedIngredient> ingredients  = getExtendedIngredients();
        for (int i=0; i<ingredients.size(); i++){
            listOfIngredients += ingredients.get(i).getOriginal() +"\n";
        }
        return listOfIngredients;
    }


    public static class ExtendedIngredient {
        @SerializedName("original")
        private String original;

        /**
         * @return the ingredients in their full form ("1 cup of flour")
         */
        public String getOriginal() {
            return original;
        }

        /**
         * Filters the name of the ingredient to remove units
         * @return a trimmed version of the ingredients
         */
        public String getName() {
            String original = this.getOriginal().toLowerCase();
            original = original.replaceAll("[0-9/]+", "");       // Remove numbers
            original = original.replaceAll("(cups?|tablespoons?|teaspoons?|tbsp|tsp|of)", ""); // Remove common units
            original = original.replaceAll("[^a-z ]", "");        // Remove punctuation
            return original.trim();
        }

    }
}

