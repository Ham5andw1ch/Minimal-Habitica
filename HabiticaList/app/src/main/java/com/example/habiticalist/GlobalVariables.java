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
import android.graphics.Color;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class GlobalVariables extends Application {
    private String apiUser;
    private String apiKey;
    private String headerColor;
    private String backgroundColor;
    private String headerTextColor;
    private String listTextColor;

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
    public void setColors(String headerColor,String backgroundColor,String headerTextColor,String listTextColor){
        try {
            FileOutputStream os = getApplicationContext().
                    openFileOutput("colors.txt", Context.MODE_PRIVATE);
            PrintWriter writer = new PrintWriter(os);
            writer.print(headerColor + "\n");
            writer.print(backgroundColor + "\n");
            writer.print(headerTextColor + "\n");
            writer.print(listTextColor);
            this.headerColor=headerColor;
            this.backgroundColor=backgroundColor;
            this.headerTextColor=headerTextColor;
            this.listTextColor=listTextColor;
            writer.close();
        }catch(Exception e){
            Log.e("Error:",Log.getStackTraceString(e));

        }
    }

    public void setHeaderColor(String headerColor) {
        this.headerColor = headerColor;
    }
    private void readColors(){
        try {
            //Open api.txt
            FileInputStream fis = getApplicationContext().openFileInput("colors.txt");

            //Use a scanner to get the keys
            Scanner sc = new Scanner(fis);
            headerColor = sc.nextLine();
            backgroundColor = sc.nextLine();
            headerTextColor = sc.nextLine();
            listTextColor = sc.nextLine();
            sc.close();
        }
        catch(InputMismatchException e){
            setColors("#00000000","#00000000","#00000000","#00000000");
            Log.e("Error:",Log.getStackTraceString(e));
        }
        catch(NoSuchElementException e){
            setColors("#00000000","#00000000","#00000000","#00000000");
            Log.e("Error:",Log.getStackTraceString(e));
        }
        catch (FileNotFoundException e) {
            setColors("#00000000","#00000000","#00000000","#00000000");
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
            apiUser = sc.nextLine();
            apiKey = sc.nextLine();
            sc.close();
        }
        catch(InputMismatchException e){
            Log.e("Error:",Log.getStackTraceString(e));
        }
        catch (FileNotFoundException e) {
            setKeys("","");
            Log.e("Error:",Log.getStackTraceString(e));
        }
        catch(NoSuchElementException e){
            setKeys("","");
            Log.e("Error:",Log.getStackTraceString(e));
        }

    }
    //Get the API Key
    public String getApiKey() {
        if(apiKey==null) {
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
    public String getHeaderColor(){
        if(headerColor==null) {
            readColors();
        }
        return headerColor;
    }
    public String getBackgroundColor(){
        if(backgroundColor==null) {
            readColors();
        }
        return backgroundColor;
    }
    public String getHeaderTextColor(){
        if(headerTextColor==null) {
            readColors();
        }
        return headerTextColor;
    }
    public String getListTextColor(){
        if(listTextColor==null) {
            readColors();
        }
        return listTextColor;
    }
}
