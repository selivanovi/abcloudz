package com.example.asyncoperations.ui.screens

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asyncoperations.R
import com.example.asyncoperations.ui.recyclerviews.episodes.EpisodesAdapter
import com.example.asyncoperations.ui.viewmodels.EpisodesViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EpisodesFragment : Fragment(R.layout.fragment_details) {

    private val episodesAdapter: EpisodesAdapter = EpisodesAdapter()

    private val episodesViewModel by viewModels<EpisodesViewModel>{
        EpisodesViewModel.Factory(requireContext())
    }

    private val episodesObserver by lazy {
        episodesViewModel.channelCharacters.onEach {
            episodesAdapter.setData(it)
        }.launchIn(lifecycleScope)
    }

    private val errorObserver by lazy {
        episodesViewModel.channelError.onEach {
            showErrorAlertDialog(it)
        }.launchIn(lifecycleScope)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        episodesObserver
        errorObserver
        setRecyclerView(view)
    }

    private fun setRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.episodesRecyclerView)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = episodesAdapter
        recyclerView.layoutManager = layoutManager
    }

    private fun showErrorAlertDialog(it: Throwable) {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle(R.string.title_error_alert_dialog)
            .setMessage(it.message)
            .setPositiveButton(R.string.ok) {_,_ ->
            }
        builder.create().show()
    }
}