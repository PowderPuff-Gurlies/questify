package com.example.and101_capstone

//import com.codepath.asynchttpclient.AsyncHttpClient
//import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.and101_capstone.databinding.FragmentQuestsBinding
import com.example.and101_capstone.ui.task.TaskData
import com.google.api.client.extensions.android.http.AndroidHttp.newCompatibleTransport
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory.getDefaultInstance
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.Events
import java.util.Collections

class QuestsFragment : Fragment() {

    companion object {
        val HTTP_TRANSPORT = newCompatibleTransport()
        val JSON_FACTORY = getDefaultInstance()
    }

    private var _binding: FragmentQuestsBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskList: MutableList<TaskData>
    private lateinit var adapter: TaskAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentQuestsBinding.bind(view)

        taskList = mutableListOf()
        adapter = TaskAdapter(taskList)
        val recyclerView: RecyclerView = binding.taskList
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        fetchEventsFromGoogleCalendar()
    }

//    private fun findAllCheckBoxes(view: View): List<CheckBox> {
//        val checkBoxes = mutableListOf<CheckBox>()
//
//        if (view is CheckBox) {
//            checkBoxes.add(view)
//        } else if (view is ViewGroup) {
//            for (i in 0 until view.childCount) {
//                val childCheckBoxes = findAllCheckBoxes(view.getChildAt(i))
//                checkBoxes.addAll(childCheckBoxes)
//            }
//        }
//
//        return checkBoxes
//    }

    private fun fetchEventsFromGoogleCalendar() {
        Thread {
            try {
                val credential = GoogleAccountCredential.usingOAuth2(
                    requireContext(), Collections.singleton(CalendarScopes.CALENDAR_READONLY)
                )

                val service = Calendar.Builder(
                    QuestsFragment.HTTP_TRANSPORT,
                    QuestsFragment.JSON_FACTORY, credential)
                    .setApplicationName("Questify")
                    .build()

                val now = com.google.api.client.util.DateTime(System.currentTimeMillis())
                val events: Events = service.events().list("bdabeb04a42d706ce2de0a3dcf884b669d2410c63381b0fb6ac71fd4c9d72f1b@group.calendar.google.com")
                    .setMaxResults(15)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute()

                val newTaskList = mutableListOf<TaskData>()

                for (event in events.items) {
                    val task = TaskData(
                        event.summary,
                        event.start.dateTime.toStringRfc3339(),
                        completed = false,
                        reward = 1
                    )
                    newTaskList.add(task)
                    Log.d("Task Data", "Task: ${event.summary}, Start Time: ${event.start.dateTime.toStringRfc3339()}")
                }

                requireActivity().runOnUiThread {
                    taskList.addAll(newTaskList)
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e("Task Error", e.message ?: "Unknown error")
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
