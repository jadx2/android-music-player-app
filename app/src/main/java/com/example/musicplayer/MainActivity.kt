package com.example.musicplayer

import android.media.Image
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import com.example.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val mediaPlayer = MediaPlayer.create(this, R.raw.cowboys_from_hell )

        val play = findViewById<ImageButton>(R.id.play_btn)

        binding.playBtn.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                play.setImageResource(R.drawable.pause_arrow)
            } else {
                mediaPlayer.pause()
                play.setImageResource(R.drawable.play_arrow)
            }
        }


    }
}