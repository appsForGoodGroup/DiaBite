package com.example.myapplication;

import java.util.ArrayList;

public interface CallbackFunction {
   //ChatGPT helped us debug how to receive a call from Spoonacular
    void onCallback(ArrayList<String> result);
}

