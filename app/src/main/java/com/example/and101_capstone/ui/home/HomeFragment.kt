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

        val hair = binding.hair
        val shirt = binding.shirt
        val pants = binding.pants
        val skin = binding.skin
        val shoe = binding.shoe

//        val backdrop = binding.backdrop
//        Picasso.get().load(R.mipmap.brick).into(backdrop)

        // Used to display each asset
        Picasso.get().load(R.mipmap.hair_1).into(hair)
        Picasso.get().load(R.mipmap.shirt_1).into(shirt)
        Picasso.get().load(R.mipmap.pants_1).into(pants)
        Picasso.get().load(R.mipmap.skin_1).into(skin)
        Picasso.get().load(R.mipmap.shoe_1).into(shoe)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}