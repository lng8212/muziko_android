package com.android.music.muziko.appInterface

import com.android.music.muziko.model.Playlist

interface PassDataForSelectPlaylist {

    fun passDataToInvokingFragment(playlist : ArrayList<Playlist>)
}