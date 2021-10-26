package com.example.musicplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class TrackBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val track = intent?.getStringExtra("track")
        val artist = intent?.getStringExtra("artist")
        val album = intent?.getStringExtra("album")
        val year = intent?.getStringExtra("year")
        val genre = intent?.getStringExtra("genre")
        if (intent != null) {
            Toast.makeText(context, "Hello!", Toast.LENGTH_SHORT).show()
        }
    }
}