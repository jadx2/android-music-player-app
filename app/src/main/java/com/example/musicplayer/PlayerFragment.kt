package com.example.musicplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import com.example.musicplayer.databinding.FragmentPlayerBinding

lateinit var runnable: Runnable
private val handler = Handler()

class PlayerFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPlayerBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_player, container, false)
        val mediaPlayer = MediaPlayer.create(activity, R.raw.cowboys_from_hell)
        binding.seekBar.progress = 0
        binding.seekBar.max = mediaPlayer.duration

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
        return binding.root
    }
}