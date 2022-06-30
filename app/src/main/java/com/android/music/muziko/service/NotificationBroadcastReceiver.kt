package com.android.music.muziko.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.android.music.R


class NotificationBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        p0?.sendBroadcast(
            Intent("Songs")
                .putExtra("actionname", p1?.action)
        )
    }
}
