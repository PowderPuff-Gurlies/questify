package com.example.and101_capstone

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment

class StatsFragment: Fragment() {

    private lateinit var healthProgressBar: ProgressBar
    private lateinit var experienceProgressBar: ProgressBar
    private lateinit var manaProgressBar: ProgressBar

    private lateinit var healthText: TextView
    private lateinit var experienceText: TextView
    private lateinit var manaText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.stats_layout, container, false)

        // Initialize views
        healthProgressBar = view.findViewById(R.id.healthProgressBar)
        experienceProgressBar = view.findViewById(R.id.experienceProgressBar)
        manaProgressBar = view.findViewById(R.id.manaProgressBar)

        healthText = view.findViewById(R.id.healthText)
        experienceText = view.findViewById(R.id.experienceText)
        manaText = view.findViewById(R.id.manaText)

        // Set progress values to TextViews
        setProgressText(healthProgressBar, healthText)
        setProgressText(experienceProgressBar, experienceText)
        setProgressText(manaProgressBar, manaText)

        return view
    }

    private fun setProgressText(progressBar: ProgressBar, textView: TextView) {
        val progress = progressBar.progress
        val max = progressBar.max
        textView.text = "$progress / $max"
        Log.d("This is text", textView.text as String)
    }
}
