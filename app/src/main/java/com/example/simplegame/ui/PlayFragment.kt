package com.example.simplegame.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.simplegame.vm.Command
import com.example.simplegame.vm.Data
import com.example.simplegame.vm.PlayViewModel
import com.example.simplegame.R
import com.example.simplegame.vm.Repository
import com.example.simplegame.databinding.FragmentPlayBinding

class PlayFragment : Fragment(R.layout.fragment_play), SwipeHandler {
    private lateinit var binding: FragmentPlayBinding
    private lateinit var swipeListener: SwipeListener
    private val viewModel = PlayViewModel(Repository())
    private val handler = Handler(Looper.getMainLooper())
    private var command = MutableLiveData<Command?>()
    private lateinit var mesh: Map<Pair<Int, Int>, TextView>
    private lateinit var downObserver: Observer<in Array<IntArray>?>
    private lateinit var upObserver: Observer<in Array<IntArray>?>
    private lateinit var leftObserver: Observer<in Array<IntArray>?>
    private lateinit var rightObserver: Observer<in Array<IntArray>?>

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
        mesh = mapOf(
            Pair(Pair(0, 0), binding.el00),
            Pair(Pair(0, 1), binding.el01),
            Pair(Pair(0, 2), binding.el02),
            Pair(Pair(0, 3), binding.el03),
            Pair(Pair(1, 0), binding.el10),
            Pair(Pair(1, 1), binding.el11),
            Pair(Pair(1, 2), binding.el12),
            Pair(Pair(1, 3), binding.el13),
            Pair(Pair(2, 0), binding.el20),
            Pair(Pair(2, 1), binding.el21),
            Pair(Pair(2, 2), binding.el22),
            Pair(Pair(2, 3), binding.el23),
            Pair(Pair(3, 0), binding.el30),
            Pair(Pair(3, 1), binding.el31),
            Pair(Pair(3, 2), binding.el32),
            Pair(Pair(3, 3), binding.el33)
        )
        createObservers()
        observe()
        drawField(viewModel.getField())
        viewModel.predict()
        binding.newGameButton.setOnClickListener { newGame() }
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

    override fun onDestroyView() {
        Data.save(viewModel.getField())
        super.onDestroyView()
    }

    private fun newGame() {
        binding.gameOverTextView.visibility = View.INVISIBLE
        clearObservers()
        viewModel.setNewGameField()
        viewModel.reset()
        command.postValue(null)
        drawField(viewModel.getField())
        createObservers()
        viewModel.predict()
    }

    private fun drawField(field: Array<IntArray>) {
        for (i in field.indices) {
            for (j in field[i].indices) {
                val textView: TextView = mesh[i to j]!!
                if (field[i][j] == 0) {
                    textView.text = ""
                } else {
                    textView.text = field[i][j].toString()
                    when (field[i][j]) {
                        in 2..8 -> {
                            textView.setTextColor(Color.BLACK)
                            textView.textSize = 25f
                        }

                        in 16..64 -> {
                            textView.setTextColor(Color.parseColor("#b21639"))
                            textView.textSize = 25f
                        }

                        in 128..512 -> {
                            textView.setTextColor(Color.parseColor("#FF018786"))
                            textView.textSize = 25f
                        }

                        else -> {
                            textView.setTextColor(Color.parseColor("#AD1457"))
                            textView.textSize = 16f
                        }
                    }
                }
            }
        }
    }

    private fun nextMove(com: Command) {
        var valid = true
        val newField: Array<IntArray>
        when (com) {
            Command.LEFT -> {
                newField = viewModel.leftField.value!!
                valid = !viewModel.invalidMove(newField)
            }

            Command.UP -> {
                newField = viewModel.upField.value!!
                valid = !viewModel.invalidMove(newField)
            }

            Command.RIGHT -> {
                newField = viewModel.rightField.value!!
                valid = !viewModel.invalidMove(newField)
            }

            Command.DOWN -> {
                newField = viewModel.downField.value!!
                valid = !viewModel.invalidMove(newField)
            }
        }
        if (valid) {
            viewModel.addNewNum(newField)
            drawField(newField)
            viewModel.setField(newField)
            command.postValue(null)
            viewModel.reset()
            viewModel.predict()
        } else if (viewModel.gameOver) binding.gameOverTextView.visibility = View.VISIBLE
    }

    private fun observe() {
        viewModel.leftField.observe(viewLifecycleOwner, leftObserver)
        viewModel.rightField.observe(viewLifecycleOwner, rightObserver)
        viewModel.upField.observe(viewLifecycleOwner, upObserver)
        viewModel.downField.observe(viewLifecycleOwner, downObserver)
        command.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    Command.DOWN -> {
                        viewModel.downField.value?.let { nextMove(command.value!!) }
                    }

                    Command.RIGHT -> {
                        viewModel.rightField.value?.let { nextMove(command.value!!) }
                    }

                    Command.LEFT -> {
                        viewModel.leftField.value?.let { nextMove(command.value!!) }
                    }

                    Command.UP -> {
                        viewModel.upField.value?.let { nextMove(command.value!!) }
                    }
                }
            }
        }
    }

    private fun clearObservers() {
        viewModel.downField.removeObserver(downObserver)
        viewModel.upField.removeObserver(upObserver)
        viewModel.leftField.removeObserver(leftObserver)
        viewModel.rightField.removeObserver(rightObserver)
    }

    private fun createObservers() {
        leftObserver = Observer {
            if (it != null && command.value == Command.LEFT) handler.post {
                viewModel.leftField.value?.let {
                    nextMove(
                        command.value!!
                    )
                }
            }
        }
        rightObserver = Observer {
            if (it != null && command.value == Command.RIGHT) handler.post {
                viewModel.rightField.value?.let {
                    nextMove(
                        command.value!!
                    )
                }
            }
        }
        upObserver = Observer {
            if (it != null && command.value == Command.UP) handler.post {
                viewModel.upField.value?.let {
                    nextMove(
                        command.value!!
                    )
                }
            }
        }
        downObserver = Observer {
            if (it != null && command.value == Command.DOWN) handler.post {
                viewModel.downField.value?.let {
                    nextMove(
                        command.value!!
                    )
                }
            }
        }
    }
}
