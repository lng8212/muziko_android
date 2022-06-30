package com.android.music.muziko.utils

import android.graphics.Point
import com.android.music.muziko.activity.MainActivity

object ScreenSizeUtils {
    fun getScreenHeight(): Int {
        val display = MainActivity.activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.y
    }

    fun getScreenWidth(): Int {
        val display = MainActivity.activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x
    }

}