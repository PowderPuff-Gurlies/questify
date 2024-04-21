package com.example.and101_capstone.ui.dashboard

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.and101_capstone.databinding.FragmentDashboardBinding

// In your activity or fragment
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.recyclerViewTasks
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Sample list of tasks (Replace with your actual data source)
        val tasks: List<Task> = listOf(
            Task("Task 1", false),
            Task("Task 2", true),
            Task("Task 3", false)
        )

        // Create an instance of TaskAdapter and set it to RecyclerView
        val adapter = TaskAdapter(tasks)
        recyclerView.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    data class Task(
        val title: String,
        val completed: Boolean
    )
}
