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
import androidx.viewpager.widget.ViewPager
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.and101_capstone.R
import com.example.and101_capstone.databinding.FragmentHomeBinding
import com.example.and101_capstone.ui.task.Task
import com.google.android.material.tabs.TabLayout
import okhttp3.internal.http2.Header
import org.json.JSONObject

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

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val addButton: Button = root.findViewById(R.id.add_button)

        addButton.setOnClickListener {
            // Show a toast message when the button is clicked
            Toast.makeText(requireContext(), "Add button clicked", Toast.LENGTH_SHORT).show()

            // Replace the current view with the new view
            val intent = Intent(requireContext(), Task::class.java)
            startActivity(intent)
        }
        getTasksFromGoogleCalendar()

        val viewPager = root.findViewById<ViewPager>(R.id.viewPager)
        viewPager.adapter = HomeTabAdapter(childFragmentManager)

        val tabLayout = root.findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)

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