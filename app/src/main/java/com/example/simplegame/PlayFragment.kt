package com.example.simplegame

import android.annotation.SuppressLint
import android.content.Context.WINDOW_SERVICE
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.simplegame.databinding.FragmentPlayBinding
import kotlin.math.abs

class PlayFragment : Fragment(R.layout.fragment_play) {
    private val binding: FragmentPlayBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCells()
        newMove()
        refresh()
        binding.root.setOnTouchListener(object : View.OnTouchListener {
            var xStart = 0f
            var yStart = 0f
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        xStart = event.x
                        yStart = event.y
                    }

                    MotionEvent.ACTION_MOVE -> {
                    }

                    MotionEvent.ACTION_UP -> {
                        val deltaX = event.x - xStart
                        val deltaY = event.y - yStart
                        if (abs(deltaX) > 5 * abs(deltaY)) {
                            if (deltaX > 0) {
                                moveRight()
                            }
                            if (deltaX < 0) {
                                moveLeft()
                            }
                        }
                        if (abs(deltaY) > 5 * abs(deltaX)) {
                            if (deltaY > 0) {
                                moveDown()
                            }
                            if (deltaY < 0) {
                                moveUp()
                            }
                        }
                    }
                }
                return true
            }
        })
        binding.newGameButton.setOnClickListener {
            clearTheField()
            newMove()
            refresh()
        }
    }

    private fun newMove() {
        val cellNumber = (0..15).random()
        if (numbers[cellNumber] == 0) {
            numbers[cellNumber] = 2
        } else {
            var i = 0
            while (i < 16) {
                if (numbers[i] == 0) {
                    numbers[i] = (1..2).random() * 2
                    i = 17
                } else {
                    i++
                }
            }

        }
    }

    @SuppressLint("ResourceAsColor")
    private fun refresh() {
        for (i in 0..15) {
            if (numbers[i] > 0) {
                cells[i].text = numbers[i].toString()
                cells[i].setTextColor(Color.parseColor("#3F51B5"))
                cells[i].textSize = 50f
                val length = cells[i].text.length
                if (length == 2) {
                    cells[i].setTextColor(Color.parseColor("#FFEA00"))
                } else if (length == 3) {
                    cells[i].textSize = 30f
                    cells[i].setTextColor(Color.parseColor("#E53935"))
                } else if (length == 4) {
                    cells[i].textSize = 20f
                    cells[i].setTextColor(Color.parseColor("#E91E63"))
                } else if (length > 4) {
                    cells[i].textSize = 10f
                    cells[i].setTextColor(Color.parseColor("#b21639"))
                }

            } else {
                cells[i].text = ""
            }
        }
    }

    private fun moveUp() {
        var noMoveFlag = true
        for (i in 4..15) {
            if (numbers[i] != 0) {
                if (numbers[i] == numbers[i - 4]) {
                    numbers[i - 4] = 2 * numbers[i]
                    numbers[i] = 0
                    noMoveFlag = false
                }
                if (numbers[i - 4] == 0) {
                    var stopFlag = false
                    var current = i
                    while (!stopFlag) {
                        numbers[current - 4] = numbers[current]
                        numbers[current] = 0
                        current -= 4
                        noMoveFlag = false
                        if (current < 4) {
                            break
                        }
                        if (numbers[current - 4] != 0) {
                            if (numbers[current - 4] == numbers[current]) {
                                numbers[current - 4] = 2 * numbers[current]
                                numbers[i] = 0
                                noMoveFlag = false
                            }
                            stopFlag = true
                        }
                    }
                }
            }
        }
        if (!noMoveFlag) {
            newMove()
            refresh()
        }
    }

    private fun moveDown() {
        var noMoveFlag = true
        for (i in 11 downTo 0) {
            if (numbers[i] != 0) {
                if (numbers[i] == numbers[i + 4]) {
                    numbers[i + 4] = 2 * numbers[i]
                    numbers[i] = 0
                    noMoveFlag = false
                }
                if (numbers[i + 4] == 0) {
                    var stopFlag = false
                    var current = i
                    while (!stopFlag) {
                        numbers[current + 4] = numbers[current]
                        numbers[current] = 0
                        current += 4
                        noMoveFlag = false
                        if (current > 11) {
                            break
                        }
                        if (numbers[current + 4] != 0) {
                            if (numbers[current + 4] == numbers[current]) {
                                numbers[current + 4] = 2 * numbers[current]
                                numbers[i] = 0
                                noMoveFlag = false
                            }
                            stopFlag = true
                        }
                    }
                }
            }
        }
        if (!noMoveFlag) {
            newMove()
            refresh()
        }
    }

    private fun moveLeft() {
        var noMoveFlag = true
        for (i in 1..15) {
            if ((numbers[i] != 0) && (i % 4 != 0)) {
                if (numbers[i] == numbers[i - 1]) {
                    numbers[i - 1] = 2 * numbers[i]
                    numbers[i] = 0
                    noMoveFlag = false
                }
                if (numbers[i - 1] == 0) {
                    var stopFlag = false
                    var current = i
                    while (!stopFlag) {
                        numbers[current - 1] = numbers[current]
                        numbers[current] = 0
                        current--
                        noMoveFlag = false
                        if (current % 4 == 0) {
                            break
                        }
                        if (numbers[current - 1] != 0) {
                            if (numbers[current - 1] == numbers[current]) {
                                numbers[current - 1] = 2 * numbers[current]
                                numbers[i] = 0
                                noMoveFlag = false
                            }
                            stopFlag = true
                        }
                    }
                }
            }
        }
        if (!noMoveFlag) {
            newMove()
            refresh()
        }
    }

    private fun moveRight() {
        var noMoveFlag = true
        for (i in 14 downTo 0) {
            if ((numbers[i] != 0) && (i % 4 != 3)) {
                if (numbers[i] == numbers[i + 1]) {
                    numbers[i + 1] = 2 * numbers[i]
                    numbers[i] = 0
                    noMoveFlag = false
                }
                if (numbers[i + 1] == 0) {
                    var stopFlag = false
                    var current = i
                    while (!stopFlag) {
                        numbers[current + 1] = numbers[current]
                        numbers[current] = 0
                        current++
                        noMoveFlag = false
                        if (current % 4 == 3) {
                            break
                        }
                        if (numbers[current + 1] != 0) {
                            if (numbers[current + 1] == numbers[current]) {
                                numbers[current + 1] = 2 * numbers[current]
                                numbers[i] = 0
                                noMoveFlag = false
                            }
                            stopFlag = true
                        }
                    }
                }
            }
        }
        if (!noMoveFlag) {
            newMove()
            refresh()
        }
    }

    private fun clearTheField() {
        for (i in 0..15) {
            numbers[i] = 0
        }
    }

    private fun initCells() {
        val displayMetrics = DisplayMetrics()
        val windowManager = context?.getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        phoneWidth = displayMetrics.widthPixels
        cells.add(0, binding.firstButton)
        cells.add(1, binding.secondButton)
        cells.add(2, binding.thirdButton)
        cells.add(3, binding.fourButton)
        cells.add(4, binding.fiveButton)
        cells.add(5, binding.sixButton)
        cells.add(6, binding.sevenButton)
        cells.add(7, binding.eightButton)
        cells.add(8, binding.nineButton)
        cells.add(9, binding.tenButton)
        cells.add(10, binding.elevenButton)
        cells.add(11, binding.twelveButton)
        cells.add(12, binding.thirteenButton)
        cells.add(13, binding.fourteenButton)
        cells.add(14, binding.fifteenButton)
        cells.add(15, binding.sixteenButton)
        images.add(0, binding.imageFirst)
        images.add(0, binding.imageSecond)
        images.add(0, binding.imageThird)
        images.add(0, binding.imageFour)
        images.add(0, binding.imageFive)
        images.add(0, binding.imageSix)
        images.add(0, binding.imageSeven)
        images.add(0, binding.imageEight)
        images.add(0, binding.imageNine)
        images.add(0, binding.imageTen)
        images.add(0, binding.imageEleven)
        images.add(0, binding.imageTwelve)
        images.add(0, binding.imageThirteen)
        images.add(0, binding.imageFourteen)
        images.add(0, binding.imageFifteen)
        images.add(0, binding.imageSixteen)
        images.forEach { image ->
            image.layoutParams.height = (phoneWidth / 4)
            image.layoutParams.width = (phoneWidth / 4)
        }
    }

    companion object Singleton {
        var numbers = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        var cells = mutableListOf<TextView>()
        var images = mutableListOf<ImageView>()
        var phoneWidth = 20
    }
}
