package com.theobviousexit.rawg.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.exoplayer2.ui.PlayerView
import com.theobviousexit.rawg.IGameSearchProvider
import com.theobviousexit.rawg.R
import com.theobviousexit.rawg.Result
import com.theobviousexit.rawg.media.MediaPlayerFactory
import com.theobviousexit.rawg.media.MediaPlayer
import com.theobviousexit.rawg.media.PlayerState
import java.lang.Exception
import java.lang.RuntimeException

class SearchResultsAdapter(
    private val gameSearchProvider: IGameSearchProvider,
    private val mediaPlayerFactory: MediaPlayerFactory,
    private val onGameClicked: (game: Result) -> Unit
) : RecyclerView.Adapter<SearchResultViewHolder>() {

    init {
        gameSearchProvider.notifyGameListChanged = { added, removed, more ->
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        return when (viewType) {
            R.layout.game_search_result -> {
                val searchResultLayout = LayoutInflater.from(parent.context)
                    .inflate(R.layout.game_search_result, parent, false)
                GameSearchResultViewHolder(searchResultLayout)
            }
            R.layout.search_progress -> GameSearchProgressViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.search_progress, parent, false)
            )
            else -> throw RuntimeException("Invalid game search result type")
        }
    }

    override fun getItemCount() =
        gameSearchProvider.gameList.size + if (gameSearchProvider.hasMore()) 1 else 0

    override fun getItemViewType(position: Int): Int {
        return if (position == gameSearchProvider.gameList.size) {
            if (gameSearchProvider.gameList.size != 0 && gameSearchProvider.hasMore())
                gameSearchProvider.loadMore()

            R.layout.search_progress
        } else R.layout.game_search_result
    }

    override fun onViewDetachedFromWindow(holder: SearchResultViewHolder) {
        super.onViewDetachedFromWindow(holder)

        (holder as? GameSearchResultViewHolder)?.destroy()
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        when (holder) {
            is GameSearchResultViewHolder -> holder.bind(gameSearchProvider, mediaPlayerFactory, onGameClicked)
            is GameSearchProgressViewHolder -> Unit
        }

    }
}

open class SearchResultViewHolder(val layout: View) : RecyclerView.ViewHolder(layout)
class GameSearchResultViewHolder(layout: View) : SearchResultViewHolder(layout) {
    private var player: MediaPlayer? = null
    private lateinit var imageView: ImageView
    private lateinit var video:PlayerView
    private lateinit var playVideoIcon:ImageView

    private var hasVideoContent = false

    fun destroy() {
        player?.stop()
        player?.release()
    }

    fun bind(gameSearchProvider: IGameSearchProvider, mediaPlayerFactory:MediaPlayerFactory, onGameClicked: (game: Result) -> Unit) {
        try {

            imageView = layout.findViewById(R.id.image) as ImageView
            layout.clipToOutline = true

            val searchResults = gameSearchProvider.gameList[position]
            Glide.with(layout).load(searchResults.backgroundImage).transform(CenterCrop(), RoundedCorners(10)).into(imageView)

            video = layout.findViewById(R.id.video)

            playVideoIcon = layout.findViewById<ImageView>(R.id.play_video)

            hasVideoContent = if(searchResults.clip?.clip != null) true else false
            playVideoIcon.visibility = if(hasVideoContent) View.VISIBLE else View.INVISIBLE

            imageView.setOnClickListener {
                searchResults.clip?.clip?.let {
                    imageView.visibility = View.INVISIBLE
                    video.visibility = View.VISIBLE
                    playVideoIcon.visibility = View.INVISIBLE

                    val state = PlayerState()
                    player = mediaPlayerFactory.getMediaPlayer(video, state)
                    player?.start(it) {
                        player?.release()
                        video.visibility = View.INVISIBLE
                        imageView.visibility = View.VISIBLE
                        playVideoIcon.visibility = View.VISIBLE
                    }
                }
            }

            video.setOnClickListener {
                video.post {
                    destroy()
                }
            }

            val text = layout.findViewById<TextView>(R.id.game_name)
            text.text = searchResults.name
            text.setOnClickListener {
                onGameClicked(searchResults)
            }

            val metacritic = layout.findViewById<TextView>(R.id.metacritic)

            metacritic.visibility =
                if (searchResults.metacritic == 0L) View.GONE else View.VISIBLE
            metacritic.text = searchResults.metacritic.toString()


            layout.findViewById<ImageView>(R.id.platformXbox)?.visibility =
                if (searchResults.platforms.mapNotNull { t -> t.platform?.slug }.find { t ->
                        t.contains("xbox")
                    } != null) View.VISIBLE else View.GONE
            layout.findViewById<ImageView>(R.id.playformPlaystation)?.visibility =
                if (searchResults.platforms.mapNotNull { t -> t.platform?.slug }.find { t ->
                        t.contains("playstation")
                    } != null) View.VISIBLE else View.GONE
            layout.findViewById<ImageView>(R.id.platformSwitch)?.visibility =
                if (searchResults.platforms.mapNotNull { t -> t.platform?.slug }.find { t ->
                        t.contains("switch")
                    } != null) View.VISIBLE else View.GONE
            layout.findViewById<ImageView>(R.id.platformMobile)?.visibility =
                if (searchResults.platforms.mapNotNull { t -> t.platform?.slug }.find { t ->
                        t.contains("mobile")
                    } != null) View.VISIBLE else View.GONE
            layout.findViewById<ImageView>(R.id.platformAtari)?.visibility =
                if (searchResults.platforms.mapNotNull { t -> t.platform?.slug }.find { t ->
                        t.contains("atari")
                    } != null) View.VISIBLE else View.GONE
            layout.findViewById<ImageView>(R.id.platformWindows)?.visibility =
                if (searchResults.platforms.mapNotNull { t -> t.platform?.slug }.find { t ->
                        t.contains("pc")
                    } != null) View.VISIBLE else View.GONE
            layout.findViewById<ImageView>(R.id.platformAmiga)?.visibility =
                if (searchResults.platforms.mapNotNull { t -> t.platform?.slug }.find { t ->
                        t.contains("amiga")
                    } != null) View.VISIBLE else View.GONE
            layout.findViewById<ImageView>(R.id.platformSega)?.visibility =
                if (searchResults.platforms.mapNotNull { t -> t.platform?.slug }.find { t ->
                        t.contains("sega")
                    } != null) View.VISIBLE else View.GONE
            layout.findViewById<ImageView>(R.id.platformAndroid)?.visibility =
                if (searchResults.platforms.mapNotNull { t -> t.platform?.slug }.find { t ->
                        t.contains("android")
                    } != null) View.VISIBLE else View.GONE
            layout.findViewById<ImageView>(R.id.platformLinux)?.visibility =
                if (searchResults.platforms.mapNotNull { t -> t.platform?.slug }.find { t ->
                        t.contains("linux")
                    } != null) View.VISIBLE else View.GONE
        } catch (e: Exception) {
            Log.i("SearchAdapeter", "Crash")
        }
    }
}

class GameSearchProgressViewHolder(layout: View) : SearchResultViewHolder(layout)