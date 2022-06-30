package com.android.music.muziko.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.android.music.muziko.model.Song
import com.android.music.muziko.repository.RecentlyRepository


class RecentlyViewModel : BaseViewModel(){
    var recRepository: RecentlyRepository
    init{
        dataset = MutableLiveData()
        recRepository = RecentlyRepository()
    }

    override fun sendDataToFragment(context: Context?, artist: String?) {
        updateData()
    }

    override fun updateData(data: String?) {
        dataset.value = recRepository.getRecSongs() as ArrayList<Any>
    }

    override fun getDataset(): ArrayList<Song>{
        return dataset.value as ArrayList<Song>
    }
}