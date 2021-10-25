package com.example.musicplayer

import android.media.Image
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import com.example.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val trackInfo: TrackInfo =
        TrackInfo("Cowboys From Hell", "Pantera", "Cowboys From Hell")
    lateinit var runnable: Runnable
    private var handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.trackInfo = trackInfo

        val mediaPlayer = MediaPlayer.create(this, R.raw.cowboys_from_hell)
        binding.seekBar.progress = 0
        binding.seekBar.max = mediaPlayer.duration
        binding.songEnd.text = mediaPlayer.duration.toString()
        binding.songCurrent.text = mediaPlayer.currentPosition.toString()

        binding.playBtn.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                binding.playBtn.setImageResource(R.drawable.pause_arrow)
            } else {
                mediaPlayer.pause()
                binding.playBtn.setImageResource(R.drawable.play_arrow)
            }
        }

        binding.seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        runnable = Runnable {
            binding.seekBar.progress = mediaPlayer.currentPosition
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
        mediaPlayer.setOnCompletionListener {
            binding.playBtn.setImageResource(R.drawable.play_arrow)
            binding.seekBar.progress = 0
        }
    }
}

