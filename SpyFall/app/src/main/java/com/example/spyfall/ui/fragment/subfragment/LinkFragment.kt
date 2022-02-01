package com.example.spyfall.ui.fragment.subfragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.spyfall.R
import com.example.spyfall.ui.fragment.listener.CreateGameListener
import com.example.spyfall.ui.viewmodel.LinkViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LinkFragment : Fragment(R.layout.fragment_link_view) {

    private val viewModel: LinkViewModel by viewModels()
    private var createGameListener: CreateGameListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = requireParentFragment().requireParentFragment()
        if(parent is CreateGameListener){
            createGameListener = parent
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("LinkView", "onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("LinkView", "onViewCreated")

        val gameId = viewModel.generateGameId()

        view.findViewById<TextView>(R.id.textViewGameId).text = gameId

        view.findViewById<Button>(R.id.buttonCool).setOnClickListener {
            createGameListener?.createGame(gameId)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("LinkView", "onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LinkView", "onStop")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LinkView", "onPause")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("LinkView", "onSaveInstanceState")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("LinkView", "onDetach")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("LinkView", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LinkView", "onDestroy")
    }
}