package models;
 //imports
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

//We used this website to guide us: https://reintech.io/blog/building-recipe-app-android-spoonacular-api
public class RecipeDetail {
    @SerializedName("title")
    private String title;

    @SerializedName("instructions")
    private String instructions;

    @SerializedName("extendedIngredients")
    private ArrayList<ExtendedIngredient> extendedIngredients;

    public String getTitle() {
        return title;
    }

    public String getInstructions() {
        return instructions;
    }

    public ArrayList<ExtendedIngredient> getExtendedIngredients() {
        return extendedIngredients;
    }



    public static class ExtendedIngredient {
        @SerializedName("original")
        private String original;

        public String getOriginal() {
            return original;
        }
    }
}

