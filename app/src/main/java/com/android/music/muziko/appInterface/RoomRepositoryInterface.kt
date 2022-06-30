package com.android.music.muziko.appInterface

import com.android.music.muziko.model.Playlist
import com.android.music.muziko.model.Recently
import com.android.music.ui.Favorites
import com.android.music.muziko.model.Song

interface RoomRepositoryInterface {

    // ----------------------- init Database ------------------------
    fun createDatabase()

    // ----------------------- playlist ------------------------
    fun createPlaylist(playlist: Playlist)
    fun removePlaylist(id: String): Boolean
    fun getPlaylists(): ArrayList<Playlist>
    fun addSongsToPlaylist(playlist_name: String, songsId: String): Boolean
    fun removeSongFromPlaylist(playlistId: String, songsId: String)
    fun listOfPlaylistsContainSpecificSong(songId: Long): ArrayList<String>
    fun removeSongFromPlaylistObject(playlist: Playlist, songsId: String) //check
    fun decreaseCountInDatabase(playlistId: String, countOfSongs: Int)
    fun increaseCountInPlaylistObject(playlist: Playlist)
    fun increaseCountInDatabase(playlist: Playlist)
    fun addSongsToPlaylistInObject(playlist: Playlist, songsId: String) //check
    fun addSongsToPlaylistInDatabase(playlist: Playlist, songsId: String)
    fun getPlaylistFromDatabase(): ArrayList<Playlist>

    fun updateCachedPlaylist()
    fun findPlaylistPositionInCachedArray(playlist: Playlist): Int
    fun getIdByName(name: String): String
    fun getPlaylistById(id: String): Playlist?


    // ----------------------- favorite ------------------------

    fun updateCachedFav()
    fun addSongToFavorites(songsId: Long)
    fun removeSongFromFavorites(song: Song)
    fun getFavoritesFromDatabase(): ArrayList<Favorites>
    fun convertFavSongsToRealSongs(): ArrayList<Song>
    fun songsIdToSongModelConverter(favSong: Favorites): Song?

    // ----------------------- recently ------------------------

    fun updateCachedRecently()
    fun addSongToRecently(songsId: Long)
    fun removeSongFromRecently(song: Song)
    fun getRecentlyFromDatabase(): ArrayList<Recently>
    fun convertRecentlySongsToRealSongs(): ArrayList<Song>
    fun songsIdToSongModelConverterRecently(recently: Recently): Song?
}