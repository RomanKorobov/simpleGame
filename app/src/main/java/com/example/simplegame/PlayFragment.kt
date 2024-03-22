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
import androidx.lifecycle.MutableLiveData
import com.example.simplegame.databinding.FragmentPlayBinding

class PlayFragment : Fragment(R.layout.fragment_play), SwipeHandler {
    private lateinit var binding: FragmentPlayBinding
    private lateinit var swipeListener: SwipeListener
    private val viewModel: PlayViewModel by viewModels()
    private val handler = Handler(Looper.getMainLooper())
    private var command = MutableLiveData<Command?>()

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
        observe()
    }

    override fun swipeUp() {
        command.postValue(Command.UP)
    }

    override fun swipeDown() {
        command.postValue(Command.DOWN)
    }

    override fun swipeLeft() {
        command.postValue(Command.LEFT)
    }

    override fun swipeRight() {
        command.postValue(Command.RIGHT)
    }

    fun drawField(field: Array<IntArray>) {

    }

    fun nextMove(com: Command) {
        when(com) {
            Command.LEFT -> { drawField(viewModel.leftField.value!!) }
            Command.UP -> { drawField(viewModel.upField.value!!) }
            Command.RIGHT -> { drawField(viewModel.rightField.value!!) }
            Command.DOWN -> { drawField(viewModel.downField.value!!) }
        }
        command.postValue(null)
        viewModel.reset()
    }

    fun observe() {
        viewModel.leftField.observe(viewLifecycleOwner) {
            if (it != null && command.value == Command.LEFT) handler.post {
                viewModel.leftField.value?.let { nextMove(command.value!!) }
            }
        }
        viewModel.rightField.observe(viewLifecycleOwner) {
            if (it != null && command.value == Command.RIGHT) handler.post {
                viewModel.rightField.value?.let { nextMove(command.value!!) }
            }
        }
        viewModel.rightField.observe(viewLifecycleOwner) {
            if (it != null && command.value == Command.UP) handler.post {
                viewModel.upField.value?.let { nextMove(command.value!!) }
            }
        }
        viewModel.downField.observe(viewLifecycleOwner) {
            if (it != null && command.value ==  Command.DOWN) handler.post {
                viewModel.downField.value?.let { nextMove(command.value!!) }
            }
        }
        command.observe(viewLifecycleOwner) {
            if (it != null) {
                when(it) {
                    Command.DOWN -> { viewModel.downField.value?.let { nextMove(command.value!!) } }
                    Command.RIGHT -> { viewModel.rightField.value?.let { nextMove(command.value!!) } }
                    Command.LEFT -> { viewModel.leftField.value?.let { nextMove(command.value!!) } }
                    Command.UP -> { viewModel.upField.value?.let { nextMove(command.value!!) } }
                }
            }
        }
    }
}
