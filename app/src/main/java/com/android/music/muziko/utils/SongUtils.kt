package com.android.music.muziko.utils

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.android.music.BuildConfig
import com.android.music.muziko.activity.MainActivity
import com.android.music.muziko.model.Song
import com.android.music.ui.FilePathUtils
import com.android.music.ui.fragments.LibraryFragment
import com.example.kookplayer.utlis.FileUtils


object SongUtils {

    fun shareMusic(context: Context, song: Song) {


        val fileToBeShared = song.data?.let { FileUtils.convertSongToFile(it) }

        if (fileToBeShared != null) {

            val fileUri = FileProvider.getUriForFile(
                context,
                "${BuildConfig.APPLICATION_ID}.FileProvider",
                fileToBeShared
            )

            fileUri?.let { FileUtils.shareFile(context, it) }
        }

    }


    fun deleteMusic(context: Context, activity: Activity, uri: Uri) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val urisToModify = mutableListOf(uri)
            val deletePendingIntent =
                MediaStore.createDeleteRequest(context.contentResolver, urisToModify)

            ActivityCompat.startIntentSenderForResult(
                activity,
                deletePendingIntent.intentSender,
                LibraryFragment.DELETE_REQUEST_CODE,
                null,
                0,
                0,
                0,
                null
            )
        } else {
            context.contentResolver.delete(uri, null, null)
        }
    }

    fun del(songId: String) {
        try {
            val where = "${MediaStore.Audio.AudioColumns._ID} = ?"
            val args = arrayOf(songId)
            val uri = FilePathUtils.getMusicsUri()
            MainActivity.activity.baseContext.contentResolver.delete(uri, where, args)
            LibraryFragment.viewModel.updateData()
        } catch (ignored: Exception) {
        }
    }


    fun getSongById(id: Long): Song? {
        for (song in LibraryFragment.viewModel.getDataset() as ArrayList<Song>) {
            if (song.id == id)
                return song
        }

        return null
    }


}