package com.example.habiticalist.Dailies;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.habiticalist.APICalls;
import com.example.habiticalist.GlobalVariables;
import com.example.habiticalist.R;
import com.example.habiticalist.Task;


public class DailyWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TodoRemoteViewsFactory(this.getApplicationContext(),intent);
    }
    class TodoRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private Context context;
        private int appWidgetId;
        private String[] test = {"one", "two", "three"};
        private HashMap<String,Task> taskID = new HashMap<String,Task>();
        private ArrayList<Task> tasks = new ArrayList<Task>();
        private boolean ranFromSelf = false;


        public TodoRemoteViewsFactory(Context context, Intent intent) {
            this.context = context;
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        }

        @Override
        public void onCreate() {
            ranFromSelf = true;
            new APITask().execute("refresh");
        }

        @Override
        public void onDataSetChanged() {
            if(!ranFromSelf) {
                new APITask().execute("refresh");
            }
            ranFromSelf = false;

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return tasks.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.todo_layout_widget);
            rv.setTextViewText(R.id.listTitleWidget, tasks.get(position).getName());
            rv.setTextViewText(R.id.listDueDateWidget, tasks.get(position).getDate());
            Intent fillInIntent = new Intent();
            fillInIntent.putExtra("delete",true);
            fillInIntent.putExtra("id",tasks.get(position).getId());
            rv.setOnClickFillInIntent(R.id.listTodoItem,fillInIntent);
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        protected void executeAfter(String[] result) {
            tasks.clear();
            taskID.clear();
            try {
                JSONObject obj = new JSONObject(result[0]);
                JSONArray array = obj.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    try {
                        if (array.getJSONObject(i).getBoolean("isDue") == true && array.getJSONObject(i).getBoolean("completed") == false) {
                            Task t = new Task();
                            t.setName(array.getJSONObject(i).getString("text").trim());
                            t.setDate(">> " + array.getJSONObject(i).getInt("streak"));
                            t.setId(array.getJSONObject(i).getString("id"));
                            taskID.put(t.getId(),t);
                        }
                    } catch (JSONException e) {

                    }
                }
                Log.d("Test","Alive");
                JSONObject orderString = new JSONObject(result[1]);
                JSONArray dailys = orderString.getJSONObject("data").getJSONObject("tasksOrder").getJSONArray("dailys");
                for(int i = 0; i < dailys.length(); i++){
                    try{
                        if(taskID.get(dailys.getString(i)) != null) {
                            Log.d("Test",taskID.get(dailys.getString(i)).getName() + " " + i);
                            tasks.add(taskID.get(dailys.getString(i)));
                        }
                    }
                    catch (JSONException e){

                    }
                }

            } catch (Exception e) {
                Log.e("Error:", Log.getStackTraceString(e));
            }
            ranFromSelf = true;
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, DailyWidget.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
        }

        private class APITask extends AsyncTask<String, Void, String[]> {
            protected String[] doInBackground(String... args) {
                //TODO remove the case statement
                final GlobalVariables globalVariable = (GlobalVariables) context.getApplicationContext();
                String apiUser = globalVariable.getApiUser();
                String apiKey = globalVariable.getApiKey();
                switch (args[0]) {
                    case "refresh":
                        return new String[]{APICalls.getTasks(apiUser,apiKey),APICalls.getUser(apiUser,apiKey)};
                    default:
                        return null;

                }

            }


            protected void onPostExecute(String[] feed) {
                if (feed != null) {
                    executeAfter(feed);
                }

                // TODO: check this.exception
                // TODO: do something with the fe
            }
        }
    }}
