package com.theobviousexit.rawg.media

import android.content.Context
import com.google.android.exoplayer2.ui.PlayerView

class MediaPlayerFactoryImpl(private val context: Context) : MediaPlayerFactory {
    override fun destroy() {
        mediaPlayer?.let{
            it.stop()
            it.release()
        }

        mediaPlayer = null
    }

    private var mediaPlayer:MediaPlayer? = null

    override fun getMediaPlayer(
        playerView: PlayerView,
        playerState: PlayerState
    ): MediaPlayer {
        mediaPlayer?.let{
            it.stop()
            it.release()
        }

        return MediaPlayer(context, playerView, playerState).apply{
            mediaPlayer = this
        }
    }
}