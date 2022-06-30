package com.android.music.ui

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import com.android.music.R
import com.android.music.muziko.model.Song
import com.android.music.muziko.utils.ImageUtils
import com.example.kookplayer.utlis.FileUtils

// class to get media from device
class SongsRepository(val context: Context) {

    @SuppressLint("Range")
    fun createSongFromCursor(cursor: Cursor): Song {
        val title = cursor.getString(MediaStore.Audio.AudioColumns.TITLE)
        val duration = cursor.getLong(MediaStore.Audio.AudioColumns.DURATION)
        val data = cursor.getString(MediaStore.Audio.AudioColumns.DATA)
        val id = cursor.getLong(MediaStore.Audio.AudioColumns._ID)
        val dateAdded = cursor.getString(MediaStore.Audio.AudioColumns.DATE_ADDED)
        val artist = cursor.getString(MediaStore.Audio.AudioColumns.ARTIST)
//        val trackNumber = cursor.getString(AudioColumns.TRACK)
        val year = cursor.getInt(MediaStore.Audio.AudioColumns.YEAR)
        val dateModified = cursor.getLong(MediaStore.Audio.AudioColumns.DATE_MODIFIED)
        val artistId = cursor.getLong(MediaStore.Audio.AudioColumns.ARTIST_ID)
        val artistName = cursor.getString(MediaStore.Audio.AudioColumns.ARTIST)
//        val composer = cursor.getString(AudioColumns.COMPOSER)
//        val albumArtist = cursor.getString(AudioColumns.ALBUM_ARTIST)
        val uri = ContentUris
            .withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID))
            )
        val albumId = cursor.getLong(MediaStore.Audio.AudioColumns.ALBUM_ID)
        val size = cursor.getString(MediaStore.Audio.AudioColumns.SIZE)

        val image = ImageUtils.albumArtUriToBitmap(context, albumId)
            ?: BitmapFactory.decodeResource(
                context.resources, R.mipmap.icon
            )


        var bitrate = ""
        if (data != "") {
            val metadata = MediaMetadataRetriever()
            metadata.setDataSource(data)
            bitrate =
                metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE).toString()

        }

        return Song(
            title = title,
            duration = duration,
            data = data,
            dateAdded = dateAdded,
            artist = artist,
            id = id,
            uri = uri,
            albumId = albumId,
            size = size,
            bitrate = bitrate,
            image = image,
            trackNumber = "",
            year = year,
            dateModified = dateModified,
            artistId = artistId,
            artistName = artistName,
            composer = "",
            albumArtist = ""
        )
    }

    fun getListOfSongs(): ArrayList<Song> {
        return getSongsFromStorage()
    }

    private fun getSongsFromStorage(): ArrayList<Song> {
        val songsAreInStorage = ArrayList<Song>()
        val cursor = FileUtils.fetchFiles(
            fileType = FileUtils.FILE_TYPES.MUSIC,
            context = context
        )
        if (cursor != null && cursor.count != 0) {
            do {
                cursor.moveToNext()
                cursor.getLong(MediaStore.Audio.AudioColumns.DURATION)
                if (cursor.getLong(MediaStore.Audio.AudioColumns.DURATION)!! > 60000)
                    songsAreInStorage.add(createSongFromCursor(cursor))
            } while (!cursor.isLast)
        } else {
//                TODO(handle null cursor)
        }
        cursor?.close()
        return songsAreInStorage
    }

    fun getListOfArtists(): ArrayList<Song> {
        val listSong = getSongsFromStorage()
        val list = mutableSetOf<String>()
        val arr = ArrayList<Song>()
        for (i in listSong) {
            list.add(i.artist.toString())
        }
        for (i in list) {
            for (j in listSong) {
                if (j.artist.toString().equals(i)) {
                    arr.add(j)
                    break
                }
            }
        }
        return arr
    }

    fun getSongOfArtists(artist: String): ArrayList<Song> {
        val listSong = getSongsFromStorage()
        val arr = ArrayList<Song>()
        for (i in listSong) {
            if (i.artist.toString().equals(artist)) {
                arr.add(i)
            }
        }
        return arr
    }
}