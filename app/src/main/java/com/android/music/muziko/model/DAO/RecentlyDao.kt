package com.android.music.muziko.model.DAO

import androidx.room.*
import com.android.music.muziko.model.Recently

@Dao
interface RecentlyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSong(song: Recently)

    @Delete
    suspend fun deleteSong(song: Recently)

    @Query("DELETE FROM recently_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM recently_table ORDER BY time DESC")
    fun getRecentlyTime(): List<Recently>
}