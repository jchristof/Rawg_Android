package com.theobviousexit.rawg.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.theobviousexit.rawg.R
import com.theobviousexit.rawg.media.MediaPlayerHolder
import com.theobviousexit.rawg.media.PlayerState
import kotlinx.android.synthetic.main.game_detail_fragment.*

class GameDetailFragment : Fragment() {

    lateinit var playerHolder: MediaPlayerHolder
    val state = PlayerState()

    companion object {
        fun newInstance() = GameDetailFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.game_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        playerHolder = MediaPlayerHolder(context!!, exoplayerview_activity_video, state)
    }

    override fun onStart() {
        super.onStart()
        playerHolder.start("",{})
    }

    override fun onStop() {
        super.onStop()
        playerHolder.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerHolder.release()
    }
}



