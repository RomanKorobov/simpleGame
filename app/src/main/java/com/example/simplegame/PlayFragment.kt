package com.example.simplegame

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.simplegame.databinding.FragmentPlayBinding

class PlayFragment : Fragment(R.layout.fragment_play), SwipeHandler {
    private lateinit var binding: FragmentPlayBinding
    private lateinit var swipeListener: SwipeListener
    private val viewModel = PlayViewModel(Repository())
    private val handler = Handler(Looper.getMainLooper())
    private var command = MutableLiveData<Command?>()
    private lateinit var mesh: Map<Pair<Int, Int>, TextView>

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
        mesh = mapOf(Pair(Pair(0, 0), binding.el00), Pair(Pair(0, 1), binding.el01),
            Pair(Pair(0, 2), binding.el02), Pair(Pair(0, 3), binding.el03), Pair(Pair(1, 0), binding.el10),
            Pair(Pair(1, 1), binding.el11), Pair(Pair(1, 2), binding.el12), Pair(Pair(1, 3), binding.el13),
            Pair(Pair(2, 0), binding.el20), Pair(Pair(2, 1), binding.el21), Pair(Pair(2, 2), binding.el22),
            Pair(Pair(2, 3), binding.el23), Pair(Pair(3, 0), binding.el30), Pair(Pair(3, 1), binding.el31),
            Pair(Pair(3, 2), binding.el32), Pair(Pair(3, 3), binding.el33))
        observe()
        drawField(viewModel.getField())
        viewModel.predict()
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
        for (i in field.indices) {
            for (j in field[i].indices) {
                val textView: TextView = mesh[i to j]!!
                if (field[i][j] == 0) textView.text = "" else textView.text = field[i][j].toString()
                textView.setTextColor(Color.BLACK)
            }
        }
    }

    fun nextMove(com: Command) {
        when(com) {
            Command.LEFT -> {
                drawField(viewModel.leftField.value!!)
                viewModel.setField(viewModel.leftField.value!!)
            }
            Command.UP -> {
                drawField(viewModel.upField.value!!)
                viewModel.setField(viewModel.upField.value!!)
            }
            Command.RIGHT -> {
                drawField(viewModel.rightField.value!!)
                viewModel.setField(viewModel.rightField.value!!)
            }
            Command.DOWN -> {
                drawField(viewModel.downField.value!!)
                viewModel.setField(viewModel.downField.value!!)
            }
        }
        command.postValue(null)
        viewModel.reset()
        viewModel.predict()
    }

    fun observe() {
        viewModel.leftField.observe(viewLifecycleOwner) {
            if (it != null && command.value == Command.LEFT) handler.post { viewModel.leftField.value?.let { nextMove(command.value!!) } }
        }
        viewModel.rightField.observe(viewLifecycleOwner) {
            if (it != null && command.value == Command.RIGHT) handler.post { viewModel.rightField.value?.let { nextMove(command.value!!) } }
        }
        viewModel.upField.observe(viewLifecycleOwner) {
            if (it != null && command.value == Command.UP) handler.post { viewModel.upField.value?.let { nextMove(command.value!!) } }
        }
        viewModel.downField.observe(viewLifecycleOwner) {
            if (it != null && command.value ==  Command.DOWN) handler.post { viewModel.downField.value?.let { nextMove(command.value!!) } }
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
