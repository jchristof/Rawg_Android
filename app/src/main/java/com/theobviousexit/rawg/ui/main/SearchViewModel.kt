package com.theobviousexit.rawg.ui.main

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.theobviousexit.rawg.RawgApi
import com.theobviousexit.rawg.RawgResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class SearchViewModel(val retrofit: Retrofit) : ViewModel() {

    val rawgResponse = MutableLiveData<RawgResponse>()

    fun search(searchTerm: String) {
        GlobalScope.launch {
            val rawgService = retrofit.create(RawgApi::class.java)
            val games = rawgService.getGames(TextUtils.htmlEncode(searchTerm))

            rawgResponse.postValue(games)
        }
    }
}
