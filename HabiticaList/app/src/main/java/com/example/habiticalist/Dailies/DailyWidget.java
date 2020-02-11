package com.example.habiticalist.Dailies;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.habiticalist.APICalls;
import com.example.habiticalist.GlobalVariables;
import com.example.habiticalist.R;

import com.example.habiticalist.User;

/**
 * Implementation of App Widget functionality.
 */
public class DailyWidget extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object

        RemoteViews toolbar = new RemoteViews(context.getPackageName(), R.layout.daily_widget);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, toolbar);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int i = 0; i < appWidgetIds.length; i++) {
            Intent intent = new Intent(context, DailyWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetIds[i]);
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.daily_widget);
            rv.setRemoteAdapter(R.id.widget_list, intent);

            rv.setEmptyView(R.id.widget_list,R.id.empty_view);

            //TODO rename. I got this from the android docs and left the name in place

            Intent toastIntent = new Intent(context, DailyWidget.class);
            // Set the action for the intent.
            // When the user touches a particular view, it will have the effect of
            // broadcasting TOAST_ACTION.

            toastIntent.setAction("delete");
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.widget_list, toastPendingIntent);

            //Refresh intent for the button
            Intent refreshIntent = new Intent(context, DailyWidget.class);
            refreshIntent.setAction("refresh");
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, 0, refreshIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtra("refresh",true);
            rv.setOnClickPendingIntent(R.id.refreshButton,refreshPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i],rv);
        }
        super.onUpdate(context,appWidgetManager,appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("delete")) {
            new APITask().execute(context, "score", intent.getStringExtra("id"));

        }else {
            //TODO this code will also run in the background. I don't want the refreshing message to show when not on homescreen.
            //int duration = Toast.LENGTH_SHORT;
            //Toast toast = Toast.makeText(context, "Refreshing", duration);
            //toast.show();
        }

        //Notify the appWidgets that their data has been updated
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), DailyWidget.class.getName());

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.widget_list);

        super.onReceive(context, intent);
    }

    private class APITask extends AsyncTask<Object,Void, User[]> {
        Context context = null;
        protected User[] doInBackground(Object... args){
            //Establish our context and global variables
            context = (Context)args[0];
            final GlobalVariables globalVariable = (GlobalVariables) context.getApplicationContext();
            String apiUser = globalVariable.getApiUser();
            String apiKey = globalVariable.getApiKey();
            //Based on the second argument, call a separate method from APICalls
            switch((String)args[1]){
                case "refresh":
                    APICalls.getTasks(apiUser,apiKey);
                    return null;
                case "score":
                    return APICalls.checkTask((String)args[2],apiUser,apiKey);
                default:
                    return null;

            }

        }
        protected void onPostExecute(User[] feed){
            //Check if values changed and use the context passed to make a toast
            if(feed[0] != null && feed[1] !=null){
                if(context != null){
                    CharSequence text = "";
                    if(feed[1].getGold()-feed[0].getGold() != 0){
                        text = text + "gp: " + String.format("%.2f",(feed[1].getGold()-feed[0].getGold())) + " ";
                    }
                    if(feed[1].getHealth()-feed[0].getHealth() != 0){
                        text = text + "hp: " + String.format("%.2f",(feed[1].getHealth()-feed[0].getHealth())) + " ";
                    }
                    if(feed[1].getMana()-feed[0].getMana() != 0){
                        text = text + "mp: " + String.format("%.2f",(feed[1].getMana()-feed[0].getMana())) + " ";
                    }
                    if(feed[1].getXp()-feed[0].getXp() != 0){
                        text = text + "exp: " +String.format("%.2f", (feed[1].getXp()-feed[0].getXp()));
                    }
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }
        }
    }
}


