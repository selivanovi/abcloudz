package com.example.spyfall.ui.fragment.start.sub

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.spyfall.R
import com.example.spyfall.ui.listener.JoinGameFragmentListener

class JoinGameFragment : Fragment(R.layout.fragment_join_game) {

    private var joinGameFragemntListener: JoinGameFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = requireParentFragment()
        if (parent is JoinGameFragmentListener){
            joinGameFragemntListener = parent
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.buttonJoinGame).setOnClickListener {
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDetach() {
        super.onDetach()
        joinGameFragemntListener = null
    }
}