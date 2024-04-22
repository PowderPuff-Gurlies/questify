package com.example.and101_capstone.ui.dashboard

import com.example.and101_capstone.ui.task.Task
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.and101_capstone.R

class TaskAdapter(private val taskList: List<Task>) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskTitle: TextView = view.findViewById(R.id.task_title)
        val taskDueDate: TextView = view.findViewById(R.id.task_dueDate)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTask = taskList[position]
        //Glide.with(holder.itemView)
        holder.taskTitle.text = currentTask.title
        holder.taskDueDate.text = currentTask.dueDate
    }
    override fun getItemCount() = taskList.size
}
