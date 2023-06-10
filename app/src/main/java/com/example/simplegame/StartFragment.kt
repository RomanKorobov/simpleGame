package com.example.simplegame

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.simplegame.databinding.FragmentStartBinding
import by.kirich1409.viewbindingdelegate.viewBinding

class StartFragment : Fragment(R.layout.fragment_start) {
    private val binding: FragmentStartBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val action = StartFragmentDirections.actionStartFragmentToPlayFragment()
        binding.startButton.setOnClickListener {
            findNavController().navigate(action)
        }
    }
}