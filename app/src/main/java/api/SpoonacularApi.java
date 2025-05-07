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
     * @return
     */
    @GET("recipes/findByNutrients")
    Call<ArrayList<Recipe>> getRecipesBySugar(
            @Query("maxSugar") int maxSugar,
            @Query("number") int number,
            @Query("apiKey") String apiKey
    );

    @GET("recipes/findByIngredients")
    Call<List<RecipeByIngredients>> getRecipesByIngredients(
            @Query("ingredients") String ingredients,
            @Query("number") int number,
            @Query("ranking") int ranking, // 1 = maximize used ingredients, 2 = minimize missing ingredients
            @Query("apiKey") String apiKey
    );



    @GET("recipes/{id}/information")
    Call<RecipeDetail> getRecipeInformation(
            @Path("id") int id,
            @Query("apiKey") String apiKey
    );
}