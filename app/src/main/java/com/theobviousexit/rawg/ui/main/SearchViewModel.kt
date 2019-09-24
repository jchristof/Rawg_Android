package com.theobviousexit.rawg.ui.main

import android.net.UrlQuerySanitizer
import android.text.TextUtils
import android.util.Range
import androidx.lifecycle.ViewModel
import com.theobviousexit.rawg.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit

class SearchViewModel(private val retrofit: Retrofit) : IGameSearchProvider, ViewModel() {

    private var lastResponse = RawgResponse()

    override fun hasMore() = lastResponse.next != null

    private var loadStarted = false

    override fun loadMore() {
        if(loadStarted)
            return

        lastResponse.next ?: return
        val sanitizer = UrlQuerySanitizer(lastResponse.next)

        val page = sanitizer.getValue("page").toIntOrNull() ?: return
        val pageSize = sanitizer.getValue("page_size").toIntOrNull() ?: return
        val search = sanitizer.getValue("search") ?: return

        loadStarted = true
        search(search, page, pageSize)
    }


    override var notifyGameListChanged: (added: Range<Int>, removed: Range<Int>, more: Boolean) -> Unit = {_,_,_->}

    override val gameList:ArrayList<Result> = ArrayList()

    fun clear(){
        gameList.clear()
    }

    fun search(searchTerm: String, page:Int, page_size:Int) {
        GlobalScope.launch {
            val rawgService = retrofit.create(RawgApi::class.java)
            val games = rawgService.getGames(TextUtils.htmlEncode(searchTerm), page, page_size)
            lastResponse = games

            withContext(Dispatchers.Main){
                gameList.addAll(games.results)
                notifyGameListChanged(Range(0,0), Range(0,0), false)
                loadStarted = false
            }


        }
    }

    fun bestOfYear(){
        GlobalScope.launch {
            val rawgService = retrofit.create(RawgApi::class.java)
            val games = rawgService.bestOfYear()

            gameList.addAll(games.results)
        }
    }
}
