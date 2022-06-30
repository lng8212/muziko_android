package com.android.music.muziko.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recently_table")
data class Recently(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo val songId: Long,
    @ColumnInfo val time: Long
)