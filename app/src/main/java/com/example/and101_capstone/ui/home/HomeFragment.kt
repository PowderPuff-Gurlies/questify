package com.example.and101_capstone.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.and101_capstone.R
import com.example.and101_capstone.databinding.FragmentHomeBinding
import com.example.and101_capstone.ui.dashboard.TaskAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import org.json.JSONObject
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header

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
    // Initialize the RecyclerView
    private lateinit var taskList: MutableList<Task> // Change this line
    private lateinit var adapter: TaskAdapter // Declare adapter variable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.taskList.layoutManager = LinearLayoutManager(requireContext())
        taskList = mutableListOf()
        adapter = TaskAdapter(taskList)

        binding.taskList.adapter = adapter

        val addButton: Button = root.findViewById(R.id.add_button)
        addButton.setOnClickListener {
            Toast.makeText(requireContext(), "Add button clicked", Toast.LENGTH_SHORT).show()
        }
        getTasksFromGoogleCalendar()
        return root
    }

    private fun getTasksFromGoogleCalendar() {
        val client = AsyncHttpClient()
        for (i in 1..30) { // Fetch from API here
            val url = "google id for API call here"
            client.get(url, object : JsonHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<Header>?,
                    response: JSONObject?
                ) {
                    val title = response?.getString("task title")
                    val dueDate = response?.getJSONArray("task due date")
                    if (title != null && dueDate != null) {
                        val task = Task(
                            title,
                            dueDate="10/10",  //should be changed from API
                            completed = false,
                            reward = 1
                        )
                        taskList.add(task) // Add the task object to the list
                        activity?.runOnUiThread { // update UI
                            adapter.notifyDataSetChanged()
                        }
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>?,
                    throwable: Throwable,
                    errorResponse: JSONObject?
                ) {
                    Log.d("Task Error", throwable?.message ?: "Unknown error")
                }
            })
        }
    }
}

