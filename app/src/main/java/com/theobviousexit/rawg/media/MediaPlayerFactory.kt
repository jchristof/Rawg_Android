package com.theobviousexit.rawg.media

import com.google.android.exoplayer2.ui.PlayerView

interface MediaPlayerFactory {

    fun getMediaPlayer(
        playerView: PlayerView,
        playerState: PlayerState
    ): MediaPlayerHolder

    fun destroy()
}