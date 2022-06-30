package com.android.music.muziko.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    open lateinit var dataset: MutableLiveData<ArrayList<*>>
    abstract fun sendDataToFragment(context: Context? = null, artist: String? = null)
    abstract fun updateData(data: String? = null)
    abstract fun getDataset(): ArrayList<*>
}