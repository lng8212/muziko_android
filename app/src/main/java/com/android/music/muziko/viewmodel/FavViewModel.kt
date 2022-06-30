package com.android.music.muziko.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.android.music.muziko.model.Song
import com.android.music.muziko.repository.FavRepository

class FavViewModel : BaseViewModel() {
    var favRepository: FavRepository


    init {
        dataset = MutableLiveData()
        favRepository = FavRepository()

    }

    override fun sendDataToFragment(context: Context?, artist: String?) {
        updateData()
    }

    override fun updateData(data: String?) {
        dataset.value = favRepository.getFavSongs() as ArrayList<Any>
    }

    override fun getDataset(): ArrayList<Song> {
        return dataset.value as ArrayList<Song>
    }
}