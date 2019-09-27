package com.theobviousexit.rawg.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.theobviousexit.rawg.IGameSearchProvider
import com.theobviousexit.rawg.R
import com.theobviousexit.rawg.Result
import com.theobviousexit.rawg.databinding.GameSearchResultBinding
import com.theobviousexit.rawg.media.MediaPlayer
import com.theobviousexit.rawg.media.MediaPlayerFactory
import com.theobviousexit.rawg.media.MediaSavedState

class SearchResultsAdapter(
    private val gameSearchProvider: IGameSearchProvider,
    private val mediaPlayerFactory: MediaPlayerFactory,
    private val onGameClicked: (game: Result) -> Unit,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<SearchResultViewHolder>() {

    init {
        gameSearchProvider.notifyGameListChanged = { added, removed, more ->
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        return when (viewType) {
            R.layout.game_search_result -> {

                val binding = GameSearchResultBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                GameSearchResultViewHolder(binding)
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

        (holder as? GameSearchResultViewHolder)?.viewDetachedFromWindow()
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        when (holder) {
            is GameSearchResultViewHolder -> holder.bind(
                gameSearchProvider,
                mediaPlayerFactory,
                onGameClicked,
                lifecycleOwner
            )
            is GameSearchProgressViewHolder -> Unit
        }

    }
}

open class SearchResultViewHolder(val layout: View) : RecyclerView.ViewHolder(layout)
class GameSearchResultViewHolder(private val binding: GameSearchResultBinding) :
    SearchResultViewHolder(binding.root.rootView) {
    private var player: MediaPlayer? = null
    private lateinit var searchResults: Result

    fun viewDetachedFromWindow() = destroy()

    private fun destroy(){
        player?.stop()
        player?.release()
        searchResults.mediaSavedState = null
    }

    fun bind(
        gameSearchProvider: IGameSearchProvider,
        mediaPlayerFactory: MediaPlayerFactory,
        onGameClicked: (game: Result) -> Unit,
        lifecycleOwner: LifecycleOwner
    ) {
        try {
            searchResults = gameSearchProvider.gameList[adapterPosition]

            binding.lifecycleOwner = lifecycleOwner
            binding.item = searchResults
            binding.gameName.setOnClickListener {
                onGameClicked(searchResults)
            }
            binding.video.setOnClickListener {
                binding.video.post {
                    destroy()
                }
            }

            Glide.with(layout).load(searchResults.backgroundImage)
                .transform(CenterCrop(), RoundedCorners(10)).into(binding.image)

            if (!searchResults.hasVideoContent())
                return

            val startPlayback = {
                searchResults.displayVideoMode()
                searchResults.mediaSavedState?.whenReady = true
                player =
                    mediaPlayerFactory.getMediaPlayer(binding.video, searchResults.mediaSavedState!!, true)
                player?.start(searchResults.clip?.clip ?: "") { cancledByUser ->
                    if (cancledByUser)
                        searchResults.mediaSavedState = null

                    player?.release()
                    searchResults.displayImageMode()
                }
            }

            if (searchResults.mediaSavedState != null)
                startPlayback.invoke()


            binding.image.setOnClickListener {
                searchResults.mediaSavedState = MediaSavedState()
                startPlayback.invoke()
            }

        } catch (e: Exception) {
            Log.i("SearchAdapeter", "Crash")
        }
    }
}

class GameSearchProgressViewHolder(layout: View) : SearchResultViewHolder(layout)