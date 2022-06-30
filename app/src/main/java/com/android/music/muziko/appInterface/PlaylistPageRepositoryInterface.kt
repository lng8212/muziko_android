package com.android.music.muziko.appInterface

import com.android.music.muziko.model.Song

interface PlaylistPageRepositoryInterface {
    fun getSongsIdFromDatabase(): String
    fun songsIdToSongModelConverter(songId: String): Song?
    fun getSongs(): ArrayList<Song>
}