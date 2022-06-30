package com.android.music.muziko.utils

import com.android.music.muziko.helper.Coordinator
import java.util.*

object TimeUtils {
    fun getReadableDuration(durationInMilliSeconds: Long): String {

        val minutes = durationInMilliSeconds / 1000 / 60
        val seconds = durationInMilliSeconds / 1000 % 60

        return if (minutes < 60)
            String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        else
            String.format(Locale.getDefault(), "%03d:%02d", minutes, seconds)

    }

    fun getDurationOfCurrentMusic(): Long? {
//        return SongFragment.songAdapter.listSong[SongFragment.songAdapter.getCurrentPosition()].duration
        return Coordinator.currentPlayingSong?.duration
    }
}