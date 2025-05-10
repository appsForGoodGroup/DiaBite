package api;
import models.Recipe;
import models.RecipeDetail;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import models.RecipeByIngredients;

import java.util.ArrayList;
import java.util.List;

// We used this website to guide us: https://reintech.io/blog/building-recipe-app-android-spoonacular-api
public interface SpoonacularApi {

    /**
     * This will search by nutrients and in this case we will use max sugar as our requirement
     * @param maxSugar - this is the max sugar a user wants in a meal
     * @param number - this is the number of recipes in the request
     * @param apiKey - this is our spoonacular API key
     * @return an arraylist of recipes that meet the sugar requirement
     */
    @GET("recipes/findByNutrients")
    Call<ArrayList<Recipe>> getRecipesBySugar(
            @Query("maxSugar") int maxSugar,
            @Query("number") int number,
            @Query("apiKey") String apiKey
    );

    /**
     * This will search recipes by ingredients
     * @param ingredients the ingredients that will be used
     * @param number the number of recipes
     * @param ranking maximizing used ingredients
     * @param apiKey the api key
     * @return an arraylist of recipes that work
     */
    @GET("recipes/findByIngredients")
    Call<List<RecipeByIngredients>> getRecipesByIngredients(
            @Query("ingredients") String ingredients,
            @Query("number") int number,
            @Query("ranking") int ranking, // 1 = maximize used ingredients, 2 = minimize missing ingredients
            @Query("apiKey") String apiKey
    );


    /**
     * This uses gives the nutrition information of a recipe
     * @param id the id of the recipe
     * @param includeNutrition if nutrition should be included
     * @param apiKey the api key
     * @return recipes with nutritional info
     */
    @GET("recipes/{id}/information")
    Call<RecipeDetail> getRecipeInformation(
            @Path("id") int id,
            @Query("includeNutrition") boolean includeNutrition,
            @Query("apiKey") String apiKey
    );

}