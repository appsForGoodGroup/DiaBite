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

    @SerializedName("ingredients")
    private String ingredients;


    /**
     * This method gives the title
     * @return this method returns the title of the recipe
     */
    public String getTitle() {
        return title;
    }

    /**
     * This returns the id of the recipe
     * @return the id of the recipe
     */
    public int getID() {
        return id;
    }
}

