package com.example.and101_capstone.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.and101_capstone.R
import com.example.and101_capstone.ui.home.Task

class TaskAdapter(private val taskList: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = taskList[position]
        holder.textTaskTitle.text = currentTask.title
        holder.checkboxTask.isChecked = currentTask.completed
    }

    override fun getItemCount() = taskList.size

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textTaskTitle: TextView = view.findViewById(R.id.task_title)
        val checkboxTask: CheckBox = view.findViewById(R.id.checkBox)
    }
}

