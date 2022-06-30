package com.android.music.muziko.repository

import android.content.Context
import com.android.music.muziko.appInterface.PlaylistRepositoryInterface
import com.android.music.muziko.model.Playlist
import java.util.*

class PlaylistRepository (val context: Context?) :
    PlaylistRepositoryInterface{
    override fun createPlaylist(name: String) {
        val uniqueID = UUID.randomUUID().toString()
        val playlist = Playlist(uniqueID, name, 0, "")
        RoomRepository.createPlaylist(playlist)
    }

    override fun createPlaylist(name: String, countOfSongs: Int, songs: String) {
        val uniqueID = UUID.randomUUID().toString()
        val playlist = Playlist(uniqueID, name, countOfSongs, songs)
        RoomRepository.createPlaylist(playlist)
    }

    override fun getPlaylists(): ArrayList<Playlist> {
        return RoomRepository.cachedPlaylistArray
    }

    override fun removePlaylist(id: String): Boolean {
        RoomRepository.removePlaylist(id)
        return true
    }

    fun removeSongFromPlaylist(playlistId: String, songsId: String) {

        RoomRepository.removeSongFromPlaylist(playlistId, songsId)
    }
}