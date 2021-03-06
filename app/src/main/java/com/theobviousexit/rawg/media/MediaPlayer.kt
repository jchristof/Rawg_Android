package com.theobviousexit.rawg.media

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.STATE_ENDED
import com.google.android.exoplayer2.Player.STATE_IDLE
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

data class MediaSavedState(
    var window: Int = 0,
    var position: Long = 0,
    var whenReady: Boolean = true
)

class MediaPlayer(
    private val context: Context,
    private val playerView: PlayerView,
    private val mediaSavedState: MediaSavedState
) {
    var canceledByUser = false
    val player: SimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
        .also {
            playerView.player = it
        }

    fun start(clipUrl:String, onFinished:(canceledByUser:Boolean)->Unit) {
        player.prepare(buildMediaSource(Uri.parse(clipUrl)))

        with(mediaSavedState) {
            player.playWhenReady = whenReady
            player.seekTo(window, position)
        }

        player.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
               // super.onPlayerStateChanged(playWhenReady, playbackState)

                if(playbackState == STATE_ENDED || playbackState == STATE_IDLE) {
                    onFinished(canceledByUser || playbackState == STATE_ENDED)
                    player.removeListener(this)
                }
            }
        })
    }

    private fun buildMediaSource(uri: Uri): ProgressiveMediaSource {
        return ProgressiveMediaSource.Factory(
            DefaultDataSourceFactory(context, "videoapp")
        ).createMediaSource(uri)
    }

    fun stop(canceledByUser:Boolean=false) {
        this.canceledByUser = canceledByUser
        with(player) {
            with(mediaSavedState) {
                position = currentPosition
                window = currentWindowIndex
                whenReady = playWhenReady
            }

            stop()
        }
        playerView.player = null
    }

    fun pause(){
        player.playWhenReady = false
    }

    fun resume(){
        player.playWhenReady = true
    }

    fun release() {
        player.release()
    }
}