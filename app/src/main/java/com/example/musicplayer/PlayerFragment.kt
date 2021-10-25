package com.example.musicplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import com.example.musicplayer.databinding.FragmentPlayerBinding

lateinit var runnable: Runnable
private val handler = Handler(Looper.getMainLooper())

class PlayerFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPlayerBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_player, container, false)
        val trackList = arrayListOf(R.raw.cowboys_from_hell, R.raw.primal_concrete_sledge)
        var songIndex = 0
        var mediaPlayer = MediaPlayer.create(activity, trackList[songIndex])
        val songDuration = mediaPlayer.duration
        binding.songEnd.text = convertToTime(songDuration)
        binding.seekBar.progress = 0
        binding.seekBar.max = songDuration

        // Player Controllers
        binding.playBtn.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                binding.playBtn.setImageResource(R.drawable.pause_arrow)
            } else {
                mediaPlayer.pause()
                binding.playBtn.setImageResource(R.drawable.play_arrow)
            }
        }

        binding.nextBtn.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                if (songIndex < trackList.size - 1) {
                    mediaPlayer.reset()
                    songIndex++
                } else {
                    mediaPlayer.reset()
                    songIndex = 0
                }

                mediaPlayer = MediaPlayer.create(activity, trackList[songIndex])
                mediaPlayer.start()
            }
        }

        binding.prevBtn.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                if (songIndex > 0) {
                    mediaPlayer.reset()
                    songIndex--
                } else {
                    mediaPlayer.reset()
                    songIndex = trackList.size - 1
                }

                mediaPlayer = MediaPlayer.create(activity, trackList[songIndex])
                mediaPlayer.start()
            }
        }

        // SeekBar
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, changed: Boolean) {
                if (changed) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        runnable = Runnable {
            binding.seekBar.progress = mediaPlayer.currentPosition
            binding.songCurrent.text = convertToTime(mediaPlayer.currentPosition)
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
        mediaPlayer.setOnCompletionListener {
            binding.playBtn.setImageResource(R.drawable.play_arrow)
            binding.seekBar.progress = 0
        }

        return binding.root
    }

    private fun convertToTime(milliseconds: Int): String {
        val minutes = milliseconds / 1000 / 60
        val seconds = milliseconds / 1000 % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}