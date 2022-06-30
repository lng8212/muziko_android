package com.android.music.ui

import android.net.Uri
import android.provider.MediaStore

//find file paths for shared/external storage
//using android.os.Environment / getExternalStorageDirectory / etc.

object FilePathUtils {

    fun getMusicsUri(): Uri {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
//      TODO(what will happen if there is no valid storage)
    }

    fun getAlbumsUri(): String {
        return "content://media/external/audio/albumart"
    }

    fun getPlayListsUri(): Uri {
        return MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
    }
}