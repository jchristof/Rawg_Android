package com.theobviousexit.rawg.ui.main

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.theobviousexit.rawg.R
import com.theobviousexit.rawg.RawgResponse
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Retrofit

class MainFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private val viewModel by viewModel<SearchViewModel>()

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

        val searchField = activity?.findViewById<EditText>(R.id.search_field)
        searchField.let {
            it?.setOnEditorActionListener { textView, _, _ ->
                val ims =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                ims?.hideSoftInputFromWindow(searchField?.windowToken, 0)
                viewModel.search(textView.text.toString())
                true
            }
        }

        recycler = activity?.findViewById<RecyclerView>(R.id.games_recycler) ?: return
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
                outRect.top = 16
                outRect.bottom = 16
            }
        })
    }

}
