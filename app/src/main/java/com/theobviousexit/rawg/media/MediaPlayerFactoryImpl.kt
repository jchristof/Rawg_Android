package com.theobviousexit.rawg.media

import android.content.Context
import com.google.android.exoplayer2.ui.PlayerView

class MediaPlayerFactoryImpl(private val context: Context) : MediaPlayerFactory {
    override fun destroy() {
        mediaPlayerHolder?.let{
            it.stop()
            it.release()
        }

        mediaPlayerHolder = null
    }

    private var mediaPlayerHolder:MediaPlayerHolder? = null

    override fun getMediaPlayer(
        playerView: PlayerView,
        playerState: PlayerState
    ): MediaPlayerHolder {
        mediaPlayerHolder?.let{
            it.stop()
            it.release()
        }

        return MediaPlayerHolder(context, playerView, playerState).apply{
            mediaPlayerHolder = this
        }
    }
}