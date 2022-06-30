package com.android.music.ui

import android.database.Cursor

fun Cursor.getLong(columnName: String): Long?{
    return try {
        this.getLong(this.getColumnIndexOrThrow(columnName))
    }
    catch (exception : Exception)
    {
        0
//        throw IllegalStateException("invalid column $columnName")
    }
}

fun Cursor.getString(columnName: String): String?{
    try {
        return this.getString(this.getColumnIndexOrThrow(columnName))
    }
    catch (exception : Exception)
    {
        return ""
//        throw IllegalStateException("invalid column $columnName")
    }
}

fun Cursor.getInt(columnName: String): Int?{
    return try {
        this.getInt(this.getColumnIndexOrThrow(columnName))
    }
    catch (exception : Exception)
    {
        0
//        throw IllegalStateException("invalid column $columnName")
    }
}