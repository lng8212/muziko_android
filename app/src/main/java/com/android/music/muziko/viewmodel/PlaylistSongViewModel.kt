package com.android.music.muziko.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.android.music.muziko.model.Song
import com.android.music.muziko.repository.PlaylistSongRepository

class PlaylistSongViewModel : BaseViewModel() {

    lateinit var playlistSongRepository: PlaylistSongRepository
    private var playlistId: String = ""

    init {
        dataset = MutableLiveData()
    }

    fun setPlaylistId(pId: String, array: ArrayList<Song>) {
        playlistId = pId
        playlistSongRepository = PlaylistSongRepository(playlistId, array)
        updateData()
    }

    override fun sendDataToFragment(context: Context?, artist: String?) {

    }

    override fun updateData(data: String?) {
        dataset.value = playlistSongRepository.getSongs() as ArrayList<Any>
    }

    override fun getDataset(): ArrayList<Song> {
        return dataset.value as ArrayList<Song>
    }

    fun removeSongFromPlaylist(songId: String) {
        playlistSongRepository.removeSongFromPlaylist(songId)
        updateData()
    }
}