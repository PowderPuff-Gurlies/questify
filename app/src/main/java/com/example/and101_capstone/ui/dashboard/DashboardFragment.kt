package com.example.and101_capstone.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.and101_capstone.databinding.FragmentDashboardBinding

// In your activity or fragment
//this file recieves the done tasks and places them in the doneTasks list
//this list then shows through the recycler view

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

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        //val recyclerView: RecyclerView = binding.recyclerViewTasks
        //recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Sample list of tasks (Replace with your actual data source)
//        val tasks: List<Task> = listOf(
//            Task("Task 1", false),
//            Task("Task 2", true),
//            Task("Task 3", false)
//        )

        // Create an instance of TaskAdapter.kt and set it to RecyclerView
        //val adapter = TaskAdapter.kt(tasks)
        //recyclerView.adapter = adapter

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
