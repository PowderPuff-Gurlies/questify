package com.example.and101_capstone.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.and101_capstone.R
import okhttp3.internal.concurrent.Task

class TaskAdapter(private val tasks: List<Task>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_items, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.textTaskTitle.text = task.title
        holder.checkboxTask.isChecked = task.completed
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textTaskTitle: TextView = view.findViewById(R.id.text_task_title)
        val checkboxTask: CheckBox = view.findViewById(R.id.checkbox_task)
    }
}
