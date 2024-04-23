package com.example.and101_capstone.ui.home

//api stuff
import android.content.Intent
import com.google.api.services.calendar.Calendar
import org.joda.time.DateTime
import com.google.api.services.calendar.model.Events
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Collections
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.*
import java.io.IOException
import java.util.Arrays
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.json.jackson2.JacksonFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
//import com.codepath.asynchttpclient.AsyncHttpClient
//import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.and101_capstone.R
import com.example.and101_capstone.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout
import org.json.JSONObject
import com.example.and101_capstone.ui.dashboard.TaskAdapter
import com.example.and101_capstone.ui.task.AddTask
import com.example.and101_capstone.ui.task.TaskData

//uses task_item.xml: has task_title and task_dueDate
//uses recycler view from fragment_home.xml: id is task_list
//PUT TO API using create event through the "Add Tasks" button

class HomeFragment : Fragment() {
    companion object {
        private val HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport()
        private val JSON_FACTORY = JacksonFactory.getDefaultInstance()
    }
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskList: MutableList<TaskData>
    private lateinit var rvTasks: RecyclerView
    private lateinit var adapter: TaskAdapter // Corrected here

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fetchEventsFromGoogleCalendar()
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

            //Add transition to the AddTask class
            val intent = Intent(requireContext(), AddTask::class.java)
            startActivity(intent)
        }
        //getTasksFromGoogleCalendar()

        val viewPager = root.findViewById<ViewPager>(R.id.viewPager)
        viewPager.adapter = HomeTabAdapter(childFragmentManager)

        val tabLayout = root.findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)

        return root
    }

    private fun fetchEventsFromGoogleCalendar() {
        // Create a new thread to fetch events
        Thread {
            try {
                // Set up Google Account Credential
                val credential = GoogleAccountCredential.usingOAuth2(
                    requireContext(), Collections.singleton(CalendarScopes.CALENDAR_READONLY)
                )
                // TODO: Set up credential with selected account

                // Set up Calendar service
                val service = Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName("Questify")
                    .build()

                // Fetch events
                val now = com.google.api.client.util.DateTime(System.currentTimeMillis())
                val events: Events = service.events().list("bdabeb04a42d706ce2de0a3dcf884b669d2410c63381b0fb6ac71fd4c9d72f1b@group.calendar.google.com")
                    .setMaxResults(10)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute()

                // Update UI on the main thread
                requireActivity().runOnUiThread {
                    for (event in events.items) {
                        val task = TaskData(
                            event.summary,
                            event.start.dateTime.toStringRfc3339(),
                            completed = false,
                            reward = 1
                        )
                        taskList.add(task)
                    }
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e("Task Error", e.message ?: "Unknown error")
            }
        }.start()
    }
}