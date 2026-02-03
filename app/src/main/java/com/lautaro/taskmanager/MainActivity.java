package com.lautaro.taskmanager;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.lautaro.taskmanager.ui.adapter.TaskAdapter;
import com.lautaro.taskmanager.data.TaskStorage;
import com.google.android.material.snackbar.Snackbar;





import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lautaro.taskmanager.data.Task;
import com.lautaro.taskmanager.ui.dialog.AddTaskDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements AddTaskDialogFragment.OnTaskSavedListener {

    private List<Task> tasks = new ArrayList<>();
    private RecyclerView rvTasks;

    private TaskAdapter taskAdapter;
    private Task deletedTask;
    private int deletedTaskPosition;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        tasks = TaskStorage.loadTasks(this);
        rvTasks = findViewById(R.id.rvTasks);
        taskAdapter = new TaskAdapter(tasks, (task, position) -> {

            AddTaskDialogFragment dialog =
                    AddTaskDialogFragment.newInstance(
                            task.getTitle(),
                            task.getDescription(),
                            position
                    );

            dialog.show(getSupportFragmentManager(), "EditTaskDialog");

        });



        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        rvTasks.setAdapter(taskAdapter);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    private final Paint paint = new Paint();

                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        deletedTaskPosition = viewHolder.getAdapterPosition();
                        deletedTask = tasks.get(deletedTaskPosition);


                        tasks.remove(deletedTaskPosition);
                        taskAdapter.notifyItemRemoved(deletedTaskPosition);


                        Snackbar.make(
                                rvTasks,
                                "Tarea eliminada",
                                Snackbar.LENGTH_LONG
                        ).setAction("DESHACER", v -> {

                            tasks.add(deletedTaskPosition, deletedTask);
                            taskAdapter.notifyItemInserted(deletedTaskPosition);
                        }).addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {

                                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                                    TaskStorage.saveTasks(MainActivity.this, tasks);
                                }
                            }
                        }).show();
                    }


                    @Override
                    public void onChildDraw(@NonNull Canvas c,
                                            @NonNull RecyclerView recyclerView,
                                            @NonNull RecyclerView.ViewHolder viewHolder,
                                            float dX, float dY,
                                            int actionState,
                                            boolean isCurrentlyActive) {

                        View itemView = viewHolder.itemView;
                        int iconMargin = (itemView.getHeight() - 64) / 2;

                        Drawable icon = ContextCompat.getDrawable(
                                MainActivity.this,
                                R.drawable.ic_delete
                        );


                        paint.setColor(Color.RED);

                        if (dX > 0) {
                            c.drawRect(
                                    itemView.getLeft(),
                                    itemView.getTop(),
                                    itemView.getLeft() + dX,
                                    itemView.getBottom(),
                                    paint
                            );

                            if (icon != null) {
                                int iconTop = itemView.getTop() + iconMargin;
                                int iconBottom = iconTop + 64;
                                int iconLeft = itemView.getLeft() + iconMargin;
                                int iconRight = iconLeft + 64;

                                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                                icon.draw(c);
                            }

                        } else if (dX < 0) {
                            c.drawRect(
                                    itemView.getRight() + dX,
                                    itemView.getTop(),
                                    itemView.getRight(),
                                    itemView.getBottom(),
                                    paint
                            );

                            if (icon != null) {
                                int iconTop = itemView.getTop() + iconMargin;
                                int iconBottom = iconTop + 64;
                                int iconRight = itemView.getRight() - iconMargin;
                                int iconLeft = iconRight - 64;

                                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                                icon.draw(c);
                            }
                        }

                        super.onChildDraw(
                                c, recyclerView, viewHolder,
                                dX, dY, actionState, isCurrentlyActive
                        );
                    }
                };

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvTasks);




        FloatingActionButton btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(view -> {

            AddTaskDialogFragment dialog =
                    AddTaskDialogFragment.newInstance("", "", -1);

            dialog.show(getSupportFragmentManager(), "AddTaskDialog");

        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
            );
            return insets;
        });
    }




    @Override
    public void onTaskSaved(String title, String description, int position) {

        if (position == -1) {

            tasks.add(new Task(title, description));
            taskAdapter.notifyItemInserted(tasks.size() - 1);

        } else {

            Task task = tasks.get(position);
            task.setTitle(title);
            task.setDescription(description);

            taskAdapter.notifyItemChanged(position);
        }


        TaskStorage.saveTasks(this, tasks);
    }




}
