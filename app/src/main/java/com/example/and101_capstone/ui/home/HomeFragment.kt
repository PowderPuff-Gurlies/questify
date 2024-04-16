package com.example.and101_capstone.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.and101_capstone.R
import com.example.and101_capstone.databinding.FragmentHomeBinding
import com.squareup.picasso.Picasso

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

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        val hair1 = binding.hair1
        val hair2 = binding.hair2
        val hair3 = binding.hair3
        val hair4 = binding.hair4
        val hair5 = binding.hair5
        val hair6 = binding.hair6
        val hair7 = binding.hair7
        val hair8 = binding.hair8
        val hair9 = binding.hair9
        // Used to display each asset
        Picasso.get().load(R.mipmap.hair_1).into(hair1)
        Picasso.get().load(R.mipmap.hair_2).into(hair2)
        Picasso.get().load(R.mipmap.hair_3).into(hair3)
        Picasso.get().load(R.mipmap.hair_4).into(hair4)
        Picasso.get().load(R.mipmap.hair_5).into(hair5)
        Picasso.get().load(R.mipmap.hair_6).into(hair6)
        Picasso.get().load(R.mipmap.hair_7).into(hair7)
        Picasso.get().load(R.mipmap.hair_8).into(hair8)
        Picasso.get().load(R.mipmap.hair_9).into(hair9)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}