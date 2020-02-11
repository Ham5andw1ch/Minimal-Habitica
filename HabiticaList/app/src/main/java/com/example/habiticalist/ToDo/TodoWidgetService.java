package com.example.habiticalist.ToDo;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.habiticalist.APICalls;
import com.example.habiticalist.GlobalVariables;
import com.example.habiticalist.R;
import com.example.habiticalist.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class TodoWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TodoRemoteViewsFactory(this.getApplicationContext(),intent);
    }
    class TodoRemoteViewsFactory implements RemoteViewsFactory {
        private Context context;
        private int appWidgetId;
        private String[] test = {"one", "two", "three"};
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
            if(tasks.get(position).getDate() != null) {
                rv.setTextViewText(R.id.listDueDateWidget, "Due: " + tasks.get(position).getDate());
            }else{
                rv.setTextViewText(R.id.listDueDateWidget, "");
            }
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

        protected void executeAfter(String result) {
            tasks.clear();
            try {
                JSONObject obj = new JSONObject(result);
                JSONArray array = obj.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    try {
                        if (array.getJSONObject(i).getString("type").equals("todo") && array.getJSONObject(i).getBoolean("completed") == false) {
                            Task t = new Task();
                            t.setName(array.getJSONObject(i).getString("text").trim());
                            t.setId(array.getJSONObject(i).getString("id"));
                            if(array.getJSONObject(i).has("date") && !array.getJSONObject(i).getString("date").equals("")){
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
                                formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                                String dateInString =array.getJSONObject(i).getString("date");
                                try {

                                    Date date = formatter.parse(dateInString.replaceAll("Z$", "+0000"));

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
                                    dateFormat.setTimeZone(TimeZone.getDefault());
                                    t.setDate(dateFormat.format(date));

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                //t.setDate(array.getJSONObject(i).getString("date"));
                            }else{
                            }
                            tasks.add(t);
                        }
                    } catch (JSONException e) {

                    }
                }

            } catch (Exception e) {
                Log.e("Error:", Log.getStackTraceString(e));
            }
            ranFromSelf = true;
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, TodoWidget.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
    }

    private class APITask extends AsyncTask<String, Void, String> {

            protected String doInBackground(String... args) {
                //TODO remove the case statement
                final GlobalVariables globalVariable = (GlobalVariables) context.getApplicationContext();
                String apiUser = globalVariable.getApiUser();
                String apiKey = globalVariable.getApiKey();
                switch (args[0]) {
                    case "refresh":
                        return APICalls.getTasks(apiUser,apiKey);
                    default:
                        return null;

                }

            }

            protected void onPostExecute(String feed) {
                if (feed != null) {
                    executeAfter(feed);
                }

                // TODO: check this.exception
                // TODO: do something with the fe
            }
        }
    }}
