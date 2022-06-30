package com.android.music.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.android.music.muziko.model.Song
import com.android.music.muziko.viewmodel.BaseViewModel

class SongViewModel : BaseViewModel() {
    lateinit var songsRepository: SongsRepository

    init {
        dataset = MutableLiveData()
    }

    override fun sendDataToFragment(context: Context?, artist: String?) {
        songsRepository = SongsRepository(context!!)
        updateData()
    }

    override fun updateData(data: String?) {
        dataset.value = songsRepository.getListOfSongs()
    }

    override fun getDataset(): ArrayList<*> {
        return dataset.value as ArrayList<Song>
    }
}