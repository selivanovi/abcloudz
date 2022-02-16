package com.example.spyfall.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.spyfall.R
import com.example.spyfall.ui.listener.LinkFragmentListener
import com.example.spyfall.ui.viewmodel.LinkViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LinkFragment : Fragment(R.layout.fragment_create_link) {

    private val viewModel: LinkViewModel by viewModels()
    private var linkFragmentListener: LinkFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = requireParentFragment()
        if(parent is LinkFragmentListener){
            linkFragmentListener = parent
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val gameId = viewModel.generateGameId()

        view.findViewById<TextView>(R.id.textViewGameId).text = gameId

        view.findViewById<Button>(R.id.buttonCool).setOnClickListener {
            linkFragmentListener?.createGame(gameId)
        }
    }

    override fun onDetach() {
        super.onDetach()
        linkFragmentListener = null
    }
}