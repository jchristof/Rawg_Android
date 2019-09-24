package com.theobviousexit.rawg.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.theobviousexit.rawg.IGameSearchProvider
import com.theobviousexit.rawg.R
import com.theobviousexit.rawg.Result
import java.lang.Exception
import java.lang.RuntimeException

class SearchResultsAdapter(private val gameSearchProvider:IGameSearchProvider, private val onGameClicked:(game:Result)->Unit): RecyclerView.Adapter<SearchResultViewHolder>()  {

    init{
        gameSearchProvider.notifyGameListChanged = { added, removed, more ->
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        return when(viewType) {
            R.layout.game_search_result-> {
                val searchResultLayout = LayoutInflater.from(parent.context)
                    .inflate(R.layout.game_search_result, parent, false)
                 GameSearchResultViewHolder(searchResultLayout)
            }
            R.layout.search_progress-> GameSearchProgressViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.search_progress, parent, false))
            else -> throw RuntimeException("Invalid game search result type")
        }
    }

    override fun getItemCount() = gameSearchProvider.gameList.size + if(gameSearchProvider.hasMore()) 1 else 0

    override fun getItemViewType(position: Int):Int {
        return if (position == gameSearchProvider.gameList.size) {
            if (gameSearchProvider.gameList.size != 0 && gameSearchProvider.hasMore())
                gameSearchProvider.loadMore()

            R.layout.search_progress
        } else R.layout.game_search_result
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        when(holder) {
            is GameSearchResultViewHolder -> {
                try {
                    val imageView = holder.layout.findViewById(R.id.image) as ImageView

                    val searchResults = gameSearchProvider.gameList[position]
                    Glide.with(holder.layout).load(searchResults.backgroundImage).into(imageView)
                    val text = holder.layout.findViewById<TextView>(R.id.game_name)
                    text.text = searchResults.name
                    text.setOnClickListener(object:View.OnClickListener{
                        override fun onClick(p0: View?) {
                            onGameClicked(searchResults)
                        }

                    })

                    val metacritic = holder.layout.findViewById<TextView>(R.id.metacritic)

                    metacritic.visibility = if(searchResults.metacritic == 0L) View.GONE else View.VISIBLE
                    metacritic.text = searchResults.metacritic.toString()


                    holder.layout.findViewById<ImageView>(R.id.platformXbox)?.visibility =
                        if (searchResults.platforms.mapNotNull { t -> t.platform?.slug }.find { t ->
                                t.contains("xbox")
                            } != null) View.VISIBLE else View.GONE
                    holder.layout.findViewById<ImageView>(R.id.playformPlaystation)?.visibility =
                        if (searchResults.platforms.mapNotNull { t -> t.platform?.slug }.find { t ->
                                t.contains("playstation")
                            } != null) View.VISIBLE else View.GONE
                    holder.layout.findViewById<ImageView>(R.id.platformSwitch)?.visibility =
                        if (searchResults.platforms.mapNotNull { t -> t.platform?.slug }.find { t ->
                                t.contains("switch")
                            } != null) View.VISIBLE else View.GONE
                    holder.layout.findViewById<ImageView>(R.id.platformMobile)?.visibility =
                        if (searchResults.platforms.mapNotNull { t -> t.platform?.slug }.find { t ->
                                t.contains("mobile")
                            } != null) View.VISIBLE else View.GONE
                    holder.layout.findViewById<ImageView>(R.id.platformAtari)?.visibility =
                        if (searchResults.platforms.mapNotNull { t -> t.platform?.slug }.find { t ->
                                t.contains("atari")
                            } != null) View.VISIBLE else View.GONE
                    holder.layout.findViewById<ImageView>(R.id.platformWindows)?.visibility =
                        if (searchResults.platforms.mapNotNull { t -> t.platform?.slug }.find { t ->
                                t.contains("pc")
                            } != null) View.VISIBLE else View.GONE
                    holder.layout.findViewById<ImageView>(R.id.platformAmiga)?.visibility =
                        if (searchResults.platforms.mapNotNull { t -> t.platform?.slug }.find { t ->
                                t.contains("amiga")
                            } != null) View.VISIBLE else View.GONE
                    holder.layout.findViewById<ImageView>(R.id.platformSega)?.visibility =
                        if (searchResults.platforms.mapNotNull { t -> t.platform?.slug }.find { t ->
                                t.contains("sega")
                            } != null) View.VISIBLE else View.GONE
                    holder.layout.findViewById<ImageView>(R.id.platformAndroid)?.visibility =
                        if (searchResults.platforms.mapNotNull { t -> t.platform?.slug }.find { t ->
                                t.contains("android")
                            } != null) View.VISIBLE else View.GONE
                    holder.layout.findViewById<ImageView>(R.id.platformLinux)?.visibility =
                        if (searchResults.platforms.mapNotNull { t -> t.platform?.slug }.find { t ->
                                t.contains("linux")
                            } != null) View.VISIBLE else View.GONE
                } catch (e: Exception) {
                    Log.i("SearchAdapeter", "Crash")
                }
            }
            is GameSearchProgressViewHolder ->{

            }
        }

    }
}

open class SearchResultViewHolder(val layout: View) : RecyclerView.ViewHolder(layout)
class GameSearchResultViewHolder(layout: View) : SearchResultViewHolder(layout)
class GameSearchProgressViewHolder(layout: View) : SearchResultViewHolder(layout)