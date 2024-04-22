package com.example.and101_capstone.ui.task

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.and101_capstone.R
import com.example.and101_capstone.ui.home.HomeFragment

data class TaskData (
    val title: String,
    val dueDate: String,
    val completed: Boolean, //whether task has been completed or not
    val reward: Int = 1     //this is the reward for completing the task, constant 1
)

class Task : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Hides the top bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_task)
        supportActionBar?.hide()

        val closeButton: Button = findViewById(R.id.close_button)
        val saveButton: Button = findViewById(R.id.save_button)

        closeButton.setOnClickListener {
            // Show a toast message when the button is clicked
            Toast.makeText(this, "Close button clicked", Toast.LENGTH_SHORT).show()

            // Replace the current fragment with the HomeFragment
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, HomeFragment())
            transaction.commit()
        }

        saveButton.setOnClickListener {
            // Show a toast message when the button is clicked
            Toast.makeText(this, "Save button clicked", Toast.LENGTH_SHORT).show()

            // Replace the current fragment with the HomeFragment
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, HomeFragment())
            transaction.commit()
        }
    }
}
