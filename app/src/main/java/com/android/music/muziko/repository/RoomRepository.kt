package com.android.music.muziko.repository

import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.android.music.muziko.activity.MainActivity
import com.android.music.muziko.appInterface.RoomRepositoryInterface
import com.android.music.muziko.model.MyDatabase
import com.android.music.muziko.model.Playlist
import com.android.music.muziko.model.Recently
import com.android.music.muziko.model.Song
import com.android.music.muziko.utils.DatabaseConverterUtils
import com.android.music.muziko.utils.SongUtils
import com.android.music.ui.Favorites
import com.android.music.ui.fragments.LibraryFragment
import kotlinx.coroutines.*

object RoomRepository : RoomRepositoryInterface {

    private val applicationScope = CoroutineScope(SupervisorJob())
    var localDatabase: MyDatabase = MyDatabase.getDatabase(
        MainActivity.activity.baseContext!!,
        MainActivity.activity.lifecycleScope
    )
    var cachedPlaylistArray = ArrayList<Playlist>()
    var cachedFavArray_Favorites = ArrayList<Favorites>()
    var cachedFavArray = ArrayList<Song>()
    var cachedRecArray = ArrayList<Song>()
    var cachedRecArray_Recently = ArrayList<Recently>()


    //    ----------------------------------------------- Database ----------------------------------------------------
    override fun createDatabase() {

        applicationScope.launch {
            cachedPlaylistArray = getPlaylistFromDatabase()
            cachedFavArray_Favorites = getFavoritesFromDatabase()
            cachedRecArray_Recently = getRecentlyFromDatabase()
        }
    }

    //    ----------------------------------------------- Playlist ----------------------------------------------------
    override fun updateCachedPlaylist() {
        GlobalScope.launch { //  GlobalScope tồn tại xuyên suốt lúc app hoạt động
            // lifecycleScope ràng buộc với vòng đời của Activity or Fragment
            // CoroutineScope
            cachedPlaylistArray = getPlaylistFromDatabase()
        }
    }

    override fun createPlaylist(playlist: Playlist) {
        applicationScope.launch {
            localDatabase.playlistDAO().addPlaylist(playlist)
        }
        cachedPlaylistArray.add(playlist)
    }

    override fun removePlaylist(id: String): Boolean {
        applicationScope.launch {
            localDatabase.playlistDAO().deletePlaylist(id)

            cachedPlaylistArray =
                localDatabase.playlistDAO().getPlaylists() as ArrayList<Playlist>
        }

        return true
    }

    override fun getPlaylists(): ArrayList<Playlist> {
        return cachedPlaylistArray
    }

    override fun addSongsToPlaylist(playlist_name: String, songsId: String): Boolean {

        val playlist = getPlaylistById(getIdByName(playlist_name))

//        add song to playlist object
        if (playlist != null) {

            val ids = DatabaseConverterUtils.stringToArraylist(playlist.songs)
            for (i in ids) {
                if (i.equals(songsId)) {
                    return true
                }
            }

            val position = findPlaylistPositionInCachedArray(playlist)

            if (position >= 0) {
                addSongsToPlaylistInObject(
                    cachedPlaylistArray[position],
                    songsId
                )
            }

            addSongsToPlaylistInDatabase(playlist, songsId)

        }

        return true
    }

    override fun removeSongFromPlaylist(playlistId: String, songsId: String) {
        val playlist = getPlaylistById(playlistId)


        if (playlist != null) {

            val position = findPlaylistPositionInCachedArray(playlist)

            if (position >= 0) {
                //remove song from playlist object
                removeSongFromPlaylistObject(cachedPlaylistArray[position], songsId)

                //decrease count of song in playlist object
                cachedPlaylistArray[position].countOfSongs--

            }

            GlobalScope.launch {
                //update songs in database
                localDatabase.playlistDAO().updateSongs(playlistId, playlist.songs)

                //update count of songs in database
                decreaseCountInDatabase(playlistId, playlist.countOfSongs)
            }

        }
    }

    override fun listOfPlaylistsContainSpecificSong(songId: Long): ArrayList<String> {
        val pls = arrayListOf<String>()

        for (playlist in cachedPlaylistArray) {
            val ids = DatabaseConverterUtils.stringToArraylist(playlist.songs)
            for (id in ids) {
                if (id.toLong() == songId) {
                    pls.add(playlist.id)
                }
            }
        }

        return pls
    }

    override fun removeSongFromPlaylistObject(playlist: Playlist, songsId: String) {
        val songsInAray = DatabaseConverterUtils.stringToArraylist(playlist.songs)
        songsInAray.remove(songsId)
        val songsInString = DatabaseConverterUtils.arraylistToString(songsInAray)
        playlist.songs = songsInString
    }

    override fun decreaseCountInDatabase(playlistId: String, countOfSongs: Int) {

        GlobalScope.launch {
            localDatabase.playlistDAO()
                .setCountOfSongs(playlistId, countOfSongs)

        }
    }

    override fun increaseCountInPlaylistObject(playlist: Playlist) {
        playlist.countOfSongs = playlist.countOfSongs + 1
    }

    override fun increaseCountInDatabase(playlist: Playlist) {
        GlobalScope.launch {
            localDatabase.playlistDAO()
                .setCountOfSongs(playlist.id, playlist.countOfSongs)
        }
    }

    override fun addSongsToPlaylistInObject(playlist: Playlist, songsId: String) {

        val position = findPlaylistPositionInCachedArray(playlist)

        cachedPlaylistArray[position].songs =
            cachedPlaylistArray[position].songs + songsId + ","

        increaseCountInPlaylistObject(cachedPlaylistArray[position])
    }

    override fun addSongsToPlaylistInDatabase(playlist: Playlist, songsId: String) {

        runBlocking {

            for (song_id in songsId) {
                localDatabase.playlistDAO().addSongToPlaylist(
                    playlist.id,
                    playlist.songs
                )
            }
            increaseCountInDatabase(playlist)
            //            TODO(Toast : operation failed! please try later)
        }
    }

    override fun getPlaylistFromDatabase(): ArrayList<Playlist> =
        runBlocking {
            val playlistsList = localDatabase.playlistDAO().getPlaylists()
            val arrayList = arrayListOf<Playlist>()
            for (playlist in playlistsList) {
                arrayList.add(playlist)
            }
            return@runBlocking arrayList
        }


    override fun findPlaylistPositionInCachedArray(playlist: Playlist): Int {
        var position: Int = -1
        while (++position < cachedPlaylistArray.size) {
            if (cachedPlaylistArray[position].id == playlist.id) {
                return position
            }
        }
        return position
    }

    override fun getIdByName(name: String): String {

        for (playlist in cachedPlaylistArray) {
            if (playlist.name == name) {
                return playlist.id
            }
        }
        return ""

    }

    override fun getPlaylistById(id: String): Playlist? {
        for (playlist in cachedPlaylistArray) {
            if (playlist.id == id) {
                return playlist
            }
        }
        return null
    }


//----------------------favourite-------------------------------------

    override fun updateCachedFav() {
        cachedFavArray_Favorites = getFavoritesFromDatabase()
    }

    override fun addSongToFavorites(songsId: Long) {
        val fav = Favorites(songsId)
        applicationScope.launch {
            localDatabase.favoriteDao().addSong(fav)
        }

        SongUtils.getSongById(songsId)?.let { cachedFavArray.add(it) }
    }

    fun removeSongFromDB(song: Song) {

        val fav = Favorites(song.id!!)
        applicationScope.launch {
            localDatabase.favoriteDao().deleteSong(fav)
        }
    }

    private fun removeSongFromCachedFavArray(song: Song) {
        val iter: MutableIterator<Song> = cachedFavArray.iterator()

        while (iter.hasNext()) {
            if (iter.next().id!! == song.id) iter.remove()
        }
    }

    override fun removeSongFromFavorites(song: Song) {
        removeSongFromDB(song)
        removeSongFromCachedFavArray(song)
    }

    override fun getFavoritesFromDatabase(): ArrayList<Favorites> =
        runBlocking {
            val favSongs = localDatabase.favoriteDao().getFavs()

            val arr = arrayListOf<Favorites>()
            arr.addAll(favSongs)
            return@runBlocking arr
        }

    override fun convertFavSongsToRealSongs(): ArrayList<Song> {
        val arrayList = arrayListOf<Song>()
        for (favSong in cachedFavArray_Favorites) {

            val realSong = songsIdToSongModelConverter(favSong)
            if (realSong != null)
                arrayList.add(realSong)
        }

        cachedFavArray = arrayList
        return arrayList
    }

    override fun songsIdToSongModelConverter(favSong: Favorites): Song? {
        val allSongsInStorage = LibraryFragment.viewModel.getDataset() as ArrayList<Song>

        for (song in allSongsInStorage) {
            if (song.id == favSong.songId) {
                return song
            }
        }
        return null
    }


//--------------------------Recently----------------------------

    override fun updateCachedRecently() {
        cachedRecArray_Recently = getRecentlyFromDatabase()
    }


    override fun addSongToRecently(songsId: Long) {
        Log.e("addRoom", songsId.toString())
        val rec = Recently(songsId, System.currentTimeMillis())
        updateCachedRecently()
        Log.e("RoomRepository", songsId.toString())
        applicationScope.launch {
            localDatabase.recentlyDao().addSong(rec)
        }
        SongUtils.getSongById(songsId)?.let { cachedRecArray.add(it) }
    }

    override fun removeSongFromRecently(song: Song) {
        removeSongFromDBRec(song)
        removeSongFromCachedRecArray(song)
    }

    override fun getRecentlyFromDatabase(): ArrayList<Recently> =
        runBlocking {
            val recentlySongs = localDatabase.recentlyDao().getRecentlyTime()

            val arr = arrayListOf<Recently>()
            arr.addAll(recentlySongs)
            return@runBlocking arr
        }


    override fun convertRecentlySongsToRealSongs(): ArrayList<Song> {
        val arrayList = arrayListOf<Song>()
        for (recSong in cachedRecArray_Recently) {
//            Log.e("song", recSong.toString())
            val realSong = songsIdToSongModelConverterRecently(recSong)
            if (realSong != null)
                arrayList.add(realSong)
        }

        cachedRecArray = arrayList
        return arrayList
    }

    override fun songsIdToSongModelConverterRecently(recently: Recently): Song? {
        val allSongsInStorage = LibraryFragment.viewModel.getDataset() as ArrayList<Song>
//        Log.e("allSongInStorage", allSongsInStorage.size.toString())
        for (song in allSongsInStorage) {
            if (song.id == recently.songId) {
                return song
            }
        }
        return null
    }

    fun removeSongFromDBRec(song: Song) {

        val rec = Recently(song.id!!, System.currentTimeMillis())
        applicationScope.launch {
            localDatabase.recentlyDao().deleteSong(rec)
        }
    }

    private fun removeSongFromCachedRecArray(song: Song) {
        val iter: MutableIterator<Song> = cachedRecArray.iterator()

        while (iter.hasNext()) {
            if (iter.next().id!! == song.id) iter.remove()
        }
    }
}