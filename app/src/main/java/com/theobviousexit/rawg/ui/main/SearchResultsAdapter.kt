package com.theobviousexit.rawg.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import com.theobviousexit.rawg.R
import com.theobviousexit.rawg.RawgResponse


class SearchResultsAdapter(private val searchResponse: RawgResponse): RecyclerView.Adapter<GameSearchResultViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameSearchResultViewHolder {
        val searchResultLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.game_search_result, parent, false)
        return GameSearchResultViewHolder(searchResultLayout)
    }

    override fun getItemCount()= searchResponse.results.size

    override fun onBindViewHolder(holder: GameSearchResultViewHolder, position: Int) {
        val imageView = holder.layout.findViewById(R.id.image) as ImageView

        val searchResults = searchResponse.results[position]
        Glide.with(holder.layout).load(searchResults.backgroundImage).into(imageView)
        val text = holder.layout.findViewById<TextView>(R.id.game_name)
        text.text = searchResults.name

//        val carouselView = holder.layout.findViewById<CarouselView>(R.id.carouselView);
//        carouselView.setPageCount(searchResults.shortScreenshots.size)
//
//        carouselView.setImageListener(object: ImageListener {
//            override fun setImageForPosition(position: Int, iv: ImageView?) {
//                val imageView = iv ?: return
//                Glide.with(holder.layout).load(searchResults.shortScreenshots[position].image).override(600, 200).into(imageView)
//            }
//
//        });
    }
}

class GameSearchResultViewHolder(val layout: View) : RecyclerView.ViewHolder(layout){

}