package api;
import models.Recipe;
import models.SearchResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.ArrayList;

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


    /**
     * This will use searching by name
     * @param query - this is the recipe that the user wants to search
     * @param number this is the number of recipes in the request
     * @param apiKey - this is the api key for spoonacular
     * @return
     */
    @GET("recipes/complexSearch")
    Call<SearchResult> searchRecipes(
            @Query("apiKey") String apiKey,
            @Query("query") String query,
            @Query("number") int number
    );

}