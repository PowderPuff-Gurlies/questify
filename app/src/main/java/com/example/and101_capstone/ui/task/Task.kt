package com.example.and101_capstone.ui.task

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.and101_capstone.R
import com.example.and101_capstone.ui.home.HomeFragment

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

            // Replace the current activity with the HomeActivity
            val intent = Intent(this, HomeFragment::class.java)
            startActivity(intent)
        }

        saveButton.setOnClickListener {
            // Show a toast message when the button is clicked
            Toast.makeText(this, "Save button clicked", Toast.LENGTH_SHORT).show()

            // Replace the current activity with the HomeActivity
            val intent = Intent(this, HomeFragment::class.java)
            startActivity(intent)
        }
    }
}
