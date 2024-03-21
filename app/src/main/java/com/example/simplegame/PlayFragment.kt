package com.example.simplegame

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.simplegame.databinding.FragmentPlayBinding

class PlayFragment : Fragment(R.layout.fragment_play), SwipeHandler {
    private lateinit var binding: FragmentPlayBinding
    private lateinit var swipeListener: SwipeListener
    private val viewModel: PlayViewModel by viewModels()
    private val handler = Handler(Looper.getMainLooper())
    private var command: Command? = null

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
        command = Command.UP
    }

    override fun swipeDown() {
        command = Command.DOWN
    }

    override fun swipeLeft() {
        command = Command.LEFT
    }

    override fun swipeRight() {
        command = Command.RIGHT
    }

    fun drawField(field: Array<IntArray>) {

    }

    fun observe() {
        viewModel.leftReady.observe(viewLifecycleOwner) {
            if (it && command == Command.LEFT) handler.post {
                viewModel.leftField.value?.let { it1 -> drawField(it1) }
                command = null
            }
        }
        viewModel.rightReady.observe(viewLifecycleOwner) {
            if (it && command == Command.RIGHT) handler.post {
                viewModel.rightField.value?.let { it1 -> drawField(it1) }
                command = null
            }
        }
        viewModel.upReady.observe(viewLifecycleOwner) {
            if (it && command == Command.UP) handler.post {
                viewModel.upField.value?.let { it1 -> drawField(it1) }
                command = null
            }
        }
        viewModel.downReady.observe(viewLifecycleOwner) {
            if (it && command == Command.DOWN) handler.post {
                viewModel.downField.value?.let { it1 -> drawField(it1) }
                command = null
            }
        }
    }
}
