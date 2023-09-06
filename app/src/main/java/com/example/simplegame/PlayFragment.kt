package com.example.simplegame

import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.simplegame.databinding.FragmentPlayBinding

class PlayFragment : Fragment(R.layout.fragment_play) {
    private val binding: FragmentPlayBinding by viewBinding()
    private var winFlag = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStickers()
        val gestureDetector = GestureDetectorCompat(requireContext(), SwipeListener(this))
        view.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                gestureDetector.onTouchEvent(event)
                return true
            }
        })
        newNumber()
        refresh()
        binding.newGameButton.setOnClickListener {
            clear()
            newNumber()
            refresh()
        }
    }

    fun move(direction: Directions) {
        when (direction) {
            Directions.DOWN -> down()
            Directions.UP -> up()
            Directions.LEFT -> left()
            Directions.RIGHT -> right()
            Directions.NONE -> {}
        }
    }

    private fun down() {
        var moveFlag = false
        for (j in 0 until 4) {
            val new = Array(4) { 0 }
            var cell = 3
            val values = mutableListOf<Int>()
            for (i in 3 downTo 0) {
                if (nums[i][j] != 0) values.add(nums[i][j])
            }
            if (values.isEmpty()) continue
            new[cell] = values[0]
            for (f in 1 until values.size) {
                if (values[f] == new[cell]) {
                    new[cell] *= 2
                    cell--
                } else {
                    cell -= if (new[cell] == 0) 0 else 1
                    new[cell] = values[f]
                }
            }
            for (k in new.indices) {
                moveFlag = moveFlag || nums[k][j] != new[k]
                nums[k][j] = new[k]
            }
        }
        if (moveFlag) {
            newNumber()
            refresh()
        }
    }

    private fun up() {
        var moveFlag = false
        for (j in 0 until 4) {
            val new = Array(4) { 0 }
            var cell = 0
            val values = mutableListOf<Int>()
            for (i in 0 until 4) {
                if (nums[i][j] != 0) values.add(nums[i][j])
            }
            if (values.isEmpty()) continue
            new[cell] = values[0]
            for (f in 1 until values.size) {
                if (values[f] == new[cell]) {
                    new[cell] *= 2
                    cell++
                } else {
                    cell += if (new[cell] == 0) 0 else 1
                    new[cell] = values[f]
                }
            }
            for (k in new.indices) {
                moveFlag = moveFlag || nums[k][j] != new[k]
                nums[k][j] = new[k]
            }
        }
        if (moveFlag) {
            newNumber()
            refresh()
        }
    }

    private fun left() {
        var moveFlag = false
        for (i in nums.indices) {
            val new = Array(4) { 0 }
            var cell = 0
            val values = nums[i].filter { it != 0 }
            if (values.isEmpty()) continue
            new[cell] = values[0]
            for (j in 1 until values.size) {
                if (values[j] == new[cell]) {
                    new[cell] *= 2
                    cell++
                } else {
                    cell += if (new[cell] == 0) 0 else 1
                    new[cell] = values[j]
                }
            }
            for (k in new.indices) {
                moveFlag = moveFlag || nums[i][k] != new[k]
                nums[i][k] = new[k]
            }
        }
        if (moveFlag) {
            newNumber()
            refresh()
        }
    }

    private fun right() {
        var moveFlag = false
        for (i in nums.indices) {
            val new = Array(4) { 0 }
            var cell = 3
            val values = nums[i].filter { it != 0 }
            if (values.isEmpty()) continue
            new[cell] = values[values.size - 1]
            for (j in values.size - 2 downTo 0) {
                if (values[j] == new[cell]) {
                    new[cell] *= 2
                    cell--
                } else {
                    cell -= if (new[cell] == 0) 0 else 1
                    new[cell] = values[j]
                }
            }
            for (k in new.indices) {
                moveFlag = moveFlag || nums[i][k] != new[k]
                nums[i][k] = new[k]
            }
        }
        if (moveFlag) {
            newNumber()
            refresh()
        }
    }

    private fun newNumber() {
        val new = (1..2).random() * 2
        val candidate = (0..3).random()
        for (j in nums[candidate].indices) {
            if (nums[candidate][j] == 0) {
                nums[candidate][j] = new
                return
            }
        }
        for (i in nums.indices) {
            for (j in nums[i].indices) {
                if (nums[i][j] == 0) {
                    nums[i][j] = new
                    return
                }
            }
        }

    }

    private fun refresh() {
        for (i in nums.indices) {
            for (j in nums[i].indices) {
                when (nums[i][j]) {
                    2, 4, 8 -> {
                        stickers[i][j].setTextColor(Color.parseColor("#00E676"))
                        stickers[i][j].textSize = 50f
                    }

                    16, 32, 64 -> {
                        stickers[i][j].setTextColor(Color.parseColor("#FFFF00"))
                        stickers[i][j].textSize = 35f
                    }

                    128, 256, 512 -> {
                        stickers[i][j].setTextColor(Color.parseColor("#FF5722"))
                        stickers[i][j].textSize = 25f
                    }

                    1024, 2048 -> {
                        stickers[i][j].setTextColor(Color.parseColor("#FF5722"))
                        stickers[i][j].textSize = 15f
                    }

                    else -> {
                        stickers[i][j].setTextColor(Color.parseColor("#BF360C"))
                        stickers[i][j].textSize = 12f
                    }
                }
                stickers[i][j].text = if (nums[i][j] == 0) "" else nums[i][j].toString()
                if (nums[i][j] == 2048 && winFlag) {
                    Toast.makeText(
                        requireContext(),
                        "Вы выйграли!",
                        Toast.LENGTH_SHORT
                    ).show()
                    winFlag = false
                }
            }
        }
    }

    private fun clear() {
        for (i in nums.indices) {
            for (j in nums[i].indices) {
                nums[i][j] = 0
            }
        }
    }

    private fun initStickers() {
        stickers = listOf(
            listOf(binding.textViewA0, binding.textViewA1, binding.textViewA2, binding.textViewA3),
            listOf(binding.textViewB0, binding.textViewB1, binding.textViewB2, binding.textViewB3),
            listOf(binding.textViewC0, binding.textViewC1, binding.textViewC2, binding.textViewC3),
            listOf(binding.textViewD0, binding.textViewD1, binding.textViewD2, binding.textViewD3)
        )
    }

    companion object {
        private val nums = Array(4) { Array(4) { 0 } }
        private var stickers = listOf(listOf<TextView>())
    }
}
