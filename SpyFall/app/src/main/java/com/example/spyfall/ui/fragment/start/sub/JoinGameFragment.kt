package com.example.spyfall.ui.fragment.start.sub

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.spyfall.R
import com.example.spyfall.ui.listener.JoinGameFragmentListener

class JoinGameFragment : Fragment(R.layout.fragment_join_game) {

    private var joinGameFragmentListener: JoinGameFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = requireParentFragment()
        if (parent is JoinGameFragmentListener){
            joinGameFragmentListener = parent
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val joinGameEditText = view.findViewById<EditText>(R.id.joinGameEditText)

        view.findViewById<Button>(R.id.buttonJoinGame).setOnClickListener {
            val gameId = joinGameEditText.text.toString()
            joinGameFragmentListener?.joinToGame(gameId)
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDetach() {
        super.onDetach()
        joinGameFragmentListener = null
    }
}