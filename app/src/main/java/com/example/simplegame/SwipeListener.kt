package com.example.simplegame

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import kotlin.math.abs

class SwipeListener(context: Context, private val swipeHandler: SwipeHandler): View.OnTouchListener {
    private val gestureDetector: GestureDetector

    init {
        gestureDetector = GestureDetector(context, GestureListener(context))
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) v?.performClick()
        return gestureDetector.onTouchEvent(event)
    }


    inner class GestureListener(context: Context): GestureDetector.SimpleOnGestureListener() {
        private val minDistance = ViewConfiguration.get(context).scaledPagingTouchSlop
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val dx = e2.x - e1.x
            val dy = e2.y - e1.y
            if (dx * dx + dy * dy > minDistance) {
                when {
                    abs(dx) > abs(dy) -> {
                        if (dx > 0) swipeRight() else swipeLeft()
                    }
                    abs(dx) < abs(dy) -> {
                        if (dy > 0) swipeDown() else swipeUp()
                    }
                }
            }
            return true
        }
        private fun swipeRight() {
            swipeHandler.swipeRight()
        }
        private fun swipeLeft() {
            swipeHandler.swipeLeft()
        }
        private fun swipeUp() {
            swipeHandler.swipeUp()
        }
        private fun swipeDown() {
            swipeHandler.swipeDown()
        }
    }
}