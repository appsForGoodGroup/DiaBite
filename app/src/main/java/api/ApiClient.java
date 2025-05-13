package api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// We used this resource https://reintech.io/blog/building-recipe-app-android-spoonacular-api, and made some modifications. This class allows us to interact with the Spoonacular API.
public class ApiClient {
    private static Retrofit retrofit = null;

    /**
     * This allows us to access the API
     * @return an instance of spoonacular api that can be used to send requests
     */
    public static SpoonacularApi getApi() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.spoonacular.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(SpoonacularApi.class);
    }
}

