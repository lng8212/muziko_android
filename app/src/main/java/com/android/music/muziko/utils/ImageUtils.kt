package com.android.music.muziko.utils

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import com.android.music.ui.FilePathUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.io.FileDescriptor


object ImageUtils {

    fun loadImageToImageView(context: Context, imageView: ImageView, image: Bitmap){
        Glide.with(context).load(image).circleCrop().into(imageView)
    }
    fun loadImageToImageView(context: Context, imageView: ImageView, image: Int){
        Glide.with(context).load(image).circleCrop().into(imageView)
    }
    fun loadImgToImgViewNotRound(context: Context, imageView: ImageView, image: Bitmap){
        Glide.with(context).load(image).transform(CenterCrop(),RoundedCorners(50)).into(imageView)
    }
    fun albumArtUriToBitmap(context: Context, album_id: Long?): Bitmap? {
        var bm: Bitmap? = null
        val options = BitmapFactory.Options()
        try {
            val sArtworkUri =
                Uri.parse(FilePathUtils.getAlbumsUri())


            val uri = ContentUris.withAppendedId(sArtworkUri, album_id!!)
            val pfd =
                context.contentResolver.openFileDescriptor(uri, "r")
            if (pfd != null) {
                val fileDescriptor: FileDescriptor? = pfd.fileDescriptor
                bm = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options)
            }
        } catch (exception: java.lang.Exception) {
//           TODO(handle the exception)
        }

        return bm
    }

}