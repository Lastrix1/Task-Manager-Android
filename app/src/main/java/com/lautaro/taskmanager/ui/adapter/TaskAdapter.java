package com.lautaro.taskmanager.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.lautaro.taskmanager.R;
import com.lautaro.taskmanager.data.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private OnTaskLongClickListener longClickListener;

    private OnTaskClickListener listener;

    public TaskAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }
    public interface OnTaskClickListener {
        void onTaskStateChanged();
    }
    public TaskAdapter(List<Task> tasks, OnTaskLongClickListener listener) {
        this.tasks = tasks;
        this.longClickListener = listener;
    }





    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        holder.tvTitle.setText(task.getTitle());
        holder.tvDescription.setText(task.getDescription());

        if (task.isState()) {
            holder.tvTitle.setPaintFlags(
                    holder.tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );
            holder.tvTitle.setAlpha(0.5f);
        } else {
            holder.tvTitle.setPaintFlags(
                    holder.tvTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG)
            );
            holder.tvTitle.setAlpha(1f);
        }
    }




    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {



        TextView tvTitle;
        TextView tvDescription;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Task task = tasks.get(position);
                    task.setState(!task.isState());
                    notifyItemChanged(position);
                }
            });
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Task task = tasks.get(position);

                    if (longClickListener != null) {
                        longClickListener.onTaskLongClicked(task, position);
                    }
                }
                return true;
            });
        }



    }
    public interface OnTaskLongClickListener {
        void onTaskLongClicked(Task task, int position);
    }

}

