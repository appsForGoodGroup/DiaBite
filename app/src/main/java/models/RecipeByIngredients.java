package models;

public class RecipeByIngredients {
    private int id;
    private String title;
    private String image;
    private int usedIngredientCount;
    private int missedIngredientCount;

    // Add getters and setters
    public int getID() { return id; }
    public String getTitle() { return title; }
    public String getImage() { return image; }
    public int getUsedIngredientCount() { return usedIngredientCount; }
    public int getMissedIngredientCount() { return missedIngredientCount; }
}
