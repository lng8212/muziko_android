package com.android.music.muziko.appInterface

import com.android.music.muziko.model.Song


interface PassDataForSelectSong {
    fun passDataToInvokingFragment(songs : ArrayList<Song>)
}