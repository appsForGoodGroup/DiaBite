package api;
import models.Recipe;
import models.RecipeDetail;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
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


    @GET("recipes/{id}/information")
    Call<RecipeDetail> getRecipeInformation(
            @Path("id") int id,
            @Query("apiKey") String apiKey
    );


}