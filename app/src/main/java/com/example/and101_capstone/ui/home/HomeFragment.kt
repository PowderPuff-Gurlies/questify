package com.example.and101_capstone.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.and101_capstone.R
import com.example.and101_capstone.databinding.FragmentHomeBinding

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

            val newView = inflater.inflate(R.layout.task_item, container, false)

            // Replace the current view with the new view
            (container?.parent as ViewGroup).removeAllViews()
            (container?.parent as ViewGroup).addView(newView)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}