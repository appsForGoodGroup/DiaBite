package api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// We used this resource https://reintech.io/blog/building-recipe-app-android-spoonacular-api, and made some modifications
public class ApiClient {
    private static Retrofit retrofit = null;

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

