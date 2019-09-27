package com.theobviousexit.rawg.media

import com.google.android.exoplayer2.ui.PlayerView

interface MediaPlayerFactory {

    fun getMediaPlayer(
        playerView: PlayerView,
        mediaSavedState: MediaSavedState,
        canceledByUser:Boolean = false
    ): MediaPlayer

    fun resume()
    fun pause()
    fun destroy()

}