package com.example.and101_capstone.ui.home

//api stuff
import android.app.AlertDialog
import android.app.Dialog
//import com.codepath.asynchttpclient.AsyncHttpClient
//import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.and101_capstone.R
import com.example.and101_capstone.databinding.FragmentHomeBinding
import com.example.and101_capstone.ui.task.AddTask
import com.google.android.material.tabs.TabLayout

//uses task_item.xml: has task_title and task_dueDate
//uses recycler view from fragment_home.xml: id is task_list
//PUT TO API using create event through the "Add Tasks" button

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
//        binding.taskList.layoutManager = LinearLayoutManager(requireContext())
        val addButton: Button = root.findViewById(R.id.add_button)
        addButton.setOnClickListener {
            Toast.makeText(requireContext(), "Add button clicked", Toast.LENGTH_SHORT).show()
            //Add transition to the AddTask class
            val intent = Intent(requireContext(), AddTask::class.java)
            startActivity(intent)
        }
        val viewPager = root.findViewById<ViewPager>(R.id.viewPager)
        viewPager.adapter = HomeTabAdapter(childFragmentManager)
        val tabLayout = root.findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)

        val timerButton: Button = root.findViewById(R.id.timer_button)
        timerButton.setOnClickListener {
            createPopUp()
        }
        return root
    }

    private fun createPopUp() {
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.pomodoro_popup, null)

        // Create the dialog with the inflated view
        val dialog = Dialog(requireContext())
        dialog.setContentView(view)
        dialog.show()
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
                    .setMaxResults(15) //grab 15 events?
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
        return root
    }
}
