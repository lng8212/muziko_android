package com.android.music.muziko.utils

object DatabaseConverterUtils {
    fun stringToArraylist(songs: String): ArrayList<String> {

        val arr = mutableListOf<String>()

        arr += songs.trim().splitToSequence(',')
            .filter { it.isNotEmpty() }
            .toList()

        return arr as ArrayList<String>
    }

    fun arraylistToString(songs: ArrayList<String>): String {
        var str = ""
        for (songId in songs) {
            str += "$songId,"
        }

        return str
    }
}