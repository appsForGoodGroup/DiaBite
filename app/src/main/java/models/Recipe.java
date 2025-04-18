package models;
//imports
import com.google.gson.annotations.SerializedName;

//We used this website to guide us: https://reintech.io/blog/building-recipe-app-android-spoonacular-api
public class Recipe {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("image")
    private String imageUrl;

    public String getTitle() {
        return title;
    }
}

