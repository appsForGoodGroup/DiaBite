package com.example.myapplication;

import java.util.ArrayList;

public interface CallbackFunction {
   //ChatGPT helped us debug how to receive a call from Spoonacular
    /**
     * This method is called when the asynchronous task is complete and the result is ready. This can return data to the function that is calling for the information.
     * @param result An ArrayList of strings representing the data returned from the API.
     */
    void onCallback(ArrayList<String> result);
}

