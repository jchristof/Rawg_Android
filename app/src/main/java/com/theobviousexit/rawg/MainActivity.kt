package com.theobviousexit.rawg

import android.app.Activity
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.theobviousexit.rawg.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {

        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            (supportFragmentManager.findFragmentById(R.id.container) as? MainFragment)?.search(query)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        (menu.findItem(R.id.search).actionView as SearchView).apply {
            this.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    (supportFragmentManager.findFragmentById(R.id.container) as? MainFragment)?.search(query!!)

                    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    var view = getCurrentFocus()
                    if (view == null)
                        view = View(this@MainActivity)

                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }

        return true
    }
}
