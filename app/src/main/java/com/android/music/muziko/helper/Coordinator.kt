package com.android.music.muziko.helper

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.android.music.R
import com.android.music.muziko.activity.MainActivity
import com.android.music.muziko.appInterface.CoordinatorInterface
import com.android.music.muziko.model.Song


// object for controlling play music
object Coordinator : CoordinatorInterface {
    override lateinit var nowPlayingQueue: ArrayList<Song> // queue songs is playing
    @SuppressLint("StaticFieldLeak")
    override lateinit var mediaPlayerAgent: MediaPlayerAgent

    override var position: Int = -1
    //        SongFragment.songAdapter.getCurrentPosition() ?: -1 // position of song in this queue
    var sourceOfSelectedSong =
        "songs" // source of current song, can be "playlist_name" or favourite
    var currentDataSource = arrayListOf<Song>() // list of songs to play

    var shuffleMode = PlaybackStateCompat.SHUFFLE_MODE_NONE
    var repeatMode = PlaybackStateCompat.REPEAT_MODE_NONE

    var currentPlayingSong: Song? = null // song is playing

    override fun setup(context: Context) { // set up mediaPlayer
        mediaPlayerAgent = MediaPlayerAgent(context)
    }

    override fun playNextSong() {
        takeActionBasedOnRepeatMode(
            MainActivity.activity.getString(R.string.onBtnClicked),
            MainActivity.activity.getString(R.string.play_next)
        )
    }

    override fun playPrevSong() {
        takeActionBasedOnRepeatMode(
            MainActivity.activity.getString(R.string.onBtnClicked),
            MainActivity.activity.getString(R.string.play_prev)
        )
    }

    override fun pause() {
        mediaPlayerAgent.pauseMusic()
    }

    override fun play(song: String) {
        Log.e("media", "play")
        mediaPlayerAgent.playMusic(song)
    }

    override fun resume() {
        mediaPlayerAgent.resumePlaying()
    }

    override fun stop() {
        mediaPlayerAgent.stop()
    }

    override fun release() {
        TODO("Not yet implemented")
    }

    // handle next and prev
    fun takeActionBasedOnRepeatMode(actionSource: String, requestedAction: String) {

        when (repeatMode) {
            PlaybackStateCompat.REPEAT_MODE_ONE -> {

                currentPlayingSong?.data?.let { play(it) }

            }
            PlaybackStateCompat.REPEAT_MODE_ALL -> {

                when (requestedAction) {
                    MainActivity.activity.getString(R.string.play_next) -> {
                        if (!hasNext()) {
                            position = -1
                        }
                        getNextSong().data?.let { play(it) }
                        updatePlayerVar(nowPlayingQueue[position])
                    }
                    MainActivity.activity.getString(R.string.play_prev) -> {
                        if (!hasPrev()) {
                            position = nowPlayingQueue.size
                        }
                        getPrevSong().data?.let { play(it) }
                        updatePlayerVar(nowPlayingQueue[position])
                    }
                }

            }
            PlaybackStateCompat.REPEAT_MODE_NONE -> {

                when (actionSource) {
                    MainActivity.activity.getString(R.string.onSongCompletion) -> {
                        when (requestedAction) {
                            MainActivity.activity.getString(R.string.play_next) -> {
                                if (!hasNext()) {
                                    mediaPlayerAgent.pauseMusic()
                                } else {
                                    getNextSong().data?.let { play(it) }
                                    updatePlayerVar(nowPlayingQueue[position])
                                }
                            }
                        }
                    }
                    MainActivity.activity.getString(R.string.onBtnClicked) -> {
                        when (requestedAction) {
                            MainActivity.activity.getString(R.string.play_next) -> {
                                if (!hasNext()) {
//                                    resetPosition
                                    position = -1
                                }
                                getNextSong().data?.let { play(it) }
                                updatePlayerVar(nowPlayingQueue[position])

                            }
                            MainActivity.activity.getString(R.string.play_prev) -> {
                                if (!hasPrev()) {
//                                    resetPosition
                                    position = nowPlayingQueue.size
                                }
                                getPrevSong().data?.let { play(it) }
                                updatePlayerVar(nowPlayingQueue[position])
                            }
                        }

                    }
                }
            }
        }
    }

    override fun updateNowPlayingQueue() {
//        nowPlayingQueue = currentDataSource
//        updateCurrentPlayingSongPosition()
        when (repeatMode) {
            PlaybackStateCompat.REPEAT_MODE_ONE -> nowPlayingQueue =
                arrayListOf(currentPlayingSong!!)
            PlaybackStateCompat.REPEAT_MODE_ALL -> nowPlayingQueue = currentDataSource
            PlaybackStateCompat.REPEAT_MODE_NONE -> nowPlayingQueue = currentDataSource

        }

        when (shuffleMode) {
            PlaybackStateCompat.SHUFFLE_MODE_NONE -> {
                nowPlayingQueue = currentDataSource
                updateCurrentPlayingSongPosition()
            }
            PlaybackStateCompat.SHUFFLE_MODE_ALL -> {

                val lst = nowPlayingQueue.toList()
                val sh_lst = lst.shuffled()
                val p = sh_lst as ArrayList<Song>

                nowPlayingQueue = p
                updateCurrentPlayingSongPosition()
            }
        }
    }

    private fun updateCurrentPlayingSongPosition() {
        position = nowPlayingQueue.indexOf(currentPlayingSong) // position of playing song in the queue
    }

    override fun isPlaying(): Boolean {
        return mediaPlayerAgent.isPlaying()
    }

    override fun getCurrentSongPosition(): Int {
        return position
    }

    override fun playSelectedSong(song: Song) {
        Log.e("Coordinator", "play")
        updatePlayerVar(song)
        updateNowPlayingQueue()
        song.data?.let { play(it) }
    }
    fun getSelectedSong(song: Song): Song {
        return song
    }
    fun updatePlayerVar(song: Song) {
        currentPlayingSong = song
        Log.e("song ", currentPlayingSong.toString())
//        MainActivity.playerPanelFragment.updatePanel()
    }

    override fun getPositionInPlayer(): Int {
        return mediaPlayerAgent.getCurrentPosition()
    }

    override fun hasNext(): Boolean {
        return position + 1 < nowPlayingQueue.size
    }

    override fun hasPrev(): Boolean {
        return position > 0
    }

    override fun getPrevSongData(): String? {
        TODO("Not yet implemented")
    }

    override fun getNextSongData(): String? {
        TODO("Not yet implemented")
    }

    override fun getNextSong(): Song {
        position += 1
        getPositionInNowPlayingQueue()
        return nowPlayingQueue[position]
    }

    private fun getPositionInNowPlayingQueue(): Int {
        return nowPlayingQueue.indexOf(currentPlayingSong)
    }

    override fun getPrevSong(): Song {
        position -= 1
        getPositionInNowPlayingQueue()
        return nowPlayingQueue[position]
    }

    override fun getSongAtPosition(position: Int): String? {
        TODO("Not yet implemented")
    }

    override fun seekTo(newPosition: Int) {
        mediaPlayerAgent.seekTo(newPosition)
    }

    override fun addFavourite() {
        TODO("Not yet implemented")
    }
}