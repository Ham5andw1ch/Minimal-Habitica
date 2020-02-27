/*
*   APICalls.java
*
*   This class provides static methods that interface with the Habitica API. These should be run
*   from an AsyncTask only.
*
*/
package com.example.habiticalist;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APICalls {

    /*
    This method will mark a task as completed, provided by id. It will then return 2 Users
    that represent the before and after states.
    */
    public static User[] checkTask(String id,String apiUser,String apiKey){
        try {
            //First, get user data from server
            URL url = new URL("https://habitica.com/api/v3/user");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("x-api-user", apiUser);
            urlConnection.setRequestProperty("x-api-key", apiKey);
            urlConnection.setRequestMethod("GET");

            //TODO use this variable to notify user if API User or Key are wrong
            String in = urlConnection.getResponseMessage();

            //Parse the output
            InputStream in1 = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in1));

            String result = "";
            String line;
            while ((line = reader.readLine())!= null){
                result += line;
            }

            //Pack the data into a user
            User u = new User();
            try{
                JSONObject obj = new JSONObject(result);
                JSONObject data = obj.getJSONObject("data").getJSONObject("stats");
                u.setGold(data.getDouble("gp"));
                u.setHealth(data.getDouble("hp"));
                u.setMana(data.getDouble("mp"));
                u.setXp(data.getDouble("exp"));

            } catch (Exception e){
                Log.e("Error:",Log.getStackTraceString(e));
            }
            urlConnection.disconnect();

            //Now start a new URL connection to score a task for the user
            url = new URL("https://habitica.com/api/v3/tasks/" + id + "/score/up/");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("x-api-user", apiUser);
            urlConnection.setRequestProperty("x-api-key", apiKey);
            urlConnection.setRequestMethod("POST");

            //TODO like before, this needs to also show output
            in = urlConnection.getResponseMessage();

            //Read the output
            in1 = new BufferedInputStream(urlConnection.getInputStream());
            reader = new BufferedReader(new InputStreamReader(in1));
            result = "";
            while ((line = reader.readLine())!= null){
                result += line;
            }

            //Pack output into a new user
            User u2 = new User();
            try{
                JSONObject obj = new JSONObject(result);
                JSONObject data = obj.getJSONObject("data");
                u2.setGold(data.getDouble("gp"));
                u2.setHealth(data.getDouble("hp"));
                u2.setMana(data.getDouble("mp"));
                u2.setXp(data.getDouble("exp"));

            } catch (Exception e){
                Log.e("Error:",Log.getStackTraceString(e));
            }
            urlConnection.disconnect();

            //Return both users for Toast generation
            return new User[]{u,u2};
        } catch (Exception e){
            Log.e("Error:",Log.getStackTraceString(e));
        }
        return null;

    }
    /*
    This method returns the JSON data of the tasks for the user. This can be parsed as needed.
     */
    public static String getTasks(String apiUser, String apiKey){
        try {
            //Open a url connection to get to the user's tasks
            URL url = new URL("https://habitica.com/api/v3/tasks/user/");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("x-api-user", apiUser);
            urlConnection.setRequestProperty("x-api-key", apiKey);
            urlConnection.setRequestMethod("GET");

            //TODO use this variable for API Key and User validation
            String in = urlConnection.getResponseMessage();

            //Assemble the output
            InputStream in1 = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in1));
            String result = "";
            String line;
            while ((line = reader.readLine())!= null){
                result += line;
            }
            urlConnection.disconnect();

            //return the json
            return result;
        } catch (Exception e){
            Log.e("Error:",Log.getStackTraceString(e));
        }
        return null;

    }

    /*
    This method returns the JSON data of the user. This can be parsed as needed.
     */
    public static String getUser(String apiUser, String apiKey){
        try {
            //Open a url connection to get to the user's tasks
            URL url = new URL("https://habitica.com/api/v3/user/");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("x-api-user", apiUser);
            urlConnection.setRequestProperty("x-api-key", apiKey);
            urlConnection.setRequestMethod("GET");

            //TODO use this variable for API Key and User validation
            String in = urlConnection.getResponseMessage();

            //Assemble the output
            InputStream in1 = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in1));
            String result = "";
            String line;
            while ((line = reader.readLine())!= null){
                result += line;
            }
            urlConnection.disconnect();

            //return the json
            return result;
        } catch (Exception e){
            Log.e("Error:",Log.getStackTraceString(e));
        }
        return null;

    }
}
