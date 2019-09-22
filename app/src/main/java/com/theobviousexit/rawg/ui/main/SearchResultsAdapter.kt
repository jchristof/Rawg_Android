package com.theobviousexit.rawg.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.theobviousexit.rawg.R
import com.theobviousexit.rawg.RawgResponse
import java.lang.Exception

class SearchResultsAdapter: RecyclerView.Adapter<GameSearchResultViewHolder>()  {

    var searchResponse: RawgResponse = RawgResponse()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameSearchResultViewHolder {
        val searchResultLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.game_search_result, parent, false)
        return GameSearchResultViewHolder(searchResultLayout)
    }

    override fun getItemCount() = searchResponse.results.size

    override fun onBindViewHolder(holder: GameSearchResultViewHolder, position: Int) {
        try {
            val imageView = holder.layout.findViewById(R.id.image) as ImageView

            val searchResults = searchResponse.results[position]
            Glide.with(holder.layout).load(searchResults.backgroundImage).into(imageView)
            val text = holder.layout.findViewById<TextView>(R.id.game_name)
            text.text = searchResults.name

            val metacritic = holder.layout.findViewById<TextView>(R.id.metacritic)
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
        }
        catch (e:Exception){
            Log.i("SearchAdapeter","Crash")
        }
    }
}

class GameSearchResultViewHolder(val layout: View) : RecyclerView.ViewHolder(layout)