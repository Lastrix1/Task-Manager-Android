package com.lautaro.taskmanager.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TaskStorage {

    private static final String PREF_NAME = "tasks_pref";
    private static final String KEY_TASKS = "tasks";

    public static void saveTasks(Context context, List<Task> tasks) {
        SharedPreferences prefs =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = gson.toJson(tasks);

        prefs.edit()
                .putString(KEY_TASKS, json)
                .apply();
    }

    public static List<Task> loadTasks(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        String json = prefs.getString(KEY_TASKS, null);

        if (json == null) {
            return new ArrayList<>();
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Task>>() {}.getType();

        return gson.fromJson(json, type);
    }
}
