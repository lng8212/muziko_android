package com.android.music.muziko.repository

import com.android.music.muziko.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class RecentlyRepository {
    private val applicationScope = CoroutineScope(SupervisorJob())
    companion object{
        var cashedRecArray = RoomRepository.cachedRecArray
    }

    init{
        applicationScope.launch {
            cashedRecArray = RoomRepository.cachedRecArray
        }
    }

    fun getRecSongs(): ArrayList<Song>{
        RoomRepository.updateCachedRecently()
        RoomRepository.convertRecentlySongsToRealSongs()
        return RoomRepository.cachedRecArray
    }

}