package com.theobviousexit.rawg.ui.main

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.print.PrintHelper.ORIENTATION_PORTRAIT
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.theobviousexit.rawg.R
import com.theobviousexit.rawg.Result
import com.theobviousexit.rawg.media.MediaPlayerFactory
import com.theobviousexit.rawg.media.MediaPlayerFactoryImpl
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchResultsFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private val viewModel by viewModel<SearchViewModel>()
    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private lateinit var mediaPlayerFactory: MediaPlayerFactory

    fun search(query: String) {
        viewModel.clear()
        viewModel.search(query, 1, 50)
        recycler.scrollToPosition(0)
    }

    companion object {
        fun newInstance() = SearchResultsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    private fun onGameClicked(result: Result) {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.container, GameDetailFragment.newInstance())?.addToBackStack(null)
            ?.commit()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recycler = activity?.findViewById(R.id.games_recycler) ?: return
        mediaPlayerFactory = MediaPlayerFactoryImpl(context!!)

        when (resources.configuration.orientation != ORIENTATION_PORTRAIT) {
            true -> recycler.layoutManager = LinearLayoutManager(activity)
            false -> recycler.layoutManager = GridLayoutManager(activity, 2)
        }

        searchResultsAdapter = SearchResultsAdapter(viewModel, mediaPlayerFactory, ::onGameClicked)
        recycler.adapter = searchResultsAdapter

        if (savedInstanceState == null)
            viewModel.bestOfYear()

        recycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.top = 8
                outRect.left = 4
                outRect.bottom = 8
                outRect.right = 4
            }
        })
    }

    override fun onResume() {
        super.onResume()
        mediaPlayerFactory.resume()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayerFactory.pause()
    }

    override fun onDestroy() {
        super.onDestroy()

        mediaPlayerFactory.destroy()
    }
}
