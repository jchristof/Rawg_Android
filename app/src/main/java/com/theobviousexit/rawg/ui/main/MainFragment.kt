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
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private val viewModel by viewModel<SearchViewModel>()
    private lateinit var searchResultsAdapter:SearchResultsAdapter

    fun search(query:String){
        viewModel.clear()
        viewModel.search(query, 1, 3)
        recycler.scrollToPosition(0)
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recycler = activity?.findViewById(R.id.games_recycler) ?: return

        when(resources.configuration.orientation != ORIENTATION_PORTRAIT) {
            true -> recycler.layoutManager = LinearLayoutManager(activity)
            false -> recycler.layoutManager = GridLayoutManager(activity, 2)
        }

        searchResultsAdapter = SearchResultsAdapter(viewModel)
        recycler.adapter = searchResultsAdapter

        if(savedInstanceState == null)
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
}
