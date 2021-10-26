package com.example.musicplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.musicplayer.databinding.FragmentPlayerBinding


class PlayerFragment : Fragment() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var detailsButton: Button
    private lateinit var playButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var seekBar: SeekBar
    private lateinit var trackList: ArrayList<Int>
    private lateinit var title: TextView
    private lateinit var band: TextView
    private var songIndex = 0
    private var songDuration = 0
    private lateinit var songCurrent: TextView
    private lateinit var songEnd: TextView
    private lateinit var runnable: Runnable
    private val handler = Handler(Looper.getMainLooper())
//    private val trackBroadcastReceiver = TrackBroadcastReceiver()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPlayerBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_player, container, false)
        trackList = arrayListOf(R.raw.cowboys_from_hell, R.raw.primal_concrete_sledge)
        title = binding.trackTitle
        band = binding.bandName
        detailsButton = binding.detailsButton
        mediaPlayer = MediaPlayer.create(activity, trackList[songIndex])
        playButton = binding.playBtn
        nextButton = binding.nextBtn
        prevButton = binding.prevBtn
        seekBar = binding.seekBar
        songDuration = mediaPlayer.duration
        songEnd = binding.songEnd
        songCurrent = binding.songCurrent
        songEnd.text = convertToTime(songDuration)
        seekBar.progress = 0
        seekBar.max = songDuration

        playButton.setOnClickListener {
            playPause()
        }

        nextButton.setOnClickListener {
            nextSong()
        }

        prevButton.setOnClickListener {
            prevSong()
        }

        seekBarHandler()

        detailsButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_playerFragment_to_detailsFragment)
        )

        return binding.root
    }

    private fun playPause() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            playButton.setImageResource(R.drawable.pause_arrow)
        } else {
            mediaPlayer.pause()
            playButton.setImageResource(R.drawable.play_arrow)
        }
    }

    private fun nextSong() {
        if (mediaPlayer.isPlaying) {
            if (songIndex < trackList.size - 1) {
                mediaPlayer.reset()
                songIndex++
            } else {
                mediaPlayer.reset()
                songIndex = 0
            }

            mediaPlayer = MediaPlayer.create(activity, trackList[songIndex])
            songDuration = mediaPlayer.duration
            songEnd.text = convertToTime(songDuration)
            mediaPlayer.start()
        }
    }

    private fun prevSong() {
        if (mediaPlayer.isPlaying) {
            if (songIndex > 0) {
                mediaPlayer.reset()
                songIndex--
            } else {
                mediaPlayer.reset()
                songIndex = trackList.size - 1
            }

            mediaPlayer = MediaPlayer.create(activity, trackList[songIndex])
            songDuration = mediaPlayer.duration
            songEnd.text = convertToTime(songDuration)
            mediaPlayer.start()
        }
    }

    private fun seekBarHandler() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, changed: Boolean) {
                if (changed) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        runnable = Runnable {
            seekBar.progress = mediaPlayer.currentPosition
            songCurrent.text = convertToTime(mediaPlayer.currentPosition)
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.play_arrow)
            seekBar.progress = 0
        }
    }


    private fun convertToTime(milliseconds: Int): String {
        val minutes = milliseconds / 1000 / 60
        val seconds = milliseconds / 1000 % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

//    override fun onResume() {
//        super.onResume()
//        val iF = IntentFilter()
//        iF.addAction("com.android.music.metachanged");
//
//        iF.addAction("com.htc.music.metachanged");
//
//        iF.addAction("fm.last.android.metachanged");
//        iF.addAction("com.sec.android.app.music.metachanged");
//        iF.addAction("com.nullsoft.winamp.metachanged");
//        iF.addAction("com.amazon.mp3.metachanged");
//        iF.addAction("com.miui.player.metachanged");
//        iF.addAction("com.real.IMP.metachanged");
//        iF.addAction("com.sonyericsson.music.metachanged");
//        iF.addAction("com.rdio.android.metachanged");
//        iF.addAction("com.samsung.sec.android.MusicPlayer.metachanged");
//        iF.addAction("com.andrew.apollo.metachanged");
//        activity?.registerReceiver(trackBroadcastReceiver, iF)
//    }
//
//    override fun onPause() {
//        super.onPause()
//        activity?.unregisterReceiver(trackBroadcastReceiver)
//    }


    override fun onDestroy() {
        super.onDestroy()
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.release()
        }
    }
}