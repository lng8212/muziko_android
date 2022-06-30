package com.android.music.muziko.helper

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import com.android.music.muziko.appInterface.VoidCallback
import kotlin.math.sin

class AnimationHelper {
    companion object {

        private const val DURATION: Long = 180
        private const val SCALE = 0.95f

        fun scaleAnimation(view: View?, animationListener: VoidCallback, scale: Float) {
            ViewCompat.animate(view!!)
                .setDuration(DURATION)
                .scaleX(if (scale != 0f) scale else SCALE)
                .scaleY(if (scale != 0f) scale else SCALE)
                .setInterpolator(CycleInterpolator())
                .setListener(object : ViewPropertyAnimatorListener {
                    override fun onAnimationStart(view: View) {
                        view.isClickable = false
                        view.isEnabled = false
                    }

                    override fun onAnimationEnd(view: View) {
                        // some thing
                        view.isClickable = true
                        view.isEnabled = true
                        animationListener.execute()
                    }

                    override fun onAnimationCancel(view: View) {

                    }
                })
                .withLayer()
                .start()
        }

        private class CycleInterpolator : android.view.animation.Interpolator {

            private val mCycles = 0.5f

            override fun getInterpolation(input: Float): Float {
                return sin(2.0 * mCycles.toDouble() * Math.PI * input.toDouble()).toFloat()
            }
        }
    }
}