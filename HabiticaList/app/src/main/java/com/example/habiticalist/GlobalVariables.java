/*
*   GlobalVariables.java
*
*   This class provides an application with access to various public variables. Its main function
*   right now is providing API Keys to various functions. It also handles the File IO for said
*   tasks.
*
*/

package com.example.habiticalist;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class GlobalVariables extends Application {
    private String apiUser;
    private String apiKey;

    //Write the keys to a file
    public void setKeys(String apiUser, String apiKey){
        try {
            FileOutputStream os = getApplicationContext().
                    openFileOutput("api.txt", Context.MODE_PRIVATE);
            PrintWriter writer = new PrintWriter(os);
            writer.print(apiUser + "\n");
            writer.print(apiKey);
            this.apiKey = apiKey;
            this.apiUser = apiUser;
            writer.close();
        }catch(Exception e){
            Log.e("Error:",Log.getStackTraceString(e));

        }
    }
    //Read the keys from the input file
    private void readKeys(){
        try {
            //Open api.txt
            FileInputStream fis = getApplicationContext().openFileInput("api.txt");

            //Use a scanner to get the keys
            Scanner sc = new Scanner(fis);
            if(sc.hasNext()) {
                apiUser = sc.nextLine();
            }
            if(sc.hasNext()) {
                apiKey = sc.nextLine();
            }
            sc.close();
        }
        catch(InputMismatchException e){
            Log.e("Error:",Log.getStackTraceString(e));
        }
        catch (FileNotFoundException e) {
            Log.e("Error:",Log.getStackTraceString(e));
        }

    }
    //Get the API Key
    public String getApiKey() {
        if(apiKey==null){
            readKeys();
        }
        return apiKey;
    }

    //Get the API User ID
    public String getApiUser() {
        if(apiUser==null){
            readKeys();
        }
        return apiUser;
    }
}
