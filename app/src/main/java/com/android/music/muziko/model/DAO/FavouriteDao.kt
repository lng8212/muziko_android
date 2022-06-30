package com.android.music.muziko.model.DAO

import androidx.room.*
import com.android.music.ui.Favorites

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSong(song: Favorites)

    @Delete
    suspend fun deleteSong(song: Favorites)

    @Query("DELETE FROM playlist_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM favorites_table")
    fun getFavs(): List<Favorites>
}