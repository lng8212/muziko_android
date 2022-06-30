package com.android.music.muziko.helper

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.util.Log
import com.android.music.muziko.service.NotificationPlayerService
import com.android.music.muziko.activity.MainActivity

// class for controlling mediaPlayer
class MediaPlayerAgent(val context: Context) {
    private var mediaPlayer = MediaPlayer()
    fun playMusic(data: String) {
        val uri: Uri = Uri.parse(data)
        mediaPlayer.release()
        mediaPlayer = MediaPlayer.create(context, uri)
        mediaPlayer.start()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            playAsService()
    }

    private fun playAsService() {
        Log.e("media agent", "play as service")
        NotificationPlayerService.stopNotification(MainActivity.activity.baseContext)
        NotificationPlayerService.startNotification(
            MainActivity.activity.baseContext,
            "start notif"
        )
    }

    fun pauseMusic() {
        mediaPlayer.pause()
    }

    fun resumePlaying() {
        mediaPlayer.start()
    }

    fun stop() {
        mediaPlayer.stop()
    }

    fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    fun seekTo(newPosition: Int) {
        mediaPlayer.seekTo(newPosition)
    }


}
