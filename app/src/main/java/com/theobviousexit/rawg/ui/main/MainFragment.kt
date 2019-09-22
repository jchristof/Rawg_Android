package com.theobviousexit.rawg.ui.main

import android.graphics.Rect
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.theobviousexit.rawg.R
import com.theobviousexit.rawg.RawgResponse
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private val viewModel by viewModel<SearchViewModel>()

    fun search(query:String){
        viewModel.search(query)
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
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = SearchResultsAdapter()

        viewModel.rawgResponse.observe(this, Observer<RawgResponse>{ response ->
            (recycler.adapter as SearchResultsAdapter).searchResponse = response
        })

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
