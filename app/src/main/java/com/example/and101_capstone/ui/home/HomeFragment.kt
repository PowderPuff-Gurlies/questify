package com.example.and101_capstone.ui.home

import HomeTabAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.and101_capstone.R
import com.example.and101_capstone.databinding.FragmentHomeBinding
import com.example.and101_capstone.ui.task.Task
import com.google.android.material.tabs.TabLayout
import org.json.JSONObject
import com.example.and101_capstone.ui.dashboard.TaskAdapter

//uses task_item.xml: has task_title and task_dueDate
//uses recycler view from fragment_home.xml: id is task_list
//PUT TO API using create event through the "Add Tasks" button

data class Task(
    val title: String,
    val dueDate: String,
    val completed: Boolean, //whether task has been completed or not
    val reward: Int = 1     //this is the reward for completing the task, constant 1
)

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskList: MutableList<Task>
    private lateinit var rvTasks: RecyclerView
    private lateinit var adapter: TaskAdapter // Corrected here

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        rvTasks = root.findViewById(R.id.task_list)
        taskList = mutableListOf()
        adapter = TaskAdapter(taskList)

        binding.taskList.adapter = adapter
        binding.taskList.layoutManager = LinearLayoutManager(requireContext())

        val addButton: Button = root.findViewById(R.id.add_button)
        addButton.setOnClickListener {
            Toast.makeText(requireContext(), "Add button clicked", Toast.LENGTH_SHORT).show()
        }
        //getTasksFromGoogleCalendar()

        val viewPager = root.findViewById<ViewPager>(R.id.viewPager)
        viewPager.adapter = HomeTabAdapter(childFragmentManager)

        val tabLayout = root.findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)

        return root
    }

    private fun getTasksFromGoogleCalendar() {
        val client = AsyncHttpClient()
        val url = "google id for API call here"
        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Headers?,
                response: JSONObject?
            ) {
                val events = response?.getJSONArray("events") // Replace "events" with the actual key
                for (i in 0 until events?.length()) {
                    val event = events?.getJSONObject(i)
                    val title = event?.getString("summary") // Replace "summary" with the actual key
                    val dueDate = event?.getString("start") // Replace "start" with the actual key
                    if (title != null && dueDate != null) {
                        val task = Task(
                            title,
                            dueDate,
                            completed = false,
                            reward = 1
                        )
                        taskList.add(task)
                        activity?.runOnUiThread {
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                throwable: Throwable,
                errorResponse: JSONObject?
            ) {
                Log.d("Task Error", throwable?.message ?: "Unknown error")
            }
        })

})
}
}