package models;


//This is a model that we used to obtain information about a recipe from Spoonacular
public class RecipeByIngredients {
    private int id;
    private String title;
    private String image;
    private int usedIngredientCount;
    private int missedIngredientCount;

    // Add getters and setters

    /**
     * This returns the id of a recipe
     * @return the id
     */
    public int getID() { return id; }

    /**
     * Returns the title of a recipe
     * @return the title
     */
    public String getTitle() { return title; }

    /**
     * This can return an image
     * @return the image
     */
    public String getImage() { return image; }

    /**
     * The ingredients that we do have
     * @return  the amount of ingredients we do have
     */
    public int getUsedIngredientCount() { return usedIngredientCount; }

    /**
     * The ingredients we don't have
     * @return the amount of ingredients missing
     */
    public int getMissedIngredientCount() { return missedIngredientCount; }
}
