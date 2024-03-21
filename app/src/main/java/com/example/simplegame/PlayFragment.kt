package com.example.simplegame

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.simplegame.databinding.FragmentPlayBinding

class PlayFragment : Fragment(R.layout.fragment_play), SwipeHandler {
    private lateinit var binding: FragmentPlayBinding
    private lateinit var swipeListener: SwipeListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayBinding.inflate(inflater, container, false)
        swipeListener = SwipeListener(requireContext(), this)
        binding.playContainer.setOnTouchListener(swipeListener)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun swipeUp() {
        Log.d("GEST", "up")
    }

    override fun swipeDown() {
        Log.d("GEST", "down")
    }

    override fun swipeLeft() {
        Log.d("GEST", "left")
    }

    override fun swipeRight() {
        Log.d("GEST", "right")
    }

    fun drawField(field: Array<IntArray>) {

    }
}
