package com.android.music.muziko.model.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.music.muziko.model.Playlist

@Dao
interface PlaylistDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPlaylist(playlist: Playlist)


    @Query("DELETE FROM playlist_table WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: String)


    @Query("DELETE FROM playlist_table")
    suspend fun deleteAll()


    @Query("SELECT * FROM playlist_table")
    fun getPlaylists(): List<Playlist>


    @Query("UPDATE playlist_table SET songs=:songs WHERE id = :id")
    suspend fun addSongToPlaylist(id: String, songs: String)

    @Query("SELECT songs FROM playlist_table WHERE id = :id")
    suspend fun getSongsOfPlaylist(id: String): String

    @Query("SELECT countOfSongs FROM playlist_table WHERE id = :id")
    fun getCountOfSongsInPlaylist(id: String): Int

    @Query("UPDATE playlist_table SET countOfSongs = :count WHERE id = :id")
    fun setCountOfSongs(id: String, count: Int)

    @Query("UPDATE playlist_table SET songs = :songs WHERE id = :id")
    fun updateSongs(id: String, songs: String)

}