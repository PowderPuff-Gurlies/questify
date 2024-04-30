package com.example.and101_capstone

//import com.codepath.asynchttpclient.AsyncHttpClient
//import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import android.icu.text.DateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.and101_capstone.databinding.FragmentQuestsBinding
import com.example.and101_capstone.ui.task.TaskData
import com.google.api.client.extensions.android.http.AndroidHttp.newCompatibleTransport
import com.google.api.client.json.jackson2.JacksonFactory.getDefaultInstance
import okhttp3.Headers
import java.text.SimpleDateFormat
import java.util.Date

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

        taskList = mutableListOf()
        fetch2()
        adapter = TaskAdapter(taskList)
        binding.taskList.adapter = adapter
        binding.taskList.layoutManager = LinearLayoutManager(requireContext())
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

    private fun fetch2() {
        val key = "AIzaSyBgWnJE3F6sQxCUgnqBY5vzuD1uKUyh6BE"
        val client = AsyncHttpClient()
        client.get("https://www.googleapis.com/calendar/v3/calendars/bdabeb04a42d706ce2de0a3dcf884b669d2410c63381b0fb6ac71fd4c9d72f1b@group.calendar.google.com/events?key=$key", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                val events = json.jsonObject.getJSONArray("items")
                taskList.clear() // Clear the task list before adding new items
                for (i in 0 until events.length()) {
                    val event = events.getJSONObject(i)
                    val title = event.getString("summary")
                    val end = event.getJSONObject("end").getString("dateTime")

                    val originalForm: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    val targetForm: SimpleDateFormat = SimpleDateFormat("MM/dd @ hh:mm aa")
                    val date: Date = originalForm.parse(end)
                    val formattedDate: String = targetForm.format(date)

                    val task = TaskData(
                        title ?: "",
                        formattedDate ?: "",
                        completed = false,
                        reward = 1
                    )
                    Log.d("task $i", "$task")
                    taskList.add(task)
                }
                setUpRecyclerView()
            }
            override fun onFailure(statusCode: Int,
                                   headers: Headers?,
                                   errorResponse: String,
                                   throwable: Throwable?) {
                Log.e("Task Error", errorResponse ?: "Unknown error")
            }
        })
    }



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
