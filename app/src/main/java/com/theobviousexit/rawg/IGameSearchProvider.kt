package com.theobviousexit.rawg

import android.util.Range

interface IGameSearchProvider {
    var notifyGameListChanged:(added:Range<Int>, removed: Range<Int>, more:Boolean)->Unit
    fun hasMore():Boolean
    val gameList:ArrayList<Result>
    fun loadMore()
}