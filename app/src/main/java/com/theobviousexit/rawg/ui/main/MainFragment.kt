package com.theobviousexit.rawg.ui.main

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.theobviousexit.rawg.R
import com.theobviousexit.rawg.RawgApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import retrofit2.Retrofit

class MainFragment : Fragment() {

    val retrofit: Retrofit by inject()
    lateinit var recycler:RecyclerView

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        val searchField = activity?.findViewById<EditText>(R.id.search_field)
        searchField.let {
            it?.setOnEditorActionListener { textView, _, _ ->
                val ims =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                ims?.hideSoftInputFromWindow(searchField?.windowToken, 0)
                search(textView.text.toString())
                true
            }
        }

        recycler = activity?.findViewById<RecyclerView>(R.id.games_recycler) ?: return
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.addItemDecoration(object:RecyclerView.ItemDecoration(){
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.top = 16
                outRect.bottom = 16
            }
        })
    }

    private fun search(searchTerm: String) {
        GlobalScope.launch {
            val rawgService = retrofit.create(RawgApi::class.java)
            val games = rawgService.getGames(TextUtils.htmlEncode(searchTerm))

            games.toString()

            withContext(Dispatchers.Main){
                recycler.adapter = SearchResultsAdapter(games)
            }
        }
    }

}
