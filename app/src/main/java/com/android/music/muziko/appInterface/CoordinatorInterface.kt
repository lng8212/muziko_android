package com.android.music.muziko.appInterface

import android.content.Context
import com.android.music.muziko.helper.MediaPlayerAgent
import com.android.music.muziko.model.Song

interface CoordinatorInterface {

//    var playingOrder: Enums.PlayingOrder
    var nowPlayingQueue: ArrayList<Song>
    var mediaPlayerAgent: MediaPlayerAgent
    var position: Int

    fun setup(context: Context)


//    player
    fun playNextSong()
    fun playPrevSong()
    fun pause() //pause current playing song
    fun play(song: String) //play new song
    fun resume() //resume current playing song
    fun stop()
    fun release()


//    update
    fun updateNowPlayingQueue()

//   get Status
    fun isPlaying(): Boolean


//    get Info
//    fun getCurrentPlayingSong(): SongModel
    fun getCurrentSongPosition(): Int
    fun playSelectedSong(song: Song)
    fun getPositionInPlayer(): Int
    fun hasNext(): Boolean
    fun hasPrev(): Boolean
    fun getPrevSongData(): String?
    fun getNextSongData(): String?
    fun getNextSong(): Song
    fun getPrevSong(): Song
    fun getSongAtPosition(position: Int): String?


//    seek bar
    fun seekTo(newPosition: Int)

    // favourite

    fun addFavourite()

}