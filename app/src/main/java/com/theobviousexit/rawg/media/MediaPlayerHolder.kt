package com.theobviousexit.rawg.media

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.STATE_ENDED
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

data class PlayerState(
    var window: Int = 0,
    var position: Long = 0,
    var whenReady: Boolean = true
)

class MediaPlayerHolder(
    val context: Context,
    val playerView: PlayerView,
    val playerState: PlayerState
) {
    val player: ExoPlayer
    val state = PlayerState()

    init {
        // Create the player instance.
        player = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
            .also {
                playerView.player = it
            }
    }

    fun start(clipUrl:String, onFinished:()->Unit) {
        with(playerState) {
            player.playWhenReady = whenReady
            player.seekTo(window, position)
        }
        // Load media.
        player.prepare(buildMediaSource(Uri.parse(clipUrl)))
        player.addListener(object : Player.DefaultEventListener() {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)

                if(playbackState == STATE_ENDED) {
                    onFinished()
                    player.removeListener(this)
                }
            }
        })
    }

    private fun buildMediaSource(uri: Uri): ExtractorMediaSource {
        return ExtractorMediaSource.Factory(
            DefaultDataSourceFactory(context, "videoapp")
        ).createMediaSource(uri)
    }

    fun stop() {
        with(player) {
            with(state) {
                position = currentPosition
                window = currentWindowIndex
                whenReady = playWhenReady
            }

            stop()
        }
    }

    fun release() {
        player.release()
    }
}