package com.example.and101_capstone

//import com.codepath.asynchttpclient.AsyncHttpClient
//import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.and101_capstone.databinding.FragmentQuestsBinding
import com.example.and101_capstone.ui.task.TaskData
import com.google.api.client.extensions.android.http.AndroidHttp.newCompatibleTransport
import com.google.api.client.json.jackson2.JacksonFactory.getDefaultInstance

class QuestsFragment : Fragment() {

    companion object {
        val HTTP_TRANSPORT = newCompatibleTransport()
        val JSON_FACTORY = getDefaultInstance()
    }

    private var _binding: FragmentQuestsBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskList: MutableList<TaskData>
    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//         Create 5 dummy tasks
        val task1 = TaskData("Study for Algorithms Exam", "1PM", false)
        val task2 = TaskData("Study for Math Final", "3PM", false)
        val task3 = TaskData("Buy Groceries", "10AM", false)
        val task4 = TaskData("Prepare for Presentation", "2PM", false)
        val task5 = TaskData("Go for a Run", "6AM", false)
        val task6 = TaskData("Read Chapter 5 of Novel", "7PM", false)
        val task7 = TaskData("Attend Yoga Class", "9AM", false)
        val task8 = TaskData("Write Article for Blog", "12PM", false)
        // Add the tasks to the list
        taskList = mutableListOf(task1, task2, task3, task4, task5, task6, task7, task8)

        adapter = TaskAdapter(taskList)
        binding.taskList.adapter = adapter
        binding.taskList.layoutManager = LinearLayoutManager(requireContext())

        // You can uncomment and call fetchEventsFromGoogleCalendar() here if needed
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

//    private fun fetch2() {
//        val client = AsyncHttpClient()
//        client.get("https://www.googleapis.com/calendar/v3/calendars/bdabeb04a42d706ce2de0a3dcf884b669d2410c63381b0fb6ac71fd4c9d72f1b@group.calendar.google.com/events?key=YOUR_API_KEY", object : JsonHttpResponseHandler() {
//            override fun onSuccess(p0: Int, p1: Array<Header>?, p2: JSONObject?) {
//                super.onSuccess(p0, p1, p2)
//
//                val events = p2?.getJSONArray("items")
//                taskList.clear() // Clear the task list before adding new items
//                for (i in 0 until events?.length() ?: 0) {
//                    val event = events?.getJSONObject(i)
//                    val organizer = event?.getJSONObject("organizer")
//                    val end = event?.getJSONObject("end")
//                    val task = TaskData(
//                        organizer?.getString("displayName") ?: "",
//                        end?.getString("dateTime") ?: "",
//                        completed = false,
//                        reward = 1
//                    )
//                    taskList.add(task)
//                }
//                // Set up the RecyclerView and adapter after fetching tasks
//                setUpRecyclerView()
//            }
//
//            override fun onFailure(
//                statusCode: Int,
//                p1: Array<Header>?,
//                errorResponse: String?,
//                throwable: Throwable?
//            ) {
//                super.onFailure(statusCode, p1, errorResponse, throwable)
//                Log.e("Task Error", errorResponse ?: "Unknown error")
//            }
//
//            override fun onSuccess(statusCode: Int, p1: Array<Header>, p2: ByteArray?) {
//                super.onSuccess(statusCode, p1, p2)
//                // This method is not implemented because we're only interested in onSuccess with JSON response
//            }
//        })
//    }



    private fun setUpRecyclerView() {
        adapter = TaskAdapter(taskList)
        binding.taskList.adapter = adapter
        binding.taskList.layoutManager = LinearLayoutManager(requireContext())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
