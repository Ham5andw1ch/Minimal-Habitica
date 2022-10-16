package com.example.habiticalist;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.habiticalist.Dailies.DailyWidget;
import com.example.habiticalist.ToDo.TodoWidget;
import com.example.habiticalist.ToDo.TodoWidgetService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Parcelable;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static ArrayList<Task> tasks = new ArrayList<Task>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        final GlobalVariables globalVariable = (GlobalVariables) getApplicationContext();
        TextInputLayout apiUser = findViewById(R.id.apiUser);
        TextInputLayout apiKey = findViewById(R.id.apiKey);
        TextInputLayout headerColor = findViewById(R.id.headerColor);
        TextInputLayout backgroundColor = findViewById(R.id.backgroundColor);
        TextInputLayout headerTextColor = findViewById(R.id.headerText);
        TextInputLayout listTextColor = findViewById(R.id.listText);

        apiUser.getEditText().getText().append(globalVariable.getApiUser());
        apiKey.getEditText().getText().append(globalVariable.getApiKey());
        headerColor.getEditText().getText().append(globalVariable.getHeaderColor());
        backgroundColor.getEditText().getText().append(globalVariable.getBackgroundColor());
        listTextColor.getEditText().getText().append(globalVariable.getListTextColor());
        headerTextColor.getEditText().getText().append(globalVariable.getHeaderTextColor());
        //Save the API Key and User
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout apiUser = findViewById(R.id.apiUser);
                TextInputLayout apiKey = findViewById(R.id.apiKey);
                TextInputLayout headerColor = findViewById(R.id.headerColor);
                TextInputLayout backgroundColor = findViewById(R.id.backgroundColor);
                TextInputLayout headerTextColor = findViewById(R.id.headerText);
                TextInputLayout listTextColor = findViewById(R.id.listText);
                final GlobalVariables globalVariable = (GlobalVariables) getApplicationContext();
                globalVariable.setKeys(apiUser.getEditText().getText().toString(),apiKey.getEditText().getText().toString());
                globalVariable.setColors(headerColor.getEditText().getText().toString(),backgroundColor.getEditText().getText().toString(),
                        headerTextColor.getEditText().getText().toString(),listTextColor.getEditText().getText().toString());
                updateMyWidgets(getApplicationContext());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
    public static void updateMyWidgets(Context context) {
        AppWidgetManager man = AppWidgetManager.getInstance(context);
        int[] ids = man.getAppWidgetIds(
                new ComponentName(context, TodoWidget.class));
        Intent updateIntent = new Intent();
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(TodoWidget.WIDGET_ID_KEY, ids);
        context.sendBroadcast(updateIntent);
        ids = man.getAppWidgetIds(
                new ComponentName(context, DailyWidget.class));
        updateIntent = new Intent();
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(DailyWidget.WIDGET_ID_KEY, ids);
        context.sendBroadcast(updateIntent);
    }
}
