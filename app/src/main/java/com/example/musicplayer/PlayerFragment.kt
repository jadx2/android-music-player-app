package com.example.musicplayer

import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.musicplayer.databinding.FragmentPlayerBinding

class PlayerFragment : Fragment() {

    private lateinit var binding: FragmentPlayerBinding
    private val trackList = arrayListOf(R.raw.cowboys_from_hell, R.raw.primal_concrete_sledge)
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var songName: String
    private var songIndex = 0
    private var songDuration = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_player, container, false)
        mediaPlayer = MediaPlayer.create(activity, trackList[songIndex])
        songName = resources.getResourceName(trackList[songIndex])

        setHasOptionsMenu(true)
        initializeSeekBar()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflown_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            view?.findNavController()!!
        ) || super.onOptionsItemSelected(item)
    }

    private fun playPause() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            binding.playBtn.setImageResource(R.drawable.pause_arrow)
        } else {
            mediaPlayer.pause()
            binding.playBtn.setImageResource(R.drawable.play_arrow)
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
            songName = resources.getResourceName(trackList[songIndex])
            getMeta(songName)
            binding.songEnd.text = convertToTime(songDuration)
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
            songName = resources.getResourceName(trackList[songIndex])
            binding.songEnd.text = convertToTime(songDuration)
            mediaPlayer.start()
        }
    }

    private fun seekBarChangeHandler() {
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, changed: Boolean) {
                if (changed) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

    }

    private fun initializeSeekBar() {
        binding.seekBar.max = mediaPlayer.duration
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                binding.seekBar.progress = mediaPlayer.currentPosition
                handler.postDelayed(this, DEFAULT_DELAY)
            }
        }, 0)
    }

    private fun convertToTime(milliseconds: Int): String {
        val minutes = milliseconds / TO_SECONDS / TO_MINUTES
        val seconds = milliseconds / TO_SECONDS % TO_MINUTES
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun getMeta(path: String) {
        val fileName = path.split("/")[1]
        val uriPath = "android.resource://" + activity?.packageName + "/raw/$fileName"
        val uri = Uri.parse(uriPath)
        val metaRetriever = MediaMetadataRetriever()
        metaRetriever.setDataSource(context, uri)
        val artist = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
        val song = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        binding.trackTitle.text = song
        binding.bandName.text = artist
    }

    override fun onResume() {
        super.onResume()
        Log.wtf("Jaa", mediaPlayer.currentPosition.toString())
        songName = resources.getResourceName(trackList[songIndex])
        songDuration = mediaPlayer.duration
        binding.songEnd.text = convertToTime(songDuration)
        binding.seekBar.max = songDuration

        binding.playBtn.setOnClickListener {
            playPause()
        }

        binding.nextBtn.setOnClickListener {
            nextSong()
        }

        binding.prevBtn.setOnClickListener {
            prevSong()
        }

        seekBarChangeHandler()
        getMeta(songName)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    companion object {
        const val DEFAULT_DELAY = 1000L
        const val TO_SECONDS = 1000
        const val TO_MINUTES = 60
    }
}